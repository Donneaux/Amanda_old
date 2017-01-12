package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author joshuadonnoe
 */
public final class MethodTypeConstant extends Constant {

    private final Future<List<String>> typesFuture;
    
    public MethodTypeConstant(ClassFile cF) {
        super(cF);
        typesFuture = cF.readTypesFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(asLiteral(typesFuture.get()));
    }
    
    public static String asLiteral(List<String> types) {
        return types.stream().map(s -> s + ".class").collect(joining(", ", "java.lang.invoke.MethodType.methodType(", ")"));
    }
}