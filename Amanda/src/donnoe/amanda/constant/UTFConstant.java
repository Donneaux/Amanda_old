package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;

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
