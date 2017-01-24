package donnoe.amanda.attribute.annotation;

import donnoe.amanda.Blob;
import donnoe.amanda.ClassFile;
import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Stream.of;

/**
 *
 * @author joshuadonnoe
 */
public abstract class AnnotationValue extends Blob {

    protected AnnotationValue(ClassFile cF) {
        super(cF);
    }

    private static final Map<Character, Function<ClassFile, AnnotationValue>> CONSTRUCTORS = unmodifiableMap(
            new HashMap<Character, Function<ClassFile, AnnotationValue>>() {
        {
            putAll(of('B', 'S', 'I', 'J', 'F', 'D').collect(Collectors.toMap(Function.identity(), c -> LiteralAnnotationValue::new)));
            put('Z', LiteralAnnotationValue::readBooleanAnnotationValue);
            put('C', LiteralAnnotationValue::readCharacterAnnotationValue);
            put('s', LiteralAnnotationValue::readStringAnnotationValue);
            put('e', EnumAnnotationValue::new);
            put('@', Annotation::new);
            put('[', AnnotationValueArray::new);
        }
    }
    );

    /**
     *
     * @param classFile
     * @return
     */
    public static AnnotationValue readAnnotationValue(final ClassFile classFile) {
        return CONSTRUCTORS.get((char) classFile.readUnsignedByte()).apply(classFile);
    }
}
