package donnoe.amanda;

/**
 *
 * @author joshuadonnoe
 */
public class UTFConstant extends Constant {

    public UTFConstant(ClassFile cF) {
        cF.readUTF();
    }
    
}
