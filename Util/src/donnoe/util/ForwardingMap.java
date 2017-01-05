package donnoe.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author joshuadonnoe
 * @param <K>
 * @param <V>
 */
abstract public class ForwardingMap<K, V> implements Map<K, V> {

    protected final Map<K, V> delegate;

    public ForwardingMap(Map<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegate.entrySet();
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

    @Override
    public Set<K> keySet() {
        return delegate.keySet();
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        delegate.putAll(m);
    }

    @Override
    public V remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public V put(K key, V value) {
        return delegate.put(key, value);
    }

    @Override
    public V get(Object key) {
        return delegate.get(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public final int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return delegate.equals(obj);
    }

}
