package donnoe.amanda.attribute.annotation;

import donnoe.amanda.ClassFile;
import donnoe.util.concurrent.Futures;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class EnumAnnotationValue extends AnnotationValue {

    private final Future<String> clazz = Futures.getFromListFuture(readTypesFuture(), 0);
    private final String element = readString();
    
    /**
     *
     * @param cF
     */
    public EnumAnnotationValue(ClassFile cF) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(clazz.get()).append('.').append(element);
    }
}