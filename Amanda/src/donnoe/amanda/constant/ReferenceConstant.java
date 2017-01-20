package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class ReferenceConstant extends Constant {

    public final Future<String> clazz;
    public final Future<NameAndTypeConstant> nAt;

    public ReferenceConstant(ClassFile cF) {
        super(cF);
        clazz = cF.readShortStringFuture();
        nAt = cF.readConstantFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        try {
            clazz.get();
        } catch (InterruptedException x) {
            System.err.println(nAt.get());
            x.printStackTrace();
        }
        sb.append(clazz.get()).append(' ').append(nAt.get());
    }
}
