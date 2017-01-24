package donnoe.amanda.attribute.annotation;

import donnoe.amanda.ClassFile;
import donnoe.amanda.ValuePrinter;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 *
 * @author joshuadonnoe
 */
public final class LiteralAnnotationValue extends AnnotationValue {

    public LiteralAnnotationValue(ClassFile cF, Function<String, String> f) {
        super(cF);
        sb.append(f.apply(readString()));
    }

    public LiteralAnnotationValue(ClassFile cF) {
        this(cF, Function.identity());
    }
    
    public static LiteralAnnotationValue readCharacterAnnotationValue(ClassFile cF) {
        return new LiteralAnnotationValue(cF, ValuePrinter.CHARACTER::visit);
    }
    
    public static LiteralAnnotationValue readBooleanAnnotationValue(ClassFile cF) {
        return new LiteralAnnotationValue(cF, ValuePrinter.BOOLEAN::visit);
    }
    
    public static LiteralAnnotationValue readStringAnnotationValue(ClassFile cF) {
        return new LiteralAnnotationValue(cF, ClassFile::escapeString);
    }
    
    @Override
    public final void resolve() throws ExecutionException, InterruptedException {
    }
    
}