package donnoe.amanda;

import java.util.concurrent.ExecutionException;

/**
 * Much work needed
 * @author joshuadonnoe
 */
public final class ClassConstant extends UTFBasedConstant {

    public ClassConstant(ClassFile cF, int index) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(utf.get().toString().replace('/', '.')).append(".class");
    }
    
    
}
