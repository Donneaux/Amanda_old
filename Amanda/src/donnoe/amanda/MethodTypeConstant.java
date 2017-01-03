package donnoe.amanda;

/**
 *
 * @author joshuadonnoe
 */
public final class MethodTypeConstant extends Constant {

    public MethodTypeConstant(ClassFile cF) {
        cF.skip(2);
    }

    @Override
    public void resolve() {
        sb.append("method type");
    }
    
    
}
