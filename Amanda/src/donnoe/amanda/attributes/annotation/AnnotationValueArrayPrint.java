package donnoe.amanda.attributes.annotation;

import donnoe.util.DefaultMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author joshuadonnoe
 */
public enum AnnotationValueArrayPrint {

    /**
     *
     */
    EMPTY {

        @Override
        public String visit(List<AnnotationValue> l) throws ExecutionException, InterruptedException {
            return "{}";
        }

    },
    SINGLE {

        @Override
        public String visit(List<AnnotationValue> l) throws ExecutionException, InterruptedException {
            return l.get(0).toString();
        }

    },
    GENERAL {
        @Override
        public String visit(List<AnnotationValue> l) throws ExecutionException, InterruptedException {
            return l.stream().map(Object::toString).collect(joining(", ", "{", "}"));
        }
    };

    private static final Map<Integer, AnnotationValueArrayPrint> VISITORS = new DefaultMap<>(
            new HashMap<Integer, AnnotationValueArrayPrint>() {
        {
            put(0, EMPTY);
            put(1, SINGLE);
        }
    },
            GENERAL
    );

    public static String print(AnnotationValueArray a) throws ExecutionException, InterruptedException {
        List<AnnotationValue> values = a.values.get();
        return VISITORS.get(values.size()).visit(values);
    }
    /**
     *
     * @param l
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public abstract String visit(List<AnnotationValue> l) throws ExecutionException, InterruptedException;
}
