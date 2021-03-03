package com.xiaohei.java.lib.io.cache.memory;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class WeakMemoryCache<K,V> extends BaseMemoryCache<K,V>{
    @Override
    protected Reference<V> createReference(V value) {
        return new WeakReference<>(value);
    }
}
