package com.mymapview.presentation.model.cache;


public interface Cache<K, V> {

    V get(K key);

    V put(K key, V value);

    V remove(K key);
}
