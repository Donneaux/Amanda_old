package donnoe.amanda;

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
                return PrimativeConstant.readIntegerConstant(cF);
            case 0x04:
                return PrimativeConstant.readFloatConstant(cF);
            case 0x05:
                return TwoWordPrimativeConstant.readLongConstant(cF);
            case 0x06:
                return TwoWordPrimativeConstant.readDoubleConstant(cF);
            case 0x07:
                return new ClassConstant(cF, index);
            case 0x08:
                return new UTFBasedConstant(cF);
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
