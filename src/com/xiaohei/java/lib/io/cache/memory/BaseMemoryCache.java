package com.xiaohei.java.lib.io.cache.memory;

import java.lang.ref.Reference;
import java.util.*;

public abstract class BaseMemoryCache<K, V> implements MemoryCache<K, V> {
    private static final int MAX_CACHE_SIZE = 500;
    private final Map<K, Reference<V>> softMap = new LinkedHashMap<>();


    @Override
    public synchronized V get(K key) {
        V result = null;
        Reference<V> reference = softMap.get(key);
        if (reference != null) {
            result = reference.get();
        }
        if (result == null)
            softMap.remove(key);
        return result;
    }

    @Override
    public synchronized boolean put(K key, V value) {
        softMap.put(key, createReference(value));
        if (softMap.size() > MAX_CACHE_SIZE) {
            Map.Entry<K, Reference<V>> entry = softMap.entrySet().iterator().next();
            if (entry != null)
                softMap.remove(entry.getKey());
        }
        return true;
    }

    @Override
    public synchronized V remove(K key) {
        Reference<V> bmpRef = softMap.remove(key);
        return bmpRef == null ? null : bmpRef.get();
    }

    @Override
    public synchronized Collection<K> keys() {
        return new HashSet<>(softMap.keySet());
    }

    @Override
    public void clear() {
        softMap.clear();
    }


    protected abstract Reference<V> createReference(V value);
}
