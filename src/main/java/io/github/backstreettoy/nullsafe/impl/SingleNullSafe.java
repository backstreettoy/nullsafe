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
     * @see io.github.backstreettoy.nullsafe.NullSafe#notNullThen(Object, Consumer)
     */
    public final <T> boolean notNullThen(T obj, Consumer<T> action) {
        Optional.ofNullable(action).orElseThrow(() -> new NullPointerException("action function must not be null"));
        return notNullThenOrElse(obj, action, null);
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#notNullThenOrElse(Object, Consumer, Action)
     */
    public final <T> boolean notNullThenOrElse(T obj, Consumer<T> notNullAction, Action nullAction) {
        boolean notNull = !isNull(obj);
        if (notNull) {
            if (!isNull(notNullAction)) {
                notNullAction.accept(obj);
            }
        } else if (!isNull(nullAction)){
            nullAction.act();
        }
        return notNull;
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#notNullThenByOptional(Optional, Consumer)
     */
    public final <T> boolean notNullThenByOptional(Optional<T> optional, Consumer<T> action) {
        if (isNull(optional)) {
            throw new NullPointerException("optional must not be null");
        }
        T value = optional.orElse(null);
        return notNullThen(value, action);
    }

    public final <T> boolean notNullThenByOptional(Optional<T> optional,
            Consumer<T> notNullAction,
            Action nullAction) {
        if (isNull(optional)) {
            throw new NullPointerException("optional must not be null");
        }
        T value = optional.orElse(null);
        return notNullThenOrElse(value, notNullAction, nullAction);
    }

    public final <T, R> Optional<? super R> mapNotNull(T obj, Function<T, Optional<R>> map) {
        if (isNull(map)) {
            throw new NullPointerException("map must not be null");
        }
        if (isNull(obj)) {
            return Optional.empty();
        }
        return map.apply(obj);
    }
}
