package com.vodotiiets;

/**
 * Created by Denys Vodotiiets.
 */
public interface HashMap<K, V> {
    int size();
    boolean put(K key, V value);
    boolean putOnlyIfAbsent(K key, V value);
    V get(Object key);
}
