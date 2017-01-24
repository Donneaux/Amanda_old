package donnoe.amanda.attribute;

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

    private final Future<String> realName = transform(readShortStringFuture(), readStringFuture(), (o, i) -> o + '.' + i);

    public final int access = readUnsignedShort();

    /**
     *
     * @param cF
     */
    public InnerClassInfo(ClassFile cF) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(realName.get());
    }

}
