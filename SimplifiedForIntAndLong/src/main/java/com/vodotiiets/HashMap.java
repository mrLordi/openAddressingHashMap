package com.vodotiiets;

/**
 * An object that maps keys to values.  A map cannot contain duplicate keys;
 * each key can map to at most one value.
 *
 * Created by Denys Vodotiiets.
 */
public interface HashMap {

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
     * @throws IllegalStateException if map is full
     */
    boolean put(int key, long value);

    /**
     * Returns the value to which the specified key is mapped.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws IllegalStateException if map is empty or map has no such key
     */
    long get(int key);
}
