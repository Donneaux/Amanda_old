package donnoe.util;

import static java.util.Collections.unmodifiableMap;
import java.util.Map;

public final class DefaultMap<K, V> extends LookupMap<K, V> {

    public DefaultMap(Map<K, V> map, V v) {
        super(map, k -> v);
    }

    public static <K, V> Map<K, V> unmodifiable(Map<K, V> delegate, V v) {
        return unmodifiableMap(new DefaultMap<>(delegate, v));
    }
}
