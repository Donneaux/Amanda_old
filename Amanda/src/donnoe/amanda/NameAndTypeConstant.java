package donnoe.amanda;

/**
 *
 * @author joshuadonnoe
 */
public class NameAndTypeConstant extends Constant {

    public NameAndTypeConstant(ClassFile cF) {
        cF.skip(4);
    }

    @Override
    public void resolve() {
        sb.append("name and type");
    }
    
    
    
}
