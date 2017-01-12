package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import donnoe.amanda.constant.Constant;
import static java.lang.String.*;
import static java.lang.Double.*;

/**
 *
 * @author joshuadonnoe
 */
public class PrimativeConstant extends Constant {

    protected PrimativeConstant(ClassFile cF, String s) {
        super(cF);
        sb.append(s);
    }

    public static PrimativeConstant readIntegerConstant(ClassFile cF) {
        return new PrimativeConstant(cF, String.format("%d", cF.readInt()));
    }

    public static String getLiteral(double value, String type, char suffix) {
        //this is a good candidate for a default map using doublebits toLong

        return Double.isInfinite(value) ? (format("%s.%sTIVE_INFINITY", type, value > 0 ? "POSI" : "NEGA")) : (isNaN(value) ? "Float.Nan" : format("%ff", value));
    }

    public static PrimativeConstant readFloatConstant(ClassFile cF) {
        float f = cF.readFloat();
        return new PrimativeConstant(cF, Float.isInfinite(f) ? (format("Float.%sTIVE_INFINITY", f > 0 ? "POSI" : "NEGA")) : (isNaN(f) ? "Float.Nan" : format("%ff", f)));
    }

    @Override
    public void resolve() {
    }
}
