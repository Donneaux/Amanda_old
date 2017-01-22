package donnoe.amanda;

import donnoe.amanda.attributes.UnrecognizedAttribute;
import donnoe.amanda.attributes.IgnoredAttribute;
import donnoe.amanda.attributes.Attribute;
import donnoe.amanda.attributes.InnerClassesAttribute;
import donnoe.util.DefaultMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import static java.util.stream.Stream.*;
import static java.util.stream.Collectors.*;
import static java.util.Collections.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import static donnoe.amanda.Amanda.INSTANCE;
import donnoe.amanda.attributes.BootStrapMethodsAttribute;
import donnoe.amanda.attributes.ConstantValueAttribute;
import donnoe.amanda.attributes.ExceptionsAttribute;
import donnoe.amanda.attributes.SignatureAttribute;
import donnoe.amanda.attributes.SyntheticAttribute;
import donnoe.util.TypeSafeHeterogenousContainer;
import donnoe.util.concurrent.Futures;
import java.util.Arrays;
import static java.util.Arrays.asList;
import static java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author joshuadonnoe
 */
public abstract class Attributable extends Blob {

    private static final Map<String, BiFunction<ClassFile, String, Attribute>> ATTRIBUTE_CONSTRUCTORS = unmodifiableMap(
            new DefaultMap<String, BiFunction<ClassFile, String, Attribute>>(
                    new HashMap<String, Function<ClassFile, Attribute>>() {
                        {
                            putAll(of("SourceFile").collect(toMap(s -> s, s -> IgnoredAttribute::new)));
                            put("InnerClasses", InnerClassesAttribute::new);
                            put("Exceptions", ExceptionsAttribute::new);
                            put("BootstrapMethods", BootStrapMethodsAttribute::new);
                            put("ConstantValue", ConstantValueAttribute::new);
                            put("Signature", SignatureAttribute::new);
                            put("Synthetic", SyntheticAttribute::new);
                        }
                    }.entrySet().stream().collect(toMap(
                            Map.Entry::getKey,
                            e -> (cF, s) -> e.getValue().apply(cF)
                    )),
                    UnrecognizedAttribute::new
            )
    );

//    static {
//        final Map<String, Function<ClassFile, Attribute>> constructors = of("SourceFile", "LineNumberTable", "LocalVariableTable", "LocalVariableTypeTable", "org.netbeans.SourceLevelAnnotations", "Deprecated").collect(toMap(s -> s, s -> IgnoredAttribute::new));
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
//    }
    public Attributable(ClassFile cF) {
        super(cF);
    }

    public final BlockingQueue<Future<TypeSafeHeterogenousContainer<Attribute>>> q = new ArrayBlockingQueue<>(1);
    public final Future<TypeSafeHeterogenousContainer<Attribute>> attributes = Amanda.INSTANCE.exec.submit(() -> q.take().get());

    protected final void readAttributes() {
        q.add(Futures.transform(
                readObjects(
                        this::readAttribute,
                        toMap(
                                Attribute::getClass,
                                a -> asList(INSTANCE.queueForResolution(a)),
                                (l1, l2) -> concat(l1.stream(), l2.stream()).collect(toList())
                        )
                ).entrySet().stream().collect(
                        collectingAndThen(
                                toMap(
                                        Entry::getKey,
                                        e -> Futures.transformList(e.getValue())
                                ),
                                Futures::transformMapWithKnownKeys
                        )),
                TypeSafeHeterogenousContainer<Attribute>::new
        ));
    }

    private Attribute readAttribute() {
        String name = readString();
        return ATTRIBUTE_CONSTRUCTORS.get(name).apply(cF, name);
    }

    public final <A extends Attribute> Future<List<A>> getAttributeFutures(Class<A> clazz) {
        return Futures.transform(attributes, tshc -> tshc.get(clazz));
    }

    public final <A extends Attribute> Future<A> getAttributeFuture(Class<A> clazz) {
        return Futures.transform(getAttributeFutures(clazz), l -> l.isEmpty() ? null : l.get(0));
    }
}
