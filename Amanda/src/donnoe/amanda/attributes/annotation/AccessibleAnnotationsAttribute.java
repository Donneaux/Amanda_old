package donnoe.amanda.attributes.annotation;

import static donnoe.amanda.Amanda.INSTANCE;
import donnoe.amanda.ClassFile;
import donnoe.amanda.attributes.RecognizedAttribute;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class AccessibleAnnotationsAttribute extends RecognizedAttribute {

    private final Future<Annotations> annotations;

    /**
     *
     * @param cF
     */
    public AccessibleAnnotationsAttribute(ClassFile cF) {
        super(cF);
        annotations = INSTANCE.queueForResolution(new Annotations(cF));
    }

    /**
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(annotations.get());
    }
}