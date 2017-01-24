package donnoe.amanda.attribute;

import donnoe.amanda.ClassFile;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author joshuadonnoe
 */
public final class SyntheticAttribute extends RecognizedAttribute {

    /**
     *
     * @param classFile
     */
    public SyntheticAttribute(final ClassFile classFile) {
        super(classFile);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
    }

}
