package donnoe.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author joshuadonnoe
 * @param <T>
 */
public final class FutureOf<T> implements Future<T> {

    private final T t;

    public FutureOf(T t) {
        this.t = t;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return t;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return t;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }
}
