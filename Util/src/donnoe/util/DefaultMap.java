package donnoe.util;

import java.util.HashMap;
import java.util.Map;

public final class DefaultMap<K, V> extends LookupMap<K, V> {

    public DefaultMap(V v) {
        this(new HashMap<>(), v);
    }
    
    public DefaultMap(Map<K, V> map, V v) {
        super(map, k -> v);
    }
}
