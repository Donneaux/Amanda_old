package donnoe.util.concurrent;

import static java.lang.System.currentTimeMillis;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author joshuadonnoe
 */
public class UnwrappingFuture<T> extends ForwardingFuture<T> {

    class InnerFutureValue implements Future<T> {

        Future<T> f;

        public InnerFutureValue(Future<T> f) {
            this.f = f;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return checkStatus(f.cancel(mayInterruptIfRunning));
        }

        @Override
        public boolean isCancelled() {
            return checkStatus(f.isCancelled());
        }

        @Override
        public boolean isDone() {
            return checkStatus(f.isDone());
        }

        boolean checkStatus(boolean isDone) {
            if (isDone) {
                try {
                    UnwrappingFuture.this.f = Futures.of(Futures.getNow(f));
                } catch (CancellationException x) {
                    UnwrappingFuture.this.f = Futures.cancelled();
                } catch (IllegalArgumentException x) {
                    UnwrappingFuture.this.f = Futures.throwing(x.getCause());
                } catch (IllegalStateException x) {
                    //precondition: f either could not be cancelled or reported being done

                    //wasn't actually done
                    throw new AssertionError();
                }
                return true;
            }
            return false;
        }

        /**
         * This is the base of a lot of different calls so don't change it to
         * depend on other stuff
         * @param getter
         * @return
         * @throws InterruptedException
         * @throws ExecutionException
         * @throws TimeoutException 
         */
        public T getHelper(Callable<T> c) throws InterruptedException, ExecutionException, TimeoutException {
            try {
                UnwrappingFuture.this.f = Futures.of(c.call());
            } catch (CancellationException x) {
                UnwrappingFuture.this.f = Futures.cancelled();
            } catch (ExecutionException x) {
                UnwrappingFuture.this.f = Futures.throwing(x.getCause());
            } catch (TimeoutException x) {
                throw x;
            } catch (Exception x) {
                synchronized (UnwrappingFuture.class) {
                    System.err.println("Something Went Wrong");
                    x.printStackTrace();
                    System.exit(1);
                }
            }
            @SuppressWarnings("unchecked")
            T t = (T) UnwrappingFuture.this.f.get();
            return t;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            try {
                return getHelper(f::get);
            } catch (TimeoutException x) {
                throw new AssertionError();
            }
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return getHelper(() -> f.get(timeout, unit));
        }
    }

    class OuterFutureValue implements Future<T> {

        Future<Future<T>> f;

        public OuterFutureValue(Future<Future<T>> f) {
            this.f = f;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            //future.cancel will evalute to false if the future was already cancelled
            //so if you can't cancel outer, try and cancel inner
            
            
            //if the outer cancel failed, try to cancel the inner
            
            //notice full circuit: converts to cancelled future even if cancel succeeded
            return f.cancel(mayInterruptIfRunning) | convertToInner().cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            //if the outer is done, then check if the inner is cancelled
            
            //if the outer was canceled then f is replaced with cancelled future
            return f.isDone() && convertToInner().isCancelled();
        }

        @Override
        public boolean isDone() {
            return f.isDone() && convertToInner().isDone();
        }
        
        /**
         * only call if future.done() would return true
         * @return 
         */
        private Future<T> convertToInner() {
            try {
                UnwrappingFuture.this.f = new InnerFutureValue(Futures.getNow(f));
            } catch (CancellationException x) {
                UnwrappingFuture.this.f = Futures.cancelled();
            } catch (IllegalArgumentException x) {
                UnwrappingFuture.this.f = Futures.throwing(x.getCause());
            } catch (IllegalStateException x) {
                //precondition: f either could not be cancelled or reported being done
                
                //wasn't actually done
                throw new AssertionError();
            }
            return UnwrappingFuture.this;
        }

        private Future<T> getHelper(Callable<Future<T>> c) throws ExecutionException, InterruptedException {
            try {
                UnwrappingFuture.this.f = new InnerFutureValue(c.call());
            } catch (CancellationException x) {
                UnwrappingFuture.this.f = Futures.cancelled();
            } catch (ExecutionException x) {
                UnwrappingFuture.this.f = Futures.throwing(x.getCause());
            } catch (InterruptedException x) {
                throw x;
            } catch (Exception x) {
                throw new AssertionError();
            }
            return UnwrappingFuture.this;
        }

        @Override
        public T get() throws ExecutionException, InterruptedException {
            return getHelper(f::get).get();
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
            long end = currentTimeMillis() + unit.toMillis(timeout);
            return getHelper(() -> f.get(end - currentTimeMillis(), MILLISECONDS)).get(end - currentTimeMillis(), MILLISECONDS);
        }

    }

    public UnwrappingFuture(Future<Future<T>> f) {
        super(null);
        this.f = new OuterFutureValue(f);
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        @SuppressWarnings("unchecked")
        T t = (T) f.get();
        return t;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        @SuppressWarnings("unchecked")
        T t = (T) f.get(timeout, unit);
        return t;
    }

}
