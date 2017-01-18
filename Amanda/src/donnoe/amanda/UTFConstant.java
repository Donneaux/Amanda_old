package donnoe.amanda;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author joshuadonnoe
 */
public final class UTFConstant extends Constant {

    public UTFConstant(ClassFile cF) {
        cF.readUTF();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
    }
    
}
