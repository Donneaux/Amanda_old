package donnoe.amanda.attribute.annotation;

import donnoe.amanda.ClassFile;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static donnoe.amanda.attribute.annotation.AnnotationValue.*;

/**
 *
 * @author joshuadonnoe
 */
public final class AnnotationValueArray extends AnnotationValue {

    public final Future<List<AnnotationValue>> values = readItemFutureList(() -> readAnnotationValue(cF));
    
    /**
     *
     * @param cF
     */
    public AnnotationValueArray(ClassFile cF) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(AnnotationValueArrayPrint.print(this));
    }
}