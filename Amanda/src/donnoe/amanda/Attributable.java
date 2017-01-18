package donnoe.amanda;

import donnoe.amanda.attributes.UnrecognizedAttribute;
import donnoe.amanda.attributes.IgnoredAttribute;
import donnoe.amanda.attributes.Attribute;
import donnoe.amanda.attributes.InnerClassesAttribute;
import donnoe.util.DefaultMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Stream.of;
import static java.util.stream.Collectors.*;
import static java.util.Collections.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public abstract class Attributable extends Blob {

    private static final Map<String, BiFunction<ClassFile, String, Attribute>> ATTRIBUTE_CONSTRUCTORS = unmodifiableMap(
            new DefaultMap<String, BiFunction<ClassFile, String, Attribute>> (
                    new HashMap<String, Function<ClassFile, Attribute>>() {{
                        putAll(of("SourceFile").collect(toMap(s -> s, s -> IgnoredAttribute::new)));
//                        put("InnerClasses", InnerClassesAttribute::new);
                    }}.entrySet().stream().collect(toMap(
                            Map.Entry::getKey,
                            e -> (cF, s) -> e.getValue().apply(cF)
                    )),
                    UnrecognizedAttribute::new
            )
    );

    static {
        final Map<String, Function<ClassFile, Attribute>> constructors = of("SourceFile", "LineNumberTable", "LocalVariableTable", "LocalVariableTypeTable", "org.netbeans.SourceLevelAnnotations", "Deprecated").collect(toMap(s -> s, s -> IgnoredAttribute::new));
//        constructors.put("ConstantValue", ConstantValueAttribute::new);
//        constructors.put("Code", CodeAttribute::new);
//        constructors.put("Exceptions", ExceptionsAttribute::new);
//        constructors.put("Signature", SignatureAttribute::new);
//        constructors.put("InnerClasses", InnerClassesAttribute::new);
//        constructors.put("BootstrapMethods", BootStrapMethodsAttribute::new);
//        constructors.put("RuntimeVisibleAnnotations", AccessibleAnnotationsAttribute::new);
//        constructors.put("RuntimeInvisibleAnnotations", AccessibleAnnotationsAttribute::new);
//        constructors.put("AnnotationDefault", AnnotationDefaultAttribute::new);
//        constructors.put("Synthetic", SyntheticAttribute::new);
//        ATTRIBUTE_CONSTRUCTORS = donnoe.util.DefaultMap.unmodifiable(constructors.entrySet().stream().collect(toMap(Entry::getKey, e -> (c, s) -> e.getValue().apply(c))), UnrecognizedAttribute::new);
    }    
    
    public Attributable(ClassFile cF) {
        super(cF);
    }
    
    public Future<List<Attribute>> l;
    protected final void readAttributes() {
        l = readItemFutureList(this::readAttribute, readUnsignedShort());
    }
    
    private Attribute readAttribute() {
        String name = readString();
        return ATTRIBUTE_CONSTRUCTORS.get(name).apply(cF, name);
    }
}
