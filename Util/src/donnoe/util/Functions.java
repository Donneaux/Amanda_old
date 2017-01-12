package donnoe.util;

/**
 *
 * @author joshuadonnoe
 */
public class Functions {

    private Functions() {
    }

    public interface ExceptionalFunction<T, R> {

        R apply(T t) throws Exception;
    }
}
