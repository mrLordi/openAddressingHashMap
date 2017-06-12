package com.vodotiiets;

import java.util.Map;
import java.util.Objects;

/**
 * Open addressing Hash Map based implementation of the <tt>HashMap</tt> interface.
 *
 * <p>This implementation provides constant-time performance for the basic
 * operations (<tt>get</tt> and <tt>put</tt>), assuming the hash function
 * disperses the elements properly among the buckets.
 *
 * all entry records are stored in the bucket array itself. When a new entry has
 * to be inserted, the buckets are examined, starting with the hashed-to slot and
 * proceeding in some probe sequence(in this case - <tt>Linear probing</tt>, in which
 * the intervalbetween probes is fixed (usually 1)), until an unoccupied slot is found.
 * When searchingfor an entry, the buckets are scanned in the same sequence, until
 * either the target record is found, or an unused array slot is found, which indicates
 * that there is no such key in the table.
 *
 * @see HashMap
 *
 * Created by Denys Vodotiiets.
 */
public class OpenAddressingHashMap<K, V> implements HashMap<K, V> {

    /**
     * The default initial capacity - MUST be a power of two.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * Basic hash bin node
     */
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;

        Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue())) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Computes key.hashCode() and spreads (XORs) higher bits of hash
     * to lower.
     */
    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * Returns a power of two size for the given target capacity.
     */
    private static int tableSizeFor(int capacity) {
        int size = capacity - 1;
        size |= size >>> 1;
        size |= size >>> 2;
        size |= size >>> 4;
        size |= size >>> 8;
        size |= size >>> 16;
        return (size < 0) ? 1 : (size >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : size + 1;
    }

    /**
     * The table, initialized on first use, and resized as
     * necessary. When allocated, length is always a power of two.
     * or zero.
     */
    private Node<K, V>[] table;

    /**
     * The number of key-value mappings contained in this map.
     */
    private int size;

    /**
     * The load factor for the hash map.
     */
    private final float loadFactor;

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    private int threshold;

    /**
     * Constructs an empty <tt>OpenAddressingHashMap</tt> with the specified initial
     * capacity and load factor.
     *
     * @param  initialCapacity the initial capacity
     * @param  loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the load factor is nonpositive
     */
    public OpenAddressingHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    /**
     * Constructs an empty <tt>OpenAddressingHashMap</tt> with the specified initial
     * capacity and the default load factor (0.75).
     *
     * @param  initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     */
    public OpenAddressingHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs an empty <tt>OpenAddressingHashMap</tt> with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public OpenAddressingHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
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
     * @throws IllegalArgumentException if the specified key is null
     */
    public boolean put(K key, V value) {
        return putValue(hash(key), key, value, false);
    }

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
    public boolean putOnlyIfAbsent(K key, V value) {
        return putValue(hash(key), key, value, true);
    }

    /**
     * Implements OpenAddressingHashMap.put,
     * OpenAddressingHashMap.putOnlyIfAbsent and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @param value the value to put
     * @param onlyIfAbsent if true, don't change existing value
     * @return <tt>true</tt> if the addition occurred
     * @throws IllegalArgumentException if the specified key is null
     */
    private boolean putValue(int hash, K key, V value, boolean onlyIfAbsent) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key: " + key);
        }

        Node<K,V> currentNode;
        int length, startIndex, currentIndex;
        boolean checkPut = false;

        if (table == null || (length = table.length) == 0) {
            length = (table = resize()).length;
        }

        if ((currentNode = table[startIndex = currentIndex = (length - 1) & hash]) == null) {
            table[startIndex] = newNode(hash, key, value);
            size++;
            checkPut = true;
        } else {
            K currentKey;

            if (currentNode.hash == hash &&
                    ((currentKey = currentNode.key) == key || key.equals(currentKey))) {
                if (currentNode.value == null || !onlyIfAbsent) {
                    currentNode.setValue(value);
                    return true;
                } else {
                    return false;
                }
            } else {
                currentIndex = (length - 1) & (currentIndex + 1);
                while (startIndex != currentIndex) {
                    if ((currentNode = table[currentIndex]) == null) {
                        table[currentIndex] = newNode(hash, key, value);
                        size++;
                        checkPut = true;
                        break;
                    }

                    if (currentNode.hash == hash &&
                            ((currentKey = currentNode.key) == key || key.equals(currentKey))) {
                        if (currentNode.value == null || !onlyIfAbsent) {
                            currentNode.setValue(value);
                            return true;
                        } else {
                            return false;
                        }
                    }
                    currentIndex = (length - 1) & (currentIndex + 1);
                }
            }
        }
        if (!checkPut && threshold != Integer.MAX_VALUE) {
            resize();
            putValue(hash, key, value, onlyIfAbsent);
        }

        if (size > threshold) {
            resize();
        }

        return checkPut;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws IllegalArgumentException if the specified key is null
     */
    public V get(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key: " + key);
        }

        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    /**
     * Implements OpenAddressingHashMap.get and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @return the node, or null if none
     */
    private Node<K,V> getNode(int hash, Object key) {
        Node<K,V> currentNode;
        int length;
        K currentKey;
        int startIndex, currentIndex;

        if ( table != null && (length = table.length) > 0 &&
                (currentNode = table[(startIndex = currentIndex = (length - 1) & hash)]) != null) {
            if (currentNode.hash == hash &&
                    ((currentKey = currentNode.key) == key || (key != null && key.equals(currentKey)))) {
                return currentNode;
            }

            currentIndex = (length - 1) & (currentIndex + 1);
            while (startIndex != currentIndex) {
                if ((currentNode = table[currentIndex]) != null){
                    if (currentNode.hash == hash &&
                            ((currentKey = currentNode.key) == key || (key != null && key.equals(currentKey)))) {
                        return currentNode;
                    }
                    currentIndex = (length - 1) & (currentIndex + 1);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Initializes or doubles table size.  If null, allocates in
     * accord with initial capacity target held in field threshold.
     * Otherwise, the elements from each bin must either stay at
     * same index, or move with a power of two offset in the new table.
     *
     * @return the table
     */
    private Node<K,V>[] resize() {
        Node<K,V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity, newThreshold = 0;

        if (oldCapacity > 0) {
            if (oldCapacity >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTable;
            } else if ((newCapacity = oldCapacity << 1) < MAXIMUM_CAPACITY
                    && oldCapacity >= DEFAULT_INITIAL_CAPACITY) {
                newThreshold = oldThreshold << 1; // double threshold
            }
        } else if (oldThreshold > 0) { // initial capacity was placed in threshold
            newCapacity = oldThreshold;
        } else { // zero initial threshold signifies using defaults
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        if (newThreshold == 0) {
            float ft = (float)newCapacity * loadFactor;
            newThreshold = (newCapacity < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                    (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThreshold;

        Node<K,V>[] newTable = (Node<K,V>[])new Node[newCapacity];
        table = newTable;

        if (oldTable != null) {
            for (int j = 0; j < oldCapacity; ++j) {
                Node<K,V> currentNode;
                if ((currentNode = oldTable[j]) != null) {
                    oldTable[j] = null;
                    newTable[currentNode.hash & (newCapacity - 1)] = currentNode;
                }
            }
        }
        return newTable;
    }

    /**
     * Create a node
     */
    private Node<K,V> newNode(int hash, K key, V value) {
        return new Node<>(hash, key, value);
    }
}
