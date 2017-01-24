package donnoe.amanda.attributes;

import static donnoe.amanda.Amanda.INSTANCE;
import donnoe.amanda.ClassFile;
import donnoe.util.concurrent.Futures;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static java.util.stream.Collectors.*;

/**
 *
 * @author joshuadonnoe
 */
public class InnerClassesAttribute extends RecognizedAttribute {

    public final Map<Integer, Future<InnerClassInfo>> innerClasses = readObjects(
            () -> cF,
            toMap(
                    ClassFile::readUnsignedShort,//this is the index of the ClassConstant that is a InnerClass
                    cf -> INSTANCE.queueForResolution(new InnerClassInfo(cf))
            )
    );

    public InnerClassesAttribute(ClassFile cF) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(Futures.transformMapWithKnownKeys(innerClasses).get());
    }

}
