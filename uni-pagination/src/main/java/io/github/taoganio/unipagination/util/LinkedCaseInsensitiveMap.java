package io.github.taoganio.unipagination.util;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * 不分大小写映射
 */
public class LinkedCaseInsensitiveMap<V> extends AbstractMap<String, V> {
    private final Map<String, V> internalMap;
    private final Map<String, String> caseInsensitiveKeys;
    private final Locale locale;

    public LinkedCaseInsensitiveMap() {
        this(Locale.getDefault());
    }

    public LinkedCaseInsensitiveMap(int initialCapacity) {
        this(initialCapacity, Locale.getDefault());
    }

    public LinkedCaseInsensitiveMap(Locale locale) {
        this(16, locale);
    }

    public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale) {
        this.internalMap = new LinkedHashMap<>();
        this.caseInsensitiveKeys = new HashMap<>(initialCapacity);
        this.locale = locale;
    }


    private String toLowerCase(String key) {
        return key.toLowerCase(locale);
    }

    @Override
    public int size() {
        return internalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return caseInsensitiveKeys.containsKey(toLowerCase((String) key));
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return internalMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        if (key instanceof String) {
            String originalKey = caseInsensitiveKeys.get(toLowerCase((String) key));
            if (originalKey != null) {
                return internalMap.get(originalKey);
            }
        }
        return null;
    }

    @Override
    public V put(String key, V value) {
        String originalKey = caseInsensitiveKeys.put(toLowerCase(key), key);
        V oldKeyValue = null;
        if (originalKey != null && !originalKey.equals(key)) {
            oldKeyValue = internalMap.remove(originalKey);
        }
        V oldValue = internalMap.put(key, value);
        return oldKeyValue != null ? oldKeyValue : oldValue;
    }

    @Override
    public V remove(Object key) {
        if (key instanceof String) {
            String originalKey = caseInsensitiveKeys.remove(toLowerCase((String) key));
            if (originalKey != null) {
                return internalMap.remove(originalKey);
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        if (!m.isEmpty()) {
            m.forEach(this::put);
        }
    }

    @Override
    public void clear() {
        caseInsensitiveKeys.clear();
        internalMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(internalMap.keySet());
    }

    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(internalMap.values());
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return Collections.unmodifiableSet(internalMap.entrySet());
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super V> action) {
        internalMap.forEach(action);
    }

    @Override
    public V putIfAbsent(String key, V value) {
        V oldValue = get(key);
        if (oldValue == null) {
            return put(key, value);
        }
        return oldValue;
    }

    @Override
    public boolean remove(Object key, Object value) {
        if (containsKey(key) && Objects.equals(get(key), value)) {
            remove(key);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
