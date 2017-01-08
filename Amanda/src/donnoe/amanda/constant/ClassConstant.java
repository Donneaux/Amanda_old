package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import donnoe.util.DefaultMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import static java.util.Collections.*;
import java.util.HashMap;

/**
 * Much work needed
 *
 * @author joshuadonnoe
 */
public final class ClassConstant extends UTFBasedConstant {

    ClassFile cF;
    
    public ClassConstant(ClassFile cF, int index) {
        super(cF);
        this.cF = cF;
    }

    private static final Map<Character, BiFunction<ClassFile, Queue<Character>, String>> TYPE_GETTERS =
            unmodifiableMap(
                    new DefaultMap<>(
                            new HashMap<Character, BiFunction<ClassFile, Queue<Character>, String>>() {{
                                put('[', ClassFile::getType);
                            }},
                            ClassFile::getClassType
                    )
            );
    
    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        Queue<Character> q = ClassFile.toQueue(utf.get());
        sb.append(TYPE_GETTERS.get(q.peek()).apply(cF, q)).append(".class");
    }
}
