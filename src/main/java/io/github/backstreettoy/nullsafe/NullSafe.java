package io.github.backstreettoy.nullsafe;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import io.github.backstreettoy.nullsafe.impl.Pair;

/**
 *
 * @author backstreettoy
 */
public class NullSafe {

    /**
     * Invoke action function when obj is not null.
     * @param obj target object
     * @param action the function called when obj is not null
     * @param <T> the class of the value
     */
    public static final <T> void notNullThen(Object obj, Consumer<T> action) {
        return;
    }

    /**
     * Invoke not null action function when obj is not null, otherwise invoke null action.
     * @param obj target object
     * @param notNullAction required, the function called when obj is not null
     * @param nullAction not required, the function called when obj is null, nothing will happen if this param is null
     * @param <T> the class of the value
     */
    public static final <T> void notNullThen(Object obj, Consumer<T> notNullAction, Consumer<Void> nullAction) {

    }

    /**
     * Handle elements not null in iterable by function action
     * @param iterable an iterable object
     * @param action the function called for each element
     * @param <T> the class of the element
     */
    public static final <T> void handleNotNullElements(Iterable<T> iterable, Consumer<T> action) {

    }

    /**
     *
     * @param stream
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    public static final <T, R> Stream<R> mapNotNullElements(Stream<T> stream, Function<T,R> function) {
        return null;
    }

    /**
     * Invoke action function if both t1 and t2 are not null.
     * @param t1 param t1
     * @param t2 param t2
     * @param action the function called when both t1 and t2 are not null
     * @param <T1> the class of t1
     * @param <T2> the class of t2
     */
    public static final <T1, T2> void noneOfNullThen(T1 t1, T2 t2, BiConsumer<T1, T2> action) {

    }

    public static final <T1, T2> void noneOfNullThen(T1 t1, T2 t2,
            BiConsumer<T1, T2> noneOfNullAction,
            BiConsumer<T1, T2> someIsNullAction) {

    }

    /**
     * Invoke noneOfNullAction if both the value property of t1 and t2 are not null, otherwise invoke anyOfNullAction.
     * @param t1 param t1
     * @param t2 param t2
     * @param noneOfNullAction the function called when both t1 and t2 are not null
     * @param someIsNullAction not required, the function called when any of t1 and t2 is null.
     * @param <K1> the class of the key property of t1
     * @param <K2> the class of the key property of t2
     * @param <V1> the class of the value property of t1
     * @param <V2> the class of the value property of t2
     */
    public static final <K1, K2, V1, V2> void noneOfNullThen(Pair<K1, V1> t1, Pair<K2, V2> t2,
            BiConsumer<Pair<K1, V1>, Pair<K2, V2>> noneOfNullAction,
            BiConsumer<Pair<K1, V1>, Pair<K2, V2>> someIsNullAction) {

    }

    /**
     * Invoke map function if both t1 and t2 are not null, otherwise just return fallback value
     * @param t1 param t1
     * @param t2 param t2
     * @param map the map function
     * @param fallback the fallback value returned if any of t1 and t2 is null
     * @param <T1> the class of t1
     * @param <T2> the class of t2
     * @param <R> the class of return type
     * @return value of type R result from map function result or fallback value
     */
    public static final <T1, T2, R> R mapIfNoneOfNullOrElse(T1 t1, T2 t2, BiFunction<T1, T2, R> map, R fallback) {
        return null;
    }

    /**
     * Invoke map function if both t1 and t2 are not null, otherwise invoke fallback function
     * @param t1 param t1
     * @param t2 param t2
     * @param map the map function
     * @param fallback the fallback function called if any of t1 and t2 is null, if this param is null then return null
     * @param <T1> the class of t1
     * @param <T2> the class of t2
     * @param <R> the class of return type
     * @return value of type R result from map function result or fallback function
     */
    public static final <T1, T2, R> R mapIfNoneOfNullOrElseGet(T1 t1, T2 t2, BiFunction<T1, T2, R> map, Supplier<R> fallback) {
        return null;
    }

    public static final <T> T coalesce(T t1, T t2) {
        return null;
    }

    public <T> T coalesce(T t1, T t2, T t3) {
        return null;
    }

    public <T> T coalesce(T t1, T t2, T t3, T t4) {
        return null;
    }

    public <T> T coalesce(T t1, T t2, T t3, T t4, T t5) {
        return null;
    }

    public <T> T coalesce(T t1, T t2, T t3, T t4, T t5, T t6) {
        return null;
    }

    public <T> T coalesce(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return null;
    }


    public interface BiConsumer<T1, T2> {
        void consume(T1 t1, T2 t2);
    }
}
