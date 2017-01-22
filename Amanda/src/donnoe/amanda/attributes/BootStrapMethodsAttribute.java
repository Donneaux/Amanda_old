package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class BootStrapMethodsAttribute extends RecognizedAttribute {

    /**
     *
     */
    public final Future<List<BootStrapMethod>> methods;
    
    /**
     *
     * @param cF
     */
    public BootStrapMethodsAttribute(ClassFile cF) {
        super(cF);
        methods = cF.readItemFutureList(() -> new BootStrapMethod(cF), readUnsignedShort());
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {}
    
    
}