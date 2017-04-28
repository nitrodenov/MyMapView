package com.mymapview.presentation.model.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryCache<K, V> implements Cache<K, V> {

    private final LinkedHashMap<K, V> map;

    private int maxSize;
    private int size;

    public InMemoryCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }

        this.maxSize = maxSize;
        map = new LinkedHashMap<>(0, 0.75f, true);
    }

    @Override
    public V get(K key) {
        V mapValue;
        synchronized (this) {
            mapValue = map.get(key);
            return mapValue;
        }
    }

    @Override
    public V put(K key, V value) {
        V previous;
        synchronized (this) {
            size += sizeOf(key, value);
            previous = map.put(key, value);
            if (previous != null) {
                size -= sizeOf(key, previous);
            }
        }

        trimToSize(maxSize);
        return previous;
    }

    @Override
    public V remove(K key) {
        V previous;
        synchronized (this) {
            previous = map.remove(key);
            if (previous != null) {
                size -= sizeOf(key, previous);
            }
        }
        return previous;
    }

    protected int sizeOf(K key, V value) {
        return 1;
    }

    private void trimToSize(int maxSize) {
        while (true) {
            K key;
            V value;
            synchronized (this) {
                if (size <= maxSize || map.isEmpty()) {
                    break;
                }

                Map.Entry<K, V> toRemove = map.entrySet().iterator().next();
                key = toRemove.getKey();
                value = toRemove.getValue();
                map.remove(key);
                size -= sizeOf(key, value);
            }
        }
    }
}
