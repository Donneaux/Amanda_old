package donnoe.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author joshuadonnoe
 * @param <T>
 */
final class CalculatingThread<T> extends Thread {

    /**
     *
     */
    protected T t;

    /**
     *
     */
    protected Throwable x;

    /**
     *
     */
    protected ValueStatus status = ValueStatus.PENDING;
    private final Callable<T> callable;

    /**
     *
     * @param c
     */
    public CalculatingThread(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            t = callable.call();
            status = ValueStatus.KNOWN;
        } catch (ExecutionException x) {
            this.x = x.getCause();
            status = ValueStatus.DOES_NOT_EXIST;
        } catch (InterruptedException | TimeoutException x) {
            //following comment from 2016 iteration
            
            //this interrupted Exception
            //...means that there was an error in getting (call threw an exception), so the larger absttaction
            //...is an ExecutionException caused by x
            
            //following comment from 2017 iteration
            //timeoutexception will happen if something in call timed out
            
            this.x = x;
            status = ValueStatus.DOES_NOT_EXIST;
        } catch (Exception x) {
        }
    }

    /**
     *
     * @return
     */
    public T getValue() {
        return t;
    }

    /**
     *
     * @return
     */
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