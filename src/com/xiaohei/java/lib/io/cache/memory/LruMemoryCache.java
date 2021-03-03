package com.xiaohei.java.lib.io.cache.memory;

import java.util.*;

public class LruMemoryCache<K, V> extends AbsMemoryCache<K, V> {
    private final LinkedHashMap<K, V> cacheMap = new LinkedHashMap<>(0, .75f, true);

    public LruMemoryCache(long maxCacheSize,CalculateSize<V> calculateSize) {
        super(maxCacheSize,calculateSize);
    }

    @Override
    protected void trimToSize() {
        while (true) {
            synchronized (this) {
                if (cacheSize < 0 || cacheMap.isEmpty() || cacheSize <= maxCacheSize)
                    return;
                Map.Entry<K, V> entry = cacheMap.entrySet().iterator().next();
                if (entry == null)
                    return;
                remove(entry.getKey());
            }
        }
    }

    @Override
    protected boolean putValue(K key, V value) {
        if (key == null || value == null)
            return false;
        synchronized (this) {
            cacheMap.put(key, value);
            return true;
        }
    }

    @Override
    public V get(K key) {
        if (key == null)
            return null;
        synchronized (this) {
            return cacheMap.get(key);
        }

    }

    @Override
    public V remove(K key) {
        if (key == null)
            return null;
        if (calculateSize == null)
            throw new NullPointerException("calculateSize is null");
        synchronized (this) {
            V v = cacheMap.remove(key);
            if (v == null)
                return null;
            cacheSize -= calculateSize.sizeof(v);
            return v;
        }
    }

    @Override
    public synchronized Collection<K> keys() {
        return new HashSet(cacheMap.keySet());
    }

    @Override
    public void clear() {
        super.clear();
        cacheMap.clear();
    }
}
