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

    public final Future<String> name;
    public final Future<List<String>> types;

    public NameAndTypeConstant(ClassFile cF) {
        super(cF);
        name = cF.readStringFuture();
        types = cF.readTypesFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(name.get()).append(' ').append(types.get());
    }
}
