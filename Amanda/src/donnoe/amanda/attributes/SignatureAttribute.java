package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class SignatureAttribute extends RecognizedAttribute {

    /**
     *
     */
    public final Future<List<String>> sig;
    
    public SignatureAttribute(ClassFile cF) {
        super(cF);
        sig = readTypesFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {}
    
}