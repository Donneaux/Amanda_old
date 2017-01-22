package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class ExceptionsAttribute extends RecognizedAttribute {
    private final Future<List<String>> EXCEPTIONS;
    
    /**
     *
     * @param cF
     */
    public ExceptionsAttribute(ClassFile cF) {
        super(cF);
        EXCEPTIONS = cF.readShortStringsListFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(EXCEPTIONS.get());
    }
}
