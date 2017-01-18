package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author joshuadonnoe
 */
public class IgnoredAttribute extends Attribute {

    public IgnoredAttribute(ClassFile cF) {
        super(cF);
        skip(readInt());
    }

    @Override
    public final void resolve() throws ExecutionException, InterruptedException {
    }
}
