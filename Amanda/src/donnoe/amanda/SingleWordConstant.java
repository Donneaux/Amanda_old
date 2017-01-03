package donnoe.amanda;

/**
 *
 * @author joshuadonnoe
 */
public class SingleWordConstant extends Constant {

    public SingleWordConstant(ClassFile cF) {
        cF.skip(2);
    }

    @Override
    public void resolve() {
        sb.append("single");
    }
    
    
}
