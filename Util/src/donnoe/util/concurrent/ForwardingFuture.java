package donnoe.util.concurrent;

import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 * @param <V>
 */
public abstract class ForwardingFuture<V> implements Future<V> {

    protected volatile Future<?> f;

    /**
     *
     * @param f must be untyped so that we can transform f
     */
    ForwardingFuture(Future<?> f) {
        this.f = f;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return f.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return f.isCancelled();
    }

    @Override
    public boolean isDone() {
        return f.isDone();
    }
}
