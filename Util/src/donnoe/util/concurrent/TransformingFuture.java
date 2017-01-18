package donnoe.util.concurrent;

import static java.lang.Thread.interrupted;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author joshuadonnoe
 * @param <V>
 */
public final class TransformingFuture<V> extends ForwardingFuture<V> {

    private final Callable<V> c;
    
    public TransformingFuture(Future<?> f, Callable<V> c) {
        super(f);
        this.c = c;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return calculateInterruptably(c);
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return calculateWithin(c, unit.toMillis(timeout));
    }
    
    static <V> V calculateInterruptably(Callable<V> gettable) throws InterruptedException, ExecutionException {
        try {
            return calculateWithin(gettable, 0);
        } catch (TimeoutException x) {
            throw new AssertionError();
        }
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
}
