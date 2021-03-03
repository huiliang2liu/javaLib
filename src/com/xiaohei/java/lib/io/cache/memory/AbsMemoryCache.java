package com.xiaohei.java.lib.io.cache.memory;


public abstract class AbsMemoryCache<K, V> implements MemoryCache<K, V> {
    protected CalculateSize<V> calculateSize;
    protected long maxCacheSize;
    protected long cacheSize;

    public AbsMemoryCache(long maxCacheSize,CalculateSize<V> calculateSize) {
        this.maxCacheSize = maxCacheSize;
        this.calculateSize = calculateSize;
    }


    @Override
    public final boolean put(K key, V value) {
        if (calculateSize == null)
            throw new NullPointerException("calculateSize is null");
        if (putValue(key, value)) {
            cacheSize += calculateSize.sizeof(value);
            trimToSize();
            return true;
        }
        return false;
    }

    protected abstract void trimToSize();

    protected abstract boolean putValue(K key, V value);

    @Override
    public V remove(K key) {

        return null;
    }

    @Override
    public void clear() {
        cacheSize = 0;
    }
}
