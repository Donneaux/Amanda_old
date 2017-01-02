package donnoe.amanda;

/**
 *
 * @author joshuadonnoe
 */
public class InvokeDynamicConstant extends Constant {

    public InvokeDynamicConstant(ClassFile cF) {
        cF.skip(4);
    }
    
}
