package donnoe.amanda.attributes.annotation;

import donnoe.util.DefaultMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;


public enum AnnotationPrinter {

    /**
     *
     */
    EMPTY {
        @Override
        protected String print_(Map<String, AnnotationValue> m) throws ExecutionException, InterruptedException {
            return "";
        }
        
    },

    /**
     *
     */
    UNNAMED_SINGLE {

        @Override
        public String print_(Map<String, AnnotationValue> m) throws ExecutionException, InterruptedException {
            return '(' + m.get("value").toString() + ')';
        }
        
    },

    /**
     *
     */
    NAMED_SINGLE {

        @Override
        public String print_(Map<String, AnnotationValue> m) throws ExecutionException, InterruptedException {
            return '(' + m.entrySet().stream().map(Map.Entry::toString).findAny().get() + ')';
        }
        
    },

    /**
     *
     */
    GENERAL {

        @Override
        public String print_(Map<String, AnnotationValue> m) throws ExecutionException, InterruptedException {
            return m.entrySet().stream().map(Map.Entry::toString).collect(Collectors.joining(", ", "(", ")"));
        }
        
    };

    /**
     *
     * @param m
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected abstract String print_(Map<String, AnnotationValue> m) throws ExecutionException, InterruptedException;

    private static final Map<Integer, Function<Optional<String>, AnnotationPrinter>> VISITORS = new DefaultMap<>(
            new HashMap<Integer, Function<Optional<String>, AnnotationPrinter>>() {{
                put(0, o -> EMPTY);
                put(1, o-> o.get().equals("value") ? UNNAMED_SINGLE : NAMED_SINGLE);
            }},
            o -> GENERAL
    );
    
    /**
     *
     * @param a
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String print(Annotation a) throws ExecutionException, InterruptedException {
        return '@' +
                a.type.get() +
                VISITORS.get(a.pairs.get().size()).apply(a.pairs.get().keySet().stream().findAny()).print_(a.pairs.get());
    }
}