package donnoe.amanda;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public class UTFBasedConstant extends Constant {

    protected final Future<UTFConstant> utf;

    public UTFBasedConstant(ClassFile cF) {
        utf = cF.readConstantFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append("single");
    }
}
