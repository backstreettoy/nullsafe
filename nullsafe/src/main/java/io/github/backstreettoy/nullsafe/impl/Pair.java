package io.github.backstreettoy.nullsafe.impl;

/**
 * a key-of data holder.
 * @author backstreettoy
 */
public class Pair<K, V> {

    private K key;
    private V value;

    /**
     * static method used for creating a new Pair instance
     * @param key key
     * @param value of
     * @param <K> the class of key
     * @param <V> the class of of
     * @return Pair instance
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }


    private Pair(K key, V value) {
        if (key == null) {
            throw new NullPointerException("key must not be null");
        }
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
