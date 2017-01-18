package donnoe.amanda.attributes;

import donnoe.amanda.Blob;
import donnoe.amanda.ClassFile;
import static donnoe.util.concurrent.Futures.transform;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class InnerClassInfo extends Blob {

    /**
     *
     */
    public final int access;
    private final Future<String> realName;
    
    /**
     *
     * @param cF
     */
    public InnerClassInfo(ClassFile cF) {
        super(cF);
        realName = transform(readShortStringFuture(), readStringFuture(), (o, i) -> o + '.' + i);
        access = readUnsignedShort();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(realName.get());
    }
    
}

