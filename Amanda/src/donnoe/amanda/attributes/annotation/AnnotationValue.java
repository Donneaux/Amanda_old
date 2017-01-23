package donnoe.amanda.attributes.annotation;

import donnoe.amanda.ClassFile;
import donnoe.amanda.Blob;
import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author joshuadonnoe
 */
public abstract class AnnotationValue extends Blob {
    
    protected AnnotationValue(ClassFile cF) {
        super(cF);
    }
    
    private static final Map<Character, Function<ClassFile, AnnotationValue>> CONSTRUCTORS;
    
    static {
        Map<Character, Function<ClassFile, AnnotationValue>> constructors = Stream.of('B', 'S', 'I', 'J', 'F', 'D').collect(Collectors.toMap(Function.identity(), c -> LiteralAnnotationValue::new));
        constructors.put('Z', LiteralAnnotationValue::readBooleanAnnotationValue);
        constructors.put('C', LiteralAnnotationValue::readCharacterAnnotationValue);
        constructors.put('s', LiteralAnnotationValue::readStringAnnotationValue);
        constructors.put('e', EnumAnnotationValue::new);
        constructors.put('@', Annotation::new);
//        constructors.put('[', AnnotationValueArray::new);
        CONSTRUCTORS = unmodifiableMap(new HashMap<>());
    }

    /**
     *
     * @param classFile
     * @return
     */
    public static AnnotationValue readAnnotationValue(final ClassFile classFile) {
        return CONSTRUCTORS.get((char) classFile.readUnsignedByte()).apply(classFile);
    }
}