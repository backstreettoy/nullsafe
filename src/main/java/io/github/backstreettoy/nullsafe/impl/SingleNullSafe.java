package io.github.backstreettoy.nullsafe.impl;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import io.github.backstreettoy.nullsafe.functions.Action;

/**
 *
 * @author backstreettoy
 */
public final class SingleNullSafe {

    private static final SingleNullSafe INSTANCE = new SingleNullSafe();

    public static final SingleNullSafe getInstance() {
        return INSTANCE;
    }

    public final boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#notNullThen(Object, Consumer, Action)
     */
    public final <T> void notNullThen(T obj, Consumer<T> action) {
        if (isNull(action)) {
            throw new NullPointerException("action must not be null");
        }
        notNullThen(obj, action, null);
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#notNullThen(Object, Consumer, Action)
     */
    public final <T> void notNullThen(T obj, Consumer<T> notNullAction, Action nullAction) {
        if (!isNull(obj)) {
            if (!isNull(notNullAction)) {
                notNullAction.accept(obj);
            }
        } else if (!isNull(nullAction)){
            nullAction.act();
        }
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#notNullThenByOptional(Optional, Consumer)
     */
    public final <T> void notNullThenByOptional(Optional<T> optional, Consumer<T> action) {
        if (isNull(optional)) {
            throw new NullPointerException("optional must not be null");
        }
        T value = optional.orElse(null);
        notNullThen(value, action);
    }

    public final <T> void notNullThenByOptional(Optional<T> optional, Consumer<T> notNullAction, Action nullAction) {
        if (isNull(optional)) {
            throw new NullPointerException("optional must not be null");
        }
        T value = optional.orElse(null);
        notNullThen(value, notNullAction, nullAction);
    }



    public final <T, R> Optional<R> mapNotNull(T obj, Function<T, R> map) {
        if (isNull(map)) {
            throw new NullPointerException("map must not be null");
        }
        if (isNull(obj)) {
            return Optional.empty();
        }
        return Optional.ofNullable(map.apply(obj));
    }



}
