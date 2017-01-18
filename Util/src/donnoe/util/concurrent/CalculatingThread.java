package donnoe.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import static donnoe.util.concurrent.ValueStatus.*;

/**
 *
 * @author joshuadonnoe
 * @param <V>
 */
final class CalculatingThread<V> extends Thread {

    protected V v;

    protected Exception x;

    protected ValueStatus status = PENDING;

    private final Callable<V> callable;

    public CalculatingThread(Callable<V> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            v = callable.call();
            status = KNOWN;
        } catch (ExecutionException | TimeoutException x) {
            this.x = x;
            status = DOES_NOT_EXIST;
        } catch (InterruptedException x) {
            //silent death is correct behavior
            //calculateWithin has already thrown an interruptedException
        } catch (Exception x) {
            throw new AssertionError(x);
        }
    }

    public V getValue() {
        return v;
    }

    public V throwException() throws ExecutionException, InterruptedException {
        try {
            throw x;
        } catch (ExecutionException | InterruptedException x) {
            throw x;
        } catch (Exception x) {
            throw new AssertionError(x);
        }
    }
}
