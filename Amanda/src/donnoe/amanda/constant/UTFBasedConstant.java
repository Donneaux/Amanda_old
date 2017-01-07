package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public abstract class UTFBasedConstant extends Constant {

    protected final Future<String> utf;

    public UTFBasedConstant(ClassFile cF) {
        utf = cF.readStringFuture();
    }
}
