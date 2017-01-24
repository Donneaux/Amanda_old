package donnoe.amanda.attributes.annotation;

import static donnoe.amanda.Amanda.INSTANCE;
import donnoe.amanda.ClassFile;
import static donnoe.amanda.attributes.annotation.AnnotationPrinter.print;
import static donnoe.amanda.attributes.annotation.AnnotationValue.readAnnotationValue;
import donnoe.util.concurrent.Futures;
import static donnoe.util.concurrent.Futures.transformMapWithKnownKeys;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author joshuadonnoe
 */
public final class Annotation extends AnnotationValue {

    protected final Future<String> type = Futures.getFromListFuture(readTypesFuture(), 0);

    protected final Future<Map<String, AnnotationValue>> pairs = transformMapWithKnownKeys(readObjects(() -> cF, toMap(ClassFile::readString, cf -> INSTANCE.queueForResolution(readAnnotationValue(cf)))));

    /**
     *
     * @param cF
     */
    public Annotation(final ClassFile cF) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(print(this));
    }
}
