package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import java.util.concurrent.ExecutionException;
import static donnoe.amanda.ClassFile.escapeString;
/**
 *
 * @author joshuadonnoe
 */
public class StringConstant extends UTFBasedConstant implements CompileTimeConstant {

    public StringConstant(ClassFile cF) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(escapeString(utf.get()));
    }
}
