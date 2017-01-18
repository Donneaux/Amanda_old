package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;
import static java.util.stream.Collectors.*;
import static donnoe.amanda.Amanda.INSTANCE;
import donnoe.util.concurrent.Futures;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public class InnerClassesAttribute extends RecognizedAttribute {

    public final Map<Integer, Future<InnerClassInfo>> innerClasses;
    
    public InnerClassesAttribute(ClassFile cF) {
        super(cF);
        innerClasses = readObjects(
            () -> cF,
            toMap(
                    ClassFile::readUnsignedShort,//this is the index of the ClassConstant that is a InnerClass
                    cf -> INSTANCE.queueForResolution(new InnerClassInfo(cf))
            )
        );
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(Futures.transformMapWithKnownKeys(innerClasses).get());
    }
    
}
