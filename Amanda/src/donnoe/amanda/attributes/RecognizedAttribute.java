package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author joshuadonnoe
 */
public abstract class RecognizedAttribute extends Attribute {

    public RecognizedAttribute(ClassFile cF) {
        super(cF);
        skip(4);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
