package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;

/**
 *
 * @author joshuadonnoe
 */
public class TwoWordPrimativeConstant extends PrimativeConstant {

    public TwoWordPrimativeConstant(String s) {
        super(s);
    }

    public static TwoWordPrimativeConstant readLongConstant(ClassFile cF) {
        return new TwoWordPrimativeConstant(String.format("%dl", cF.readLong()));
    }

    public static TwoWordPrimativeConstant readDoubleConstant(ClassFile cF) {
        double d = cF.readDouble();
        return new TwoWordPrimativeConstant(Double.isInfinite(d) ? (String.format("Double.%sTIVE_INFINITY", d > 0 ? "POSI" : "NEGA")) : (Double.isNaN(d) ? "Double.Nan" : String.format("%fd", d)));
    }
}
