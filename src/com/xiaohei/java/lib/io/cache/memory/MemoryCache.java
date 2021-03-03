package com.xiaohei.java.lib.io.cache.memory;

import java.util.Collection;

public interface MemoryCache<K,V> {


    /**
     * Puts value into cache by key
     *
     * @return <b>true</b> - if value was put into cache successfully, <b>false</b> - if value was <b>not</b> put into
     * cache
     */
    boolean put(K key, V value);

    /**
     * Returns value by key. If there is no value for key then null will be returned.
     */
    V get(K key);

    /**
     * Removes item by key
     */
    V remove(K key);

    /**
     * Returns all keys of cache
     */
    Collection<K> keys();

    /**
     * Remove all items from cache
     */
    void clear();
}
