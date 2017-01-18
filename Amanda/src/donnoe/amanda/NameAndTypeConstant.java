package donnoe.amanda;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author joshuadonnoe
 */
public final class NameAndTypeConstant extends Constant {

    public NameAndTypeConstant(ClassFile cF) {
        cF.skip(4);
    }
    
    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
