package donnoe.amanda;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author joshuadonnoe
 */
public abstract class Blob {
    protected final StringBuilder sb = new StringBuilder();
    public abstract void resolve() throws ExecutionException, InterruptedException;

    @Override
    public final String toString() {
        return sb.toString();
    }
    
}
