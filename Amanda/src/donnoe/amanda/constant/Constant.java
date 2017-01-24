package donnoe.amanda.constant;

import donnoe.amanda.Blob;
import donnoe.amanda.ClassFile;
import static java.util.Collections.*;
import java.util.HashMap;
import java.util.Map;
import static java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;

/**
 *
 * @author joshuadonnoe
 */
public abstract class Constant extends Blob {


    private static final Map<Integer, BiFunction<ClassFile, Integer, Constant>> CONSTANT_CONSTRUCTORS = unmodifiableMap(
            new HashMap<Integer, BiFunction<ClassFile, Integer, Constant>>() {
        {
            putAll(
                    new HashMap<Integer, Function<ClassFile, Constant>>() {
                        {
                            putAll(of(0x09, 0x0A, 0x0B).collect(toMap(Function.identity(), k -> ReferenceConstant::new)));
                            put(0x01, UTFConstant::new);
                            put(0x03, PrimativeConstant::readIntegerConstant);
                            put(0x04, PrimativeConstant::readFloatConstant);
                            put(0x05, TwoWordPrimativeConstant::readLongConstant);
                            put(0x06, TwoWordPrimativeConstant::readDoubleConstant);
                            put(0x08, StringConstant::new);
                            put(0x0C, NameAndTypeConstant::new);
                            put(0x0F, MethodHandleConstant::new);
                            put(0x10, MethodTypeConstant::new);
                            put(0x12, InvokeDynamicConstant::new);
                        }
                    }.entrySet().stream().collect(toMap(Entry::getKey, e -> (cF, index) -> e.getValue().apply(cF))));
            put(0x07, ClassConstant::new);
        }
    }
    );

    public static Constant readConstant(ClassFile cF, int index) {
        return CONSTANT_CONSTRUCTORS.get(cF.readUnsignedByte()).apply(cF, index);
    }
    protected Constant(ClassFile cF) {
        super(cF);
    }
}
