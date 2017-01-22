package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import donnoe.amanda.attributes.BootStrapMethod;
import donnoe.util.concurrent.Futures;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author joshuadonnoe
 */
public final class InvokeDynamicConstant extends Constant {

    private final Future<NameAndTypeConstant> nAt;
    private final Future<BootStrapMethod> method;
    
    public InvokeDynamicConstant(ClassFile cF) {
        super(cF);
        
        method = Futures.getFromListFuture(cF.methods, readUnsignedShort());
        nAt = cF.readConstantFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append('(').append(nAt.get().types.get().get(0)).append(") ");//cast
        sb.append(method.get().handle.get().method.get()).append("(\n");
        sb.append("java.lang.invoke.MethodHandles.lookup(),\n");
        sb.append('"').append(nAt.get().name.get()).append("\",\n");//name of mnterface method
        sb.append(MethodTypeConstant.asLiteral(nAt.get().types.get())).append(",\n");
        sb.append(method.get().args.get().get(0)).append(",\n");
        sb.append(method.get().args.get().get(1)).append(",\n");
        sb.append(method.get().args.get().get(2)).append("\n)");
        sb.append(".dynamicInvoker().invoke(");
    }
}
