package donnoe.amanda.constant;

import donnoe.amanda.ClassFile;
import donnoe.amanda.attribute.InnerClassInfo;
import donnoe.util.DefaultMap;
import donnoe.util.concurrent.Futures;
import static java.util.Collections.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiFunction;

/**
 *
 * @author joshuadonnoe
 */
public final class ClassConstant extends UTFBasedConstant {
    private static final Map<Character, BiFunction<ClassFile, Queue<Character>, String>> TYPE_GETTERS
            = unmodifiableMap(
                    new DefaultMap<>(
                            new HashMap<Character, BiFunction<ClassFile, Queue<Character>, String>>() {
                                {
                                    put('[', ClassFile::getType);
                                }
                            },
                            ClassFile::getClassTypeHelper
                    )
            );

    private final Future<InnerClassInfo> innerClassInfo;

    public String oldName, newName;

    public ClassConstant(ClassFile cF, int index) {
        super(cF);
        innerClassInfo = Futures.unwrap(Futures.getOrDefaultFromMapFuture(cF.innerClasses, index, Futures.of(null)));
    }


    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        Queue<Character> q = ClassFile.toQueue(utf.get());
        sb.append(TYPE_GETTERS.get(q.peek()).apply(cF, q));
        if (sb.indexOf("$") > -1) {
            oldName = sb.toString();
            newName = innerClassInfo.get().toString();
            sb.setLength(0);
            sb.append(newName);
        }
        sb.append(".class");
    }
}
