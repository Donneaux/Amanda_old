package donnoe.util;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static donnoe.util.ValueStatus.*;

/**
 *
 * @author joshuadonnoe
 */
public enum FutureStatus {
    CANCELLED(DOES_NOT_EXIST) {
        @Override
        public <V> V getNowHelper(Future<V> f) {
            throw new CancellationException();
        }
    },
    THREW_EXCEPTION(DOES_NOT_EXIST) {
        @Override
        public <V> V getNowHelper(Future<V> f) {
            throw new IllegalArgumentException(exceptions.get(f));
        }
    },
    WAITING(PENDING) {
        @Override
        public <V> V getNowHelper(Future<V> f) {
            throw new IllegalStateException("Future is not finished");
        }

    },
    COMPLETED(KNOWN) {
        @Override
        public <V> V getNowHelper(Future<V> f) {
            @SuppressWarnings("unchecked")
            V v = (V) values.get(f);
            return v;
        }
    };

    final ValueStatus valueStatus;

    private FutureStatus(ValueStatus valueStatus) {
        this.valueStatus = valueStatus;
    }

//unsupported   Thrown to indicate that the requested operation is not supported.
//argument      Thrown to indicate that a method has been passed an illegal or inappropriate argument.    
//state         Signals that a method has been invoked at an illegal or inappropriate time. In other words, the Java environment or Java application is not in an appropriate state for the requested operation.
//unsupported   "i can't do that" N/A
//argument      "i can't do that to these arguments" the Future has not value
//state         "i can't do that now"    the future is not done
    public static <V> V getNow(Future<V> f) {
        return getFutureStatus(f).getNowHelper(f);
    }

    public abstract <V> V getNowHelper(Future<V> f);

    private static Map<Future, Exception> exceptions = new WeakHashMap<>();

    private static Map<Future, Object> values = new WeakHashMap<>();

    public static FutureStatus getFutureStatus(Future<?> f) {
        if (!f.isDone()) {
            return WAITING;
        }
        try {
            values.put(f, f.get());
            return COMPLETED;
        } catch (ExecutionException x) {
            exceptions.put(f, x);
            return THREW_EXCEPTION;
        } catch (InterruptedException x) {
            throw new AssertionError("The future was finished so thread can't have been interrupted ");
        } catch (ClassCastException x) {
            return CANCELLED;
        }
    }
}
