package donnoe.amanda;

/**
 *
 * @author joshuadonnoe
 */
public abstract class Blob {
    protected final StringBuilder sb = new StringBuilder();
    public abstract void resolve();

    @Override
    public final String toString() {
        return sb.toString();
    }
    
}