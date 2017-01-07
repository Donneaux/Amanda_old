package donnoe.amanda.constant;

import donnoe.amanda.Blob;
import donnoe.amanda.ClassFile;
import static donnoe.amanda.constant.PrimativeConstant.*;
import static donnoe.amanda.constant.TwoWordPrimativeConstant.*;
/**
 *
 * @author joshuadonnoe
 */
public abstract class Constant extends Blob {

    public static Constant readConstant(ClassFile cF, int index) {
        int b = cF.readUnsignedByte();
        switch (b) {
            case 0x01:
                return new UTFConstant(cF);
            case 0x03:
                return readIntegerConstant(cF);
            case 0x04:
                return readFloatConstant(cF);
            case 0x05:
                return readLongConstant(cF);
            case 0x06:
                return readDoubleConstant(cF);
            case 0x07:
                return new ClassConstant(cF, index);
            case 0x08:
                return new StringConstant(cF);
            case 0x09:
            case 0x0A:
            case 0x0B:
                return new ReferenceConstant(cF);
            case 0x0C:
                return new NameAndTypeConstant(cF);
            case 0x0F:
                return new MethodHandleConstant(cF);
            case 0x10:
                return new MethodTypeConstant(cF);
            case 0x12:
                return new InvokeDynamicConstant(cF);
            default:
                throw new UnsupportedOperationException(String.format("0x%02X", b));
        }
    }
}
