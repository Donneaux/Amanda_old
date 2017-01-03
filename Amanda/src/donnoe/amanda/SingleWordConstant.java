package donnoe.amanda;

import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public abstract class SingleWordConstant extends Constant {

    protected final Future<UTFConstant> utfFuture;
    
    public SingleWordConstant(ClassFile cF) {
        utfFuture = cF.readConstantFuture();
    }
    
}
