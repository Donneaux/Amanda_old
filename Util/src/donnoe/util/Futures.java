package donnoe.util;

import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public class Futures {
    private Futures() {}
    
    public static <T> T getNow(Future<T> f) {
        return FutureStatus.getNow(f);
    }
}
