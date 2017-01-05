package donnoe.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import static donnoe.util.ValueStatus.*;

/**
 *
 * @author joshuadonnoe
 * @param <T>
 */
final class CalculatingThread<T> extends Thread {

    protected T t;

    protected Throwable x;//this is used when we get 

    protected ValueStatus status = PENDING;

    private final Callable<T> callable;

    public CalculatingThread(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            t = callable.call();
            status = KNOWN;
        } catch (ExecutionException x) {
            //we called Future::get and the other thread threw an exception
            //we retrieve that exception
            
            this.x = x.getCause();
            status = DOES_NOT_EXIST;
        } catch (TimeoutException x) {
            //the other thread took too long
            
            this.x = x;
            status = DOES_NOT_EXIST;
        } catch (InterruptedException x) {
            //I dont care if this happens. Die silently.
            //status can be null
        } catch (Exception x) {
            throw new AssertionError(x);
        }
    }

    public T getValue() {
        return t;
    }

    public <T> T getException() throws ExecutionException, InterruptedException, TimeoutException {
        try {
            throw x;
        } catch (TimeoutException x) {
            throw x;
        } catch (Throwable x) {
            throw new ExecutionException(x);
        }
    }
}
