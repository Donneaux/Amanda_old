package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;

/**
 *
 * @author joshuadonnoe
 */
public final class UTFConstant extends Constant implements ValueConstant {

    public UTFConstant(ClassFile cF) {
        super(cF);
        sb.append(cF.readUTF());
    }

    @Override
    public void resolve() {
    }
}
