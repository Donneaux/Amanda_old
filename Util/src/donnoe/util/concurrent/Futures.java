package donnoe.util.concurrent;

import donnoe.util.ReadOnlyEntry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.stream.Collectors.*;
import static donnoe.util.concurrent.AccumulatingFuture.ofLeafFutures;
import static java.util.Arrays.*;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import static java.util.Map.Entry;
import java.util.stream.Stream;

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
        return help(new TransformingFuture<>(future, () -> function.apply(future.get())));
    }
    
    public static <T, U, R> Future<R> transform(Future<T> future1, Future<U> future2, BiFunction<T, U, R> function) {
        return help(
            new TransformingFuture<>(
                transformList(asList((Future)future1, future2)),
                () -> function.apply(future1.get(), future2.get())
            )
        );
    }
    
    public static <T> Future<T> help(Future<T> future) {
        return new UnwrappingFuture<>(Futures.of(future));
    }
    
    public static <T> Future<List<T>> transformList(List<Future<T>> list) {
        return help(
                ofLeafFutures(
                        list::stream,
                        collectingAndThen(
                                toList(),
                                Collections::unmodifiableList
                        )
                )
        );
    }
    
    public static <K, V> Future<Entry<K, V>> transformEntryWithKnownKey(Entry<K, Future<V>> entry) {
        Future<Entry<K, V>> future = help(new ForwardingFuture<Entry<K, V>>(entry.getValue()) {

            @Override
            public Entry<K, V> get() throws InterruptedException, ExecutionException {
                return new ReadOnlyEntry<>(entry.getKey(), entry.getValue().get());
            }

            @Override
            public Entry<K, V> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return new ReadOnlyEntry<>(entry.getKey(), entry.getValue().get(timeout, unit));
            }

        });
        return future;
    }
    
    public static <K, V> Future<Map<K, V>> transformMapWithKnownKeys(Map<K, Future<V>> map) {
        return help(
                new AccumulatingFuture<>(
                        map.entrySet()::stream,
                        e -> Stream.of(e.getValue()),
                        Futures::transformEntryWithKnownKey,
                        toMap(Entry::getKey, Entry::getValue)
                )
        );
    }
    
    public static <T> Collector<Future<T>, ?, Future<List<T>>> toListFuture() {
        return collectingAndThen(toList(), Futures::transformList);
    }
}
