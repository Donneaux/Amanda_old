package donnoe.amanda;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author joshuadonnoe
 */
public final class MethodTypeConstant extends Constant {

    public MethodTypeConstant(ClassFile cF) {
        cF.skip(2);
    }
    
    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
