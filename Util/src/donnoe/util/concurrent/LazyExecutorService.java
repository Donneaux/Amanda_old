package donnoe.util.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LazyExecutorService implements ExecutorService {

    private final ExecutorService exec;

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
        return exec.isTerminated();
    }

    @Override
    public void shutdown() {
        exec.shutdown();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return exec.awaitTermination(timeout, unit);
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
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            Thread.dumpStack();
            synchronized (this) {
                if (f == null) {
                    f = Futures.cancelled();
                    return true;
                }
            }
            return f.cancel(mayInterruptIfRunning);
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            synchronized (this) {
                if (f == null) {
                    f = exec.submit(c);
                }
            }
            if (f == null) {
                Thread.dumpStack();
            }
            return f.get();
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            Thread.dumpStack();
            synchronized (this) {
                if (f == null) {
                    f = exec.submit(c);
                }
            }
            return f.get(timeout, unit);
        }

        @Override
        public boolean isCancelled() {
            Thread.dumpStack();
            synchronized (this) {
                if (f == null) {
                    f = exec.submit(c);
                }
            }
            return f.isCancelled();
        }

        @Override
        public boolean isDone() {
            synchronized (this) {
                if (f == null) {
                    f = exec.submit(c);
                }
            }
            System.out.println(""+this +" -> " + f.isDone());
            return f.isDone();
        }

    }
}
