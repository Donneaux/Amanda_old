package donnoe.amanda;

import donnoe.amanda.attribute.annotation.AnnotationDefaultAttribute;
import donnoe.amanda.attribute.ExceptionsAttribute;
import donnoe.amanda.attribute.InnerClassesAttribute;
import donnoe.amanda.attribute.SyntheticAttribute;
import donnoe.amanda.attribute.IgnoredAttribute;
import donnoe.amanda.attribute.annotation.AccessibleAnnotationsAttribute;
import donnoe.amanda.attribute.SignatureAttribute;
import donnoe.amanda.attribute.BootStrapMethodsAttribute;
import donnoe.amanda.attribute.UnrecognizedAttribute;
import donnoe.amanda.attribute.ConstantValueAttribute;
import donnoe.amanda.attribute.Attribute;
import static donnoe.amanda.Amanda.INSTANCE;
import donnoe.util.DefaultMap;
import donnoe.util.TypeSafeHeterogenousContainer;
import donnoe.util.concurrent.Futures;
import static donnoe.util.concurrent.Futures.*;
import java.util.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.function.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;
import static java.util.function.Function.identity;
/**
 *
 * @author joshuadonnoe
 */
public abstract class Attributable extends Blob {

    private static final Map<String, BiFunction<ClassFile, String, Attribute>> ATTRIBUTE_CONSTRUCTORS = unmodifiableMap(
            new DefaultMap<String, BiFunction<ClassFile, String, Attribute>>(
                    new HashMap<String, Function<ClassFile, Attribute>>() {
                        {
                            putAll(of("SourceFile", "Deprecated").collect(toMap(identity(), s -> IgnoredAttribute::new)));
                            put("InnerClasses", InnerClassesAttribute::new);
                            put("Exceptions", ExceptionsAttribute::new);
                            put("BootstrapMethods", BootStrapMethodsAttribute::new);
                            put("ConstantValue", ConstantValueAttribute::new);
                            put("Signature", SignatureAttribute::new);
                            put("Synthetic", SyntheticAttribute::new);
                            put("RuntimeVisibleAnnotations", AccessibleAnnotationsAttribute::new);
                            put("AnnotationDefault", AnnotationDefaultAttribute::new);
                        }
                    }.entrySet().stream().collect(toMap(
                            Map.Entry::getKey,
                            e -> (cF, s) -> e.getValue().apply(cF)
                    )),
                    UnrecognizedAttribute::new
            )
    );

    public final BlockingQueue<Future<TypeSafeHeterogenousContainer<Attribute>>> q = new ArrayBlockingQueue<>(1);
    public final Future<TypeSafeHeterogenousContainer<Attribute>> attributes = INSTANCE.exec.submit(() -> q.take().get());

    public Attributable(ClassFile cF) {
        super(cF);
    }

    protected final void readAttributes() {
        q.add(transform(
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
                                        e -> transformList(e.getValue())
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
        return transform(attributes, tshc -> tshc.get(clazz));
    }

    public final <A extends Attribute> Future<A> getAttributeFuture(Class<A> clazz) {
        return transform(getAttributeFutures(clazz), l -> l.isEmpty() ? null : l.get(0));
    }
}
