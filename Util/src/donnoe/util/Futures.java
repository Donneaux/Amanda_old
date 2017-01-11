package donnoe.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import static java.lang.Thread.interrupted;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author joshuadonnoe
 */
public class Futures {
    private Futures() {}
    
    public static <V> V getNow(Future<V> f) {
        return FutureStatus.getNow(f);
    }
    
    public static <V> Future<V> cast(Future<?> f) {
        @SuppressWarnings("unchecked") Future<V> rv = 
        (Future<V>) f;
        return rv;
    }
    
    private static final Future<?> FUTURE_OF_NULL = new FutureOf<>(null);

    private static final Map<Object, Future<?>> FUTURES = new ConcurrentHashMap<>();

    /**
     *
     * @param <T>
     * @param t
     * @return
     */
    public static <T> Future<T> of(T t) {
        return cast(t == null ? FUTURE_OF_NULL : FUTURES.computeIfAbsent(t, FutureOf::new));
    }

    private static final Future<?> CANCELLED = new Future<Object>() {

        @Override
        public Object get() throws InterruptedException, ExecutionException {
            throw new CancellationException();
        }

        @Override
        public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            throw new CancellationException();
        }

        @Override
        public boolean isCancelled() {
            return true;
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }
    };
    
    public static <T> Future<T> cancelled() {
        return cast(CANCELLED);
    }
    
    public static <T> Future<T> throwing(Throwable t) {
        return new Future<T>() {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return true;
            }

            @Override
            public T get() throws InterruptedException, ExecutionException {
                throw new ExecutionException(t);
            }

            @Override
            public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                throw new ExecutionException(t);
            }
        };
    }    
    
    public static <T, R> Future<R> transform(Future<T> future, Function<T, R> function) {
        return new ForwardingFuture<R>(future) {
            @Override
            public R get() throws InterruptedException, ExecutionException {
                return calculateInterruptably(() -> function.apply(future.get()));
            }
            
            @Override
            public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return calculateWithin(() -> function.apply(future.get()), unit.toMillis(timeout));
            }
        };
    }
    
    static <V> V calculateWithin(Callable<V> callable, long millis) throws InterruptedException, TimeoutException, ExecutionException {
        CalculatingThread<V> thread = new CalculatingThread<>(callable);
        thread.start();
        try {
            thread.join(millis);
            return thread.status.get(thread);
        } catch (InterruptedException x) {
            //if this thread is interrupted, then interrupt the calculating thread
            thread.interrupt();
            //thread can die now, right?
            
            //https://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html
            //"By convention, any method that exits by throwing an InterruptedException...
            //clears interrupt status when it does so. However, it's always possible...
            //that interrupt status will immediately be set again, by another thread invoking interrupt."
            interrupted();
            throw x;
        }
    }

    static <V> V calculateInterruptably(Callable<V> gettable) throws InterruptedException, ExecutionException {
        try {
            return calculateWithin(gettable, 0);
        } catch (TimeoutException x) {
            throw new AssertionError();
        }
    }
}
