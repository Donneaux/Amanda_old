package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import static java.lang.Double.*;
import static java.lang.String.format;

/**
 *
 * @author joshuadonnoe
 */
public class TwoWordPrimativeConstant extends PrimativeConstant {

    public TwoWordPrimativeConstant(ClassFile cF, String s) {
        super(cF, s);
    }

    public static TwoWordPrimativeConstant readLongConstant(ClassFile cF) {
        return new TwoWordPrimativeConstant(cF, format("%dl", cF.readLong()));
    }

    public static TwoWordPrimativeConstant readDoubleConstant(ClassFile cF) {
        double d = cF.readDouble();
        return new TwoWordPrimativeConstant(cF, isInfinite(d) ? (format("Double.%sTIVE_INFINITY", d > 0 ? "POSI" : "NEGA")) : (isNaN(d) ? "Double.Nan" : format("%fd", d)));
    }
}
