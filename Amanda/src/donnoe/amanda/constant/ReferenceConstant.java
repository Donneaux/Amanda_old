package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class ReferenceConstant extends Constant {

    public final Future<String> clazz = cF.readShortStringFuture();
    public final Future<NameAndTypeConstant> nAt = cF.readConstantFuture();

    public ReferenceConstant(ClassFile cF) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(clazz.get()).append('.').append(nAt.get().name.get());
    }
}
