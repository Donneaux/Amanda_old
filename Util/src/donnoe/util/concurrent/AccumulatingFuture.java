package donnoe.util.concurrent;

import static donnoe.util.concurrent.ValueStatus.KNOWN;
import static donnoe.util.concurrent.ValueStatus.PENDING;
import static java.lang.System.currentTimeMillis;
import static java.util.Comparator.naturalOrder;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import static java.util.stream.Collectors.reducing;
import java.util.stream.Stream;
import static java.util.function.Function.identity;

/**
 *
 * @author joshuadonnoe
 * @param <T>
 * @param <TT>
 * @param <R>
 */
public final class AccumulatingFuture<T, TT, R> implements Future<R> {

    /**
     * When T == Future&lt;TT&gt;. By Leaf I mean leaf in a tree of composition
     *
     * @param <TT>
     * @param <R>
     * @param streamSupplier
     * @param collector
     * @return
     */
    public static <TT, R> AccumulatingFuture<Future<TT>, TT, R> ofLeafFutures(Supplier<Stream<Future<TT>>> streamSupplier, Collector<TT, ?, R> collector) {
        return new AccumulatingFuture<>(streamSupplier, identity(), collector);
    }

    private final Supplier<Stream<T>> streamSupplier;
    private final Function<T, Stream<Future<?>>> flatMap;
    private final Function<T, Future<TT>> map;
    private final Collector<TT, ?, R> collector;

    /**
     * When T is a composed of just single future (itself), we use the TT futures as the flat stream
     *
     * @param streamSupplier
     * @param map
     * @param collector
     */
    public AccumulatingFuture(Supplier<Stream<T>> streamSupplier, Function<T, Future<TT>> map, Collector<TT, ?, R> collector) {
        this(streamSupplier, map.andThen(Stream::of), map, collector);
    }

    /**
     *
     * @param streamSupplier    a stream of objects
     * @param flatMap           turns an object into a stream of futures that it depends on
     * @param map               turns an object into a future (probably using the futures from flat)
     * @param collector         accumulates the futures' objects into a single object
     */
    public AccumulatingFuture(Supplier<Stream<T>> streamSupplier, Function<T, Stream<Future<?>>> flatMap, Function<T, Future<TT>> map, Collector<TT, ?, R> collector) {
        this.streamSupplier = streamSupplier;
        this.flatMap = flatMap;
        this.map = map;
        this.collector = collector;
    }

    /**
     * 
     * @return all the futures needed to examine state
     */
    private Stream<Future<?>> flatStream() {
        return streamSupplier.get().flatMap(flatMap);
    }

    private Stream<Future<TT>> futureStream() {
        return streamSupplier.get().map(map);
    }

    /**
     * Complying with the general contract, this will return true if something was
     * not already done and therefore was cancelled.
     * @param mayInterruptIfRunning
     * @return 
     */
    
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return flatStream()
                .map(f -> f.cancel(mayInterruptIfRunning))
                .collect(reducing(false, Boolean::logicalOr));
    }

    /**
     * if (anything will throw or is cancelled) or (everything has a value) 
     * @return 
     */
    @Override
    public boolean isDone() {
        return flatStream()
                .map(ValueStatus::getValueStatus)
                .max(naturalOrder()).orElse(KNOWN) != PENDING;
        //with the orelse, it result will be exactly one of the three
        //the max is Known -> everyone is known
        //the max is pending -> nothing is DNE
    }

    
    /**
     * im not super happy with this method
     * 
     * 
     * @return
     * @throws ExecutionException
     * @throws InterruptedException 
     */
    @Override
    public R get() throws ExecutionException, InterruptedException{
        //this is the only gaurenteed way
        Exception[] p = new Exception[1];
        try {
            return futureStream().map(
                    f -> {
                        try {
                            return f.get();
                        } catch (ExecutionException | InterruptedException x) {
                            p[0] = x;
                            throw new CompletionException(null);
                        }
                    }
            ).collect(collector);
        } catch (CompletionException x) {
            try {
                throw p[0];
            } catch (ExecutionException | InterruptedException x2) {
                throw x2;
            } catch (Throwable t) {
                throw x;
            }
        }
    }

    @Override
    public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        //this is the only gaurenteed way
        Exception[] p = new Exception[1];
        try {
            long end = currentTimeMillis() + unit.toMillis(timeout);
            return futureStream().map(
                    f -> {
                        try {
                            return f.get(end - currentTimeMillis(), MILLISECONDS);
                        } catch (ExecutionException | InterruptedException | TimeoutException x) {
                            p[0] = x;
                            throw new CompletionException(null);
                        }
                    }
            ).collect(collector);
        } catch (CompletionException x) {
            try {
                throw p[0];
            } catch (ExecutionException | InterruptedException | TimeoutException x2) {
                throw x2;
            } catch (Throwable t) {
                throw x;
            }
        }
    }

    @Override
    public boolean isCancelled() {
        return flatStream().anyMatch(Future::isCancelled);
    }
}
