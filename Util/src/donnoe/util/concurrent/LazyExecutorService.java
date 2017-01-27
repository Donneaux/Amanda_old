package donnoe.util.concurrent;

import java.util.Collection;
import static java.util.Collections.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class LazyExecutorService implements ExecutorService {

    private final ExecutorService exec;
    private final Set<LazyFuture<?>> unstarted = newSetFromMap(new ConcurrentHashMap<>());
    
    
    public LazyExecutorService(ExecutorService exec) {
        this.exec = exec;
    }

    @Override
    public void execute(Runnable command) {
        exec.execute(command);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return exec.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return exec.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return exec.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return exec.invokeAny(tasks, timeout, unit);
    }

    @Override
    public boolean isShutdown() {
        return exec.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return unstarted.isEmpty() && exec.isTerminated();
    }

    @Override
    public void shutdown() {
        exec.shutdown();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long endTime = System.currentTimeMillis() + unit.toMillis(timeout);
        Set<LazyFuture> copy = unstarted.stream().collect(Collectors.toSet());
        copy.forEach(LazyFuture::start);
        return exec.awaitTermination(endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public List<Runnable> shutdownNow() {
        return exec.shutdownNow();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return new LazyFuture<>(task);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return exec.submit(task, null);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return exec.submit(() -> {
            task.run();
            return result;
        });
    }

    class LazyFuture<V> implements Future<V> {

        private final Callable<V> c;
        private Future<V> f;

        public LazyFuture(Callable<V> c) {
            this.c = c;
            unstarted.add(this);
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            synchronized (this) {
                if (f == null) {
                    f = Futures.cancelled();
                    unstarted.remove(this);
                    return true;
                }
            }
            return f.cancel(mayInterruptIfRunning);
        }

        private synchronized void start() {
            if (f == null) {
                f = exec.submit(c);
                unstarted.remove(this);
            }
        }
        
        @Override
        public V get() throws InterruptedException, ExecutionException {
            start();
            return f.get();
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            start();
            return f.get(timeout, unit);
        }

        @Override
        public boolean isCancelled() {
            start();
            return f.isCancelled();
        }

        @Override
        public boolean isDone() {
            start();
            return f.isDone();
        }
    }
}
