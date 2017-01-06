package donnoe.amanda;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class ReferenceConstant extends Constant {

    private final Future<ClassConstant> clazz;
    private final Future<String> nAt;

    public ReferenceConstant(ClassFile cF) {
        clazz = cF.readConstantFuture();
        nAt = cF.readStringFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(clazz.get()).append(' ').append(nAt.get());
    }
}
