package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;
import donnoe.amanda.Blob;
import donnoe.amanda.constant.Constant;
import donnoe.amanda.constant.LoadableConstant;
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

    /**
     *
     */
    public final Future<MethodHandleConstant> handle;

    /**
     *
     */
    public final Future<List<Constant>> args;

    public BootStrapMethod(ClassFile cF) {
        super(cF);
        handle = readConstantFuture();
        args = readObjects(this::readConstantFuture, toListFuture());
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
    }

}