package donnoe.amanda.attributes.annotation;

import donnoe.amanda.ClassFile;
import donnoe.amanda.Blob;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 *
 * @author joshuadonnoe
 */
public final class Annotations extends Blob {

    private final Future<List<Annotation>> annotations = readItemFutureList(() -> new Annotation(cF), readUnsignedShort());
    
    /**
     *
     * @param cF
     */
    public Annotations(ClassFile cF) {
        super(cF);
    }
    
    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(annotations.get().stream().map(Annotation::toString).collect(Collectors.joining()));
    }    
}