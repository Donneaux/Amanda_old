package donnoe.amanda.attribute;

import donnoe.amanda.Blob;
import donnoe.amanda.ClassFile;
import donnoe.amanda.constant.Constant;
import donnoe.amanda.constant.MethodHandleConstant;
import static donnoe.util.concurrent.Futures.toListFuture;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public class BootStrapMethod extends Blob {

    public final Future<MethodHandleConstant> handle = readConstantFuture();

    public final Future<List<Constant>> args = readObjects(this::readConstantFuture, toListFuture());

    public BootStrapMethod(ClassFile cF) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
    }

}
