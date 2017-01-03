package donnoe.amanda;

/**
 *
 * @author joshuadonnoe
 */
public final class ReferenceConstant extends Constant {

    public ReferenceConstant(ClassFile cF) {
        cF.skip(4);
    }

    @Override
    public void resolve() {
        sb.append("reference");
    }
    
}
