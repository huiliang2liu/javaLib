package com.xiaohei.java.lib.io.cache.memory;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

public class SoftMemoryCache<K,V> extends BaseMemoryCache<K,V>{
    @Override
    protected Reference<V> createReference(V value) {
        return new SoftReference<>(value);
    }
}
