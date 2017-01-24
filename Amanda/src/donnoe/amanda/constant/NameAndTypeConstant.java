package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public class NameAndTypeConstant extends Constant {

    public final Future<String> name = cF.readStringFuture();
    
    public final Future<List<String>> types = cF.readTypesFuture();

    public NameAndTypeConstant(ClassFile cF) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(name.get()).append(' ').append(types.get());
    }
}
