package donnoe.util;

/**
 *
 * @author joshuadonnoe
 */
public class Functions {

    private Functions() {
    }

    public interface ExceptionalFunction<T, R, X extends Throwable> {

        R apply(T t) throws X;
    }
}
