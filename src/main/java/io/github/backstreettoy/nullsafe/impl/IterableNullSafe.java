package io.github.backstreettoy.nullsafe.impl;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @author backstreettoy
 */
public final class IterableNullSafe {

    private static final IterableNullSafe INSTANCE = new IterableNullSafe();
    private static final SingleNullSafe SINGLE_NULL_SAFE = SingleNullSafe.getInstance();

    public static IterableNullSafe getInstance() {
        return INSTANCE;
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#notNullElementsThen(Iterable, Consumer)
     */
    public <T> void notNullElementsThen(Iterable<T> iterable, Consumer<T> action) {
        if (SINGLE_NULL_SAFE.isNull(iterable)) {
            throw new NullPointerException("iterable must not be null");
        }
        if (SINGLE_NULL_SAFE.isNull(action)) {
            throw new NullPointerException("action must not be null");
        }

        for (T element : iterable) {
            if (!SINGLE_NULL_SAFE.isNull(element)) {
                action.accept(element);
            }
        }
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#mapExistElements(Stream, Function)
     */
    public <T, R> Stream<? super R> mapExistElements(Stream<T> stream, Function<T,R> map) {
        if (SINGLE_NULL_SAFE.isNull(stream)) {
            throw new NullPointerException("stream must not be null");
        }
        if (SINGLE_NULL_SAFE.isNull(map)) {
            throw new NullPointerException("map function must not be null");
        }
        return stream.filter(x -> !SINGLE_NULL_SAFE.isNull(x))
                .map(map::apply);
    }
}
