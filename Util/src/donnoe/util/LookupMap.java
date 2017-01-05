package donnoe.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import static java.util.Collections.unmodifiableMap;

/**
 * looks for the key in the map and if the key is not there, computes the value
 * from the function
 *
 * @author joshuadonnoe
 * @param <K>
 * @param <V>
 */
public class LookupMap<K, V> extends ForwardingMap<K, V> {

    protected Function<K, V> function;

    public LookupMap(Function<K, V> function) {
        this(new HashMap<>(), function);
    }

    public LookupMap(Map<K, V> delegate, Function<K, V> function) {
        super(delegate);
        this.function = function;
    }

    @Override
    public final V get(Object o) {
        @SuppressWarnings("unchecked")
        K k = (K) o;
        return containsKey(k) ? delegate.get(o) : delegate.computeIfAbsent(k, function);
    }
}
