package donnoe.amanda;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author joshuadonnoe
 */
public class PrimativeConstant extends Constant {
    protected PrimativeConstant(String s) {
        sb.append(s);
    }
    
    public static PrimativeConstant readIntegerConstant(ClassFile cF) {
        return new PrimativeConstant(String.format("%d", cF.readInt()));
    }
    
    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static PrimativeConstant readFloatConstant(ClassFile cF) {
        float f = cF.readFloat();
        return new PrimativeConstant(Float.isInfinite(f) ? (String.format("Float.%sTIVE_INFINITY", f > 0 ? "POSI" : "NEGA")) : (Float.isNaN(f) ? "Float.Nan" : String.format("%ff", f)));
    }

}
