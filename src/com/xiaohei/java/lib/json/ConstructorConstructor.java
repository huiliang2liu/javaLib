package com.xiaohei.java.lib.json;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;

public class ConstructorConstructor {
    private static UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

    public static <T> T newInstance(Class<T> c){
        T t = null;
        t = newInstanceConstructor(c);
        if (t == null)
            t = newInstanceImplementationConstructor(c);
        if (t == null)
            try {
                t = unsafeAllocator.newInstance(c);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        return t;
    }

    private static <T> T newInstanceConstructor(Class<T> c) {
        try {
            Constructor<? super T> constructor = c.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return (T) constructor.newInstance();
        } catch (Exception e) {
        }
        return null;
    }

    private static <T> T newInstanceImplementationConstructor(Class<T> rawType) {
        if (Collection.class.isAssignableFrom(rawType)) {
            if (SortedSet.class.isAssignableFrom(rawType))
                return (T) new TreeSet<>();
            if (CopyOnWriteArraySet.class.isAssignableFrom(rawType))
                return (T) new CopyOnWriteArraySet();
            if (Set.class.isAssignableFrom(rawType))
                return (T) new LinkedHashSet<>();
            if (Queue.class.isAssignableFrom(rawType))
                return (T) new LinkedList<>();
            if (CopyOnWriteArrayList.class.isAssignableFrom(rawType))
                return (T) new CopyOnWriteArrayList<>();
            return (T) new ArrayList<>();
        }

        if (Map.class.isAssignableFrom(rawType)) {
            if (ConcurrentNavigableMap.class.isAssignableFrom(rawType))
                return (T) new ConcurrentSkipListMap<>();
            if (ConcurrentMap.class.isAssignableFrom(rawType))
                return (T) new ConcurrentHashMap<>();
            if (SortedMap.class.isAssignableFrom(rawType))
                return (T) new TreeMap<>();
            return (T) new HashMap<>();
        }
        return null;
    }
}
