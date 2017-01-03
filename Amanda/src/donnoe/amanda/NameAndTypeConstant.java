package donnoe.amanda;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public class NameAndTypeConstant extends Constant {

    private final Future<UTFConstant> name;
    private final Future<UTFConstant> type;
    
    public NameAndTypeConstant(ClassFile cF) {
        name = cF.readConstantFuture();
        type = cF.readConstantFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(name.get()).append(' ').append(type.get());
    }
    
    
    
}
