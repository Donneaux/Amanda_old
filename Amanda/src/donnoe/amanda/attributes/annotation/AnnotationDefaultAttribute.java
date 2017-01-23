package donnoe.amanda.attributes.annotation;

import static donnoe.amanda.Amanda.INSTANCE;
import donnoe.amanda.ClassFile;
import donnoe.amanda.attributes.RecognizedAttribute;
import static donnoe.amanda.attributes.annotation.AnnotationValue.readAnnotationValue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class AnnotationDefaultAttribute extends RecognizedAttribute {

    private final Future<AnnotationValue> value;
    
    /**
     *
     * @param cF
     */
    public AnnotationDefaultAttribute(final ClassFile cF) {
        super(cF);
        value = INSTANCE.queueForResolution(readAnnotationValue(cF));
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(" default ").append(value.get());
    }
}