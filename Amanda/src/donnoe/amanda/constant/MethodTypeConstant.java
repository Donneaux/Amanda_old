package donnoe.amanda;

import java.util.List;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author joshuadonnoe
 */
public final class MethodTypeConstant extends Constant {

    public MethodTypeConstant(ClassFile cF) {
        cF.skip(2);//this thing needs to take a utf and parse to types
    }

    @Override
    public void resolve() {
        sb.append("method type");
    }
    
    public static String asLiteral(List<String> types) {
        return types.stream().map(s -> s + ".class").collect(joining(", ", "java.lang.invoke.MethodType.methodType(", ")"));
    }
}