package io.github.backstreettoy.nullsafe.impl;

import java.util.Optional;

/**
 *
 * @author backstreettoy
 */
public class OptionalValuePair<K, V> {
    private K key;
    private Optional<V> optionalValue;

    /**
     * static method used for creating a new Pair instance
     * @param key key
     * @param value optionalValue
     * @param <K> the class of key
     * @param <V> the class of optionalValue
     * @return Pair instance
     */
    public static <K, V> OptionalValuePair<K, V> of(K key, Optional<V> value) {
        return new OptionalValuePair(key, value);
    }


    private OptionalValuePair(K key, Optional<V> value) {
        this.key = key;
        this.optionalValue = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public Optional<V> getOptionalValue() {
        return optionalValue;
    }

    public void setOptionalValue(Optional<V> optionalValue) {
        this.optionalValue = optionalValue;
    }
}
