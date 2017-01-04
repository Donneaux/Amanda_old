package donnoe.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

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
            Thread.interrupted();
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
