package com.vodotiiets;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Denys Vodotiiets.
 */
public class OpenAddressingHashMap<K, V> implements HashMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

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

    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private static int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    private Node<K, V>[] table;
    private int size;
    private final float loadFactor;
    private int threshold;

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

    public OpenAddressingHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public OpenAddressingHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean put(K key, V value) {
        return putVal(hash(key), key, value, false);
    }

    @Override
    public boolean putOnlyIfAbsent(K key, V value) {
        return putVal(hash(key), key, value, true);
    }

    private boolean putVal(int hash, K key, V value, boolean onlyIfAbsent) {
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

            if (currentNode.hash == hash && !onlyIfAbsent &&
                    ((currentKey = currentNode.key) == key || (key != null && key.equals(currentKey)))) {
                currentNode.setValue(value);
            }

            currentIndex = (length - 1) & (currentIndex + 1);
            while (startIndex != currentIndex) {
                if ((currentNode = table[currentIndex]) == null){
                    table[currentIndex] = newNode(hash, key, value);
                    size++;
                    checkPut = true;
                    break;
                }

                if (currentNode.hash == hash && !onlyIfAbsent &&
                        ((currentKey = currentNode.key) == key
                                || (key != null && key.equals(currentKey)))) {
                    currentNode.setValue(value);
                }

                currentIndex = (length - 1) & (currentIndex + 1);
            }
        }
        if (!checkPut && threshold != Integer.MAX_VALUE) {
            resize();
            putVal(hash, key, value, onlyIfAbsent);
        }

        if (size > threshold) {
            resize();
        }

        return checkPut;
    }

    @Override
    public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

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

    private Node<K,V> newNode(int hash, K key, V value) {
        return new Node<>(hash, key, value);
    }
}
