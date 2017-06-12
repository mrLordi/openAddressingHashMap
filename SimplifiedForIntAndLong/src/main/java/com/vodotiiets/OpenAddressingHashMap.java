package com.vodotiiets;

import java.util.Objects;

/**
 * Open addressing Hash Map based implementation of the <tt>HashMap</tt> interface.
 *
 * <p>This implementation provides constant-time performance for the basic
 * operations (<tt>get</tt> and <tt>put</tt>), assuming the hash function
 * disperses the elements properly among the buckets.
 *
 * All entry records are stored in the bucket array itself. When a new entry has
 * to be inserted, the buckets are examined, starting with the hashed-to slot and
 * proceeding in some probe sequence(in this case - <tt>Linear probing</tt>, in which
 * the interval between probes is fixed (usually 1)), until an unoccupied slot is found.
 * When searching for an entry, the buckets are scanned in the same sequence, until
 * either the target record is found, or an unused array slot is found, which indicates
 * that there is no such key in the table.
 *
 * @see HashMap
 *
 * Created by Denys Vodotiiets.
 */
public class OpenAddressingHashMap implements HashMap {

    static class Node {
        final int hash;
        final int key;
        long value;

        Node(int hash, int key, long value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        final int getKey() {
            return key;
        }

        final long getValue() {
            return value;
        }

        final void setValue(long newValue) {
            value = newValue;
        }
    }

    /**
     * The default initial capacity - MUST be a power of two.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;


    /**
     * Computes key.hashCode() and spreads (XORs) higher bits of hash
     * to lower.
     */
    private static int hash(int key) {
        int h;
        return  (h = Objects.hashCode(key)) ^ (h >>> 16);
    }

    /**
     * The table, initialized on first use
     */
    private Node[] table;

    /**
     * The capacity of map
     */
    private int capacity;

    /**
     * The number of key-value mappings contained in this map.
     */
    private int size;

    /**
     * Constructs an empty <tt>OpenAddressingHashMap</tt> with the specified initial
     * capacity.
     *
     * @param  initialCapacity the initial capacity
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the load factor is nonpositive
     */
    public OpenAddressingHashMap(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if (initialCapacity >= Integer.MAX_VALUE) {
            initialCapacity = Integer.MAX_VALUE - 1;
        }

        capacity = Math.max(3 * initialCapacity / 2, initialCapacity) + 1;
        table = new Node[capacity];
    }

    /**
     * Constructs an empty <tt>OpenAddressingHashMap</tt> with the default initial capacity.
     */
    public OpenAddressingHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        table = new Node[capacity];
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return size;
    }

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
    public boolean put(int key, long value) {
        if (size == capacity) {
            throw new RuntimeException("There is no place for new data");
        }

        int hash = hash(key);
        Node currentNode;
        int currentIndex, startIndex;
        currentIndex = startIndex = (capacity - 1) & hash;

        do {
            if ((currentNode = table[currentIndex]) == null) {
                table[currentIndex] = newNode(hash, key, value);
                size++;
                return true;
            }

            if (currentNode.getKey() == key) {
                currentNode.setValue(value);
                return true;
            }

            currentIndex++;

            if (currentIndex == capacity) {
                currentIndex = 0;
            }
        } while (currentIndex != startIndex);

        return false;
    }

    /**
     * Create a node
     */
    private Node newNode(int hash, int key, long value) {
        return new Node(hash, key, value);
    }

    /**
     * Returns the value to which the specified key is mapped.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped
     * @throws IllegalStateException if map is empty or map has no such key
     */
    public long get(int key) {

        if (size == 0) {
            throw new IllegalStateException("Map is empty!");
        }

        int hash = hash(key);
        Node currentNode;
        int currentIndex, startIndex;
        currentIndex = startIndex = (capacity - 1) & hash;

        do {
            if ((currentNode = table[currentIndex]) == null) {
                break;
            }

            if (currentNode.getKey() == key) {
                return currentNode.getValue();
            }

            currentIndex++;

            if (currentIndex == capacity) {
                currentIndex = 0;
            }
        } while (currentIndex != startIndex);

        throw new IllegalStateException("No such key!");
    }

}
