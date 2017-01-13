package donnoe.amanda;

import java.io.DataInputStream;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author joshuadonnoe
 */
public abstract class Blob {

    protected Blob(ClassFile cF) {
        this.cF = cF;
    }

    protected ClassFile cF;
    
    protected final DataInputStream dis() {
        return cF.in;
    }
    
    protected final StringBuilder sb = new StringBuilder();

    public abstract void resolve() throws ExecutionException, InterruptedException;

    @Override
    public final String toString() {
        return sb.toString();
    }

}
