package donnoe.amanda.attribute;

import donnoe.amanda.ClassFile;
import java.util.concurrent.ExecutionException;

public class ConstantValueAttribute extends RecognizedAttribute {

    /**
     *
     * @param cF
     */
    public ConstantValueAttribute(ClassFile cF) {
        super(cF);
        sb.append(readString());
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
    }
}
