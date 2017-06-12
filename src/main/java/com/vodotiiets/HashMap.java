package com.vodotiiets;

/**
 * An object that maps keys to values.  A map cannot contain duplicate keys;
 * each key can map to at most one value.
 *
 * Created by Denys Vodotiiets.
 */
public interface HashMap<K, V> {

    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of key-value mappings in this map
     */
    int size();

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return <tt>true</tt> if the addition occurred
     * @throws IllegalArgumentException if the specified key is null
     */
    boolean put(K key, V value);

    /**
     * If the specified key is not already associated with a value (or is mapped
     * to {@code null}) associates it with the given value and returns
     * <tt>true</tt>.
     *

     /**
     * If the specified key is not already associated with a value (or is mapped
     * to {@code null}) associates it with the given value and returns
     * <tt>true</tt>.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return <tt>true</tt> if the addition occurred
     * @throws IllegalArgumentException if the specified key is null
     */
    boolean putOnlyIfAbsent(K key, V value);

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws IllegalArgumentException if the specified key is null
     */
    V get(Object key);
}
