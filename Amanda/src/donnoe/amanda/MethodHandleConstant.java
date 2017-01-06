package donnoe.amanda;

import java.util.Map;
import static java.util.stream.Stream.*;
import static java.util.stream.Collectors.*;
import static java.util.function.Function.identity;
import static java.util.Collections.unmodifiableMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author joshuadonnoe
 */
public final class MethodHandleConstant extends Constant {

    private static final Map<Integer, String> KINDS;
    
    static {
        Map<Integer, String> kinds = of(5, 9).collect(toMap(identity(), i -> "Virtual"));
        kinds.put(1, "Getter");
        kinds.put(2, "StaticGetter");
        kinds.put(3, "Setter");
        kinds.put(4, "StaticSetter");
        kinds.put(6, "Static");
        kinds.put(7, "Special");
        kinds.put(8, "Constructor");
        KINDS = unmodifiableMap(kinds);
    }
    
    private final Future<String> method;
    
    public MethodHandleConstant(ClassFile cF) {
        sb.append("java.lang.invoke.MethodHandles.lookup().find").append(KINDS.get(cF.readUnsignedByte())).append("(\n");
        method = cF.readStringFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append(method.get());
    }
}
