package donnoe.util;

import java.util.Map;

public final class DefaultMap<K, V> extends LookupMap<K, V> {

    public DefaultMap(Map<K, V> map, V v) {
        super(map, k -> v);
    }
}
