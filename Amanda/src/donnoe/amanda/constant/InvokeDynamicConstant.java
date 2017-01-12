package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public final class InvokeDynamicConstant extends Constant {

    private final Future<NameAndTypeConstant> nAt;
    
    public InvokeDynamicConstant(ClassFile cF) {
        super(cF);
        cF.skip(2);
        nAt = cF.readConstantFuture();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append('(').append(nAt.get().types.get().get(0)).append(") ");
//        sb.append(method.get().handle.get().method.get()).append("(\n");
        sb.append("java.lang.invoke.MethodHandles.lookup(),\n");
        sb.append(nAt.get().name.get()).append(",\n");
        sb.append(MethodTypeConstant.asLiteral(nAt.get().types.get())).append(",\n");
//        sb.append(method.get().args.get().get(0)).append(",\n");
//        sb.append(method.get().args.get().get(1)).append(",\n");
//        sb.append(method.get().args.get().get(2)).append("\n)");
        sb.append(".dynamicInvoker().invoke(");    }
}
