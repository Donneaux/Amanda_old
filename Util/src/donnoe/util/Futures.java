package donnoe.util;

import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public class Futures {
    private Futures() {}
    
    public static <V> V getNow(Future<V> f) {
        return FutureStatus.getNow(f);
    }
    
    public static <V> Future<V> cast(Future<?> f) {
        @SuppressWarnings("unchecked") Future<V> rv = 
        (Future<V>) f;
        return rv;
    }
}
