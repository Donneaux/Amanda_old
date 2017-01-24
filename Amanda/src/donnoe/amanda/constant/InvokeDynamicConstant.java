package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import donnoe.amanda.attributes.BootStrapMethod;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static donnoe.util.concurrent.Futures.getFromListFuture;
import static donnoe.amanda.constant.MethodTypeConstant.asLiteral;

/**
 *
 * @author joshuadonnoe
 */
public final class InvokeDynamicConstant extends Constant {

    private final Future<BootStrapMethod> method = getFromListFuture(cF.methods, readUnsignedShort());
    private final Future<NameAndTypeConstant> nAt = cF.readConstantFuture();

    public InvokeDynamicConstant(ClassFile cF) {
        super(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        sb.append('(').append(nAt.get().types.get().get(0)).append(") ").//cast
                append(method.get().handle.get().method.get()).append("(\n").
                append("java.lang.invoke.MethodHandles.lookup(),\n").
                append('"').append(nAt.get().name.get()).append("\",\n").//name of mnterface method
                append(asLiteral(nAt.get().types.get())).append(",\n").
                append(method.get().args.get().get(0)).append(",\n").
                append(method.get().args.get().get(1)).append(",\n").
                append(method.get().args.get().get(2)).append("\n)").
                append(".dynamicInvoker().invoke(");
    }
}
