package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import donnoe.amanda.Constant;

/**
 *
 * @author joshuadonnoe
 */
public final class UTFConstant extends Constant {

    public UTFConstant(ClassFile cF) {
        sb.append(cF.readUTF());
    }

    @Override
    public void resolve() {
    }
}
