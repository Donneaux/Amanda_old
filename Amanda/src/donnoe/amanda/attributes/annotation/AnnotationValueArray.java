package donnoe.amanda.attributes.annotation;

import donnoe.amanda.ClassFile;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
//import static donnoe.amanda.attributes.annotations.AnnotationValueArrayVisitor.select;

/**
 *
 * @author joshuadonnoe
 */
public final class AnnotationValueArray extends AnnotationValue {

    final Future<List<AnnotationValue>> values;
    
    /**
     *
     * @param cF
     */
    public AnnotationValueArray(ClassFile cF) {
        super(cF);
        values = readItemFutureList(() -> AnnotationValue.readAnnotationValue(cF), readUnsignedShort());
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(AnnotationValueArrayPrint.print(this));
    }
}