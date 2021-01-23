package io.github.backstreettoy.nullsafe.impl;

/**
 * a key-value data holder.
 * @author backstreettoy
 */
public class Pair<K, V> {

    private K key;
    private V value;

    /**
     * static method used for creating a new Pair instance
     * @param key key
     * @param value value
     * @param <K> the class of key
     * @param <V> the class of value
     * @return Pair instance
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair(key, value);
    }


    private Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
