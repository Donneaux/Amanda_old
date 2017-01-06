package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import donnoe.amanda.Constant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public class NameAndTypeConstant extends Constant {

    private final Future<String> name;
    private final Future<String> type;

    public NameAndTypeConstant(ClassFile cF) {
        name = cF.readStringFuture();
        type = cF.readStringFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(name.get()).append(' ').append(type.get());
    }
}