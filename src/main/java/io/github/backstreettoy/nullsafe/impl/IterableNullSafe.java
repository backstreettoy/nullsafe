package io.github.backstreettoy.nullsafe.impl;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @author backstreettoy
 */
public class IterableNullSafe {

    private static final IterableNullSafe ITERABLE_ASSERTION = new IterableNullSafe();
    private static final SingleNullSafe SINGLE_ASSERTION = SingleNullSafe.getInstance();

    public static IterableNullSafe getInstance() {
        return ITERABLE_ASSERTION;
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#notNullElementsThen(Iterable, Consumer)
     */
    public final <T> void notNullElementsThen(Iterable<T> iterable, Consumer<T> action) {
        if (SINGLE_ASSERTION.isNull(iterable)) {
            throw new NullPointerException("iterable must not be null");
        }
        if (SINGLE_ASSERTION.isNull(action)) {
            throw new NullPointerException("action must not be null");
        }

        for (T element : iterable) {
            if (!SINGLE_ASSERTION.isNull(element)) {
                action.accept(element);
            }
        }
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#mapNotNullElements(Stream, Function)
     */
    public final <T, R> Stream<? super R> mapNotNullElements(Stream<T> stream, Function<T,R> map) {
        if (SINGLE_ASSERTION.isNull(stream)) {
            throw new NullPointerException("stream must not be null");
        }
        if (SINGLE_ASSERTION.isNull(map)) {
            throw new NullPointerException("map function must not be null");
        }
        return stream.filter(x -> !SINGLE_ASSERTION.isNull(x))
                .map(x -> map);
    }
}
