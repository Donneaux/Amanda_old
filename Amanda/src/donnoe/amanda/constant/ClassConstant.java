package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import java.util.concurrent.ExecutionException;

/**
 * Much work needed
 *
 * @author joshuadonnoe
 */
public final class ClassConstant extends UTFBasedConstant {

    public ClassConstant(ClassFile cF, int index) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        //if starts with '[' then get type
        sb.append(utf.get().replace('/', '.')).append(".class");
    }
}
