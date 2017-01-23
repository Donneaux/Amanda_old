package donnoe.amanda.attributes.annotation;

import donnoe.amanda.ClassFile;
import donnoe.util.concurrent.Futures;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class EnumAnnotationValue extends AnnotationValue {

    private final Future<String> clazz;
    private final String element;
    
    /**
     *
     * @param cF
     */
    public EnumAnnotationValue(ClassFile cF) {
        super(cF);
        clazz = Futures.getFromListFuture(readTypesFuture(), 0);
        element = readString();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(clazz.get()).append('.').append(element);
    }
}