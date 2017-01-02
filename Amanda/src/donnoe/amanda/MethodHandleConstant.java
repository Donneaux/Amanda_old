package donnoe.amanda;

/**
 *
 * @author joshuadonnoe
 */
public class MethodHandleConstant extends Constant {

    public MethodHandleConstant(ClassFile cF) {
        cF.skip(3);
    }
    
}
