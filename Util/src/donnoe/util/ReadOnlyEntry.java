package donnoe.util;

import java.util.AbstractMap;
import java.util.Map.Entry;
/**
 *
 * @author joshuadonnoe
 * @param <K>
 * @param <V>
 */
public class ReadOnlyEntry<K, V> implements Entry<K, V> {

    private final K k;
    private final V v;

    /**
     *
     * @param k
     * @param v
     */
    public ReadOnlyEntry(K k, V v) {
        this.k = k;
        this.v = v;
    }
    
    public ReadOnlyEntry(Entry<K, V> e) {
        this(e.getKey(), e.getValue());
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }

    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException("Value is final");
    }

    @Override
    public String toString() {
        return k + " = " + v;
    }
}
