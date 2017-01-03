package donnoe.amanda;

/**
 *
 * @author joshuadonnoe
 */
public final class MethodHandleConstant extends Constant {

    public MethodHandleConstant(ClassFile cF) {
        cF.skip(3);
    }

    @Override
    public void resolve() {
        sb.append("method handle");
    }
    
    
}
