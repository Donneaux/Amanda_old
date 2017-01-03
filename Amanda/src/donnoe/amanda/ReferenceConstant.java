package donnoe.amanda;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class ReferenceConstant extends Constant {

    private final Future<ClassConstant> clazz;
    public ReferenceConstant(ClassFile cF) {
        clazz = cF.readConstantFuture();
        cF.skip(2);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append("reference");
        sb.append(clazz.get());
    }
    
}
