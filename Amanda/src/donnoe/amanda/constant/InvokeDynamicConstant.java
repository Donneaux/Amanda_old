package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;

/**
 *
 * @author joshuadonnoe
 */
public final class InvokeDynamicConstant extends Constant {

    public InvokeDynamicConstant(ClassFile cF) {
        cF.skip(4);
    }

    @Override
    public void resolve() {
        sb.append("invokedynamic");
    }
}
