package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import java.util.Map;
import static java.util.stream.Stream.*;
import static java.util.stream.Collectors.*;
import static java.util.function.Function.identity;
import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author joshuadonnoe
 */
public final class MethodHandleConstant extends Constant {

    private static final Map<Integer, String> KINDS = unmodifiableMap(new HashMap<Integer, String>(){
        {
            putAll(of(5, 9).collect(toMap(identity(), i -> "Virtual")));
            put(1, "Getter");
            put(2, "StaticGetter");
            put(3, "Setter");
            put(4, "StaticSetter");
            put(6, "Static");
            put(7, "Special");
            put(8, "Constructor");
        }
    });
    
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
