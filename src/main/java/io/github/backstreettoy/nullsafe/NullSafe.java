package io.github.backstreettoy.nullsafe;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import io.github.backstreettoy.nullsafe.functions.Action;
import io.github.backstreettoy.nullsafe.impl.CompositeNullSafe;
import io.github.backstreettoy.nullsafe.impl.IterableNullSafe;
import io.github.backstreettoy.nullsafe.impl.OptionalValuePair;
import io.github.backstreettoy.nullsafe.impl.Pair;
import io.github.backstreettoy.nullsafe.impl.SingleNullSafe;

/**
 * The facade class of nullsafe utility
 * @author backstreettoy
 */
public final class NullSafe {

    private static final SingleNullSafe SINGLE_ASSERTION = SingleNullSafe.getInstance();
    private static final IterableNullSafe ITERABLE_ASSERTION = IterableNullSafe.getInstance();
    private static final CompositeNullSafe COMPOSITE_NULL_SAFE = CompositeNullSafe.getInstance();

    /**
     * Applying map function if obj is not null
     * @param obj target object
     * @param map the function mapping from obj to result
     * @param <T> the class of param obj
     * @param <R> the class of result
     * @return an Optional class instance, if obj is null then the result is not present
     */
    public static <T, R> Optional<? super R> mapNotNull(T obj, Function<T, R> map) {
        return SINGLE_ASSERTION.mapNotNull(obj, map);
    }

    /**
     * Applying action function if obj is not null.
     * @param obj target object
     * @param action the function called when obj is not null
     * @param <T> the class of the param obj
     */
    public static <T> boolean notNullThen(T obj, Consumer<T> action) {
        return SINGLE_ASSERTION.notNullThen(obj, action);
    }
    /**
     * Applying action function if optional is present.
     * @param optional required, optional object
     * @param action required, the function applied when optional is not null
     * @param <T> the class of the value
     */
    public static <T> boolean notNullThenByOptional(Optional<T> optional, Consumer<T> action) {
        return SINGLE_ASSERTION.notNullThenByOptional(optional, action);
    }

    /**
     * Appling notNullAction function when obj is not null, otherwise nullAction function applied.
     * @param obj target object
     * @param notNullAction not required, the function called when obj is not null
     * @param nullAction not required, the function called when obj is null
     * @param <T> the class of the value
     */
    public static <T> boolean notNullThenOrElse(T obj, Consumer<T> notNullAction, Action nullAction) {
        return SINGLE_ASSERTION.notNullThen(obj, notNullAction, nullAction);
    }

    /**
     * Appling notNullAction function when optional is present, otherwise nullAction function applied.
     * @param optional required, optional object
     * @param notNullAction not required, the function called when obj is not null
     * @param nullAction not required, the function called when obj is null
     * @param <T> the class of the value
     */
    public static final <T> boolean notNullThenOrElseByOptional(Optional<T> optional,
            Consumer<T> notNullAction,
            Action nullAction) {
        return SINGLE_ASSERTION.notNullThenByOptional(optional, notNullAction, nullAction);
    }

    /**
     * Applying the action function to each element not null in iterable
     * @param iterable required, an iterable object
     * @param action required, the function called for each element
     * @param <T> the class of the element
     */
    public static final <T> void notNullElementsThen(Iterable<T> iterable, Consumer<T> action) {
        ITERABLE_ASSERTION.notNullElementsThen(iterable, action);
    }

    /**
     * Creating a new stream filtering out null elements and applying map function on each exist element.
     * @param stream
     * @param map
     * @param <T>
     * @param <R>
     * @return
     */
    public static final <T, R> Stream<? super R> mapEachExistElement(Stream<T> stream, Function<T,R> map) {
        return ITERABLE_ASSERTION.mapExistElements(stream, map);
    }

    /**
     * Invoking the action function if all data params exist.
     * @param params the data params
     * @param action the function called when all data params exist
     */
    public static final boolean ifAllExistThen(Action action, Object... params) {
        return COMPOSITE_NULL_SAFE.ifAllExistThen(action, params);
    }

    /**
     * Invoking the action function if all Optional params are present.
     * @param params the Optional params
     * @param action the function called when all Optional params are present
     */
    public static final boolean ifAllExistThenByOptional(Action action, Optional<?>... params) {
        return COMPOSITE_NULL_SAFE.ifAllExistThenByOptional(action, params);
    }

    /**
     * Invoking the noneOfNullAction function if all Optional params are present,
     * someOfNullConsumer function otherwise.
     * @param noneOfNullAction the function called when all Optional params are present
     * @param someOfNullConsumer the function called when some Optional params is not present,
     *                          the keys of the OptionalValuePair which value not present will be passed as param
     * @param params the OptionalValuePair params
     */
    public static <K> boolean ifAllExistThenOrElseByOptional(Action noneOfNullAction,
            Consumer<List<K>> someOfNullConsumer,
            OptionalValuePair<K, ?>... params) {
        return COMPOSITE_NULL_SAFE.ifAllExistThenOrElseByOptional(noneOfNullAction,
                someOfNullConsumer,
                params);
    }

    /**
     * Applying noneOfNullAction if all data params are not null, otherwise someOfNullConsumer function applied.
     * @param t1 param t1
     * @param t2 param t2
     * @param noneOfNullAction not required, the function called when all data params are not null
     * @param someOfNullConsumer not required, the function called when any of data params is null,
     *                          param of the consumer is the key of null values
     * @param <K> the class of the key property
     */
    public static <K> boolean ifAllExistThen(Pair<K, ?> t1, Pair<K, ?> t2,
            Action noneOfNullAction,
            Consumer<List<K>> someOfNullConsumer) {
        return COMPOSITE_NULL_SAFE.ifAllExistThenOrElse(noneOfNullAction, someOfNullConsumer, t1, t2);
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
    public static final <T1, T2, R> R mapIfAllExistOrElse(T1 t1, T2 t2, BiFunction<T1, T2, R> map, R fallback) {
        COMPOSITE_NULL_SAFE.mapIfAllExistOrElse()
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
    public static final <T1, T2, R> R mapIfAllExistOrElseGet(T1 t1, T2 t2, BiFunction<T1, T2, R> map, Supplier<R> fallback) {
        return null;
    }

    /**
     * Return a optional contains the first not null param. Optional will be empty if none of params exists.
     * @param t1 first param
     * @param t2 second param
     * @param <T> the class of the param
     * @return Optional
     */
    public static final <T> Optional<? super T> coalesce(T t1, T t2) {
        return COMPOSITE_NULL_SAFE.coalesce(t1, t2);
    }

    /**
     * Return a optional contains the first not null param. Optional will be empty if none of params exists.
     * @param t1 first param
     * @param t2 second param
     * @param t3 third param
     * @param <T> the class of the param
     * @return Optional
     */
    public <T> Optional<? super T> coalesce(T t1, T t2, T t3) {
        return COMPOSITE_NULL_SAFE.coalesce(t1, t2, t3);
    }

    /**
     * Return a optional contains the first not null param. Optional will be empty if none of params exists.
     * @param t1 first param
     * @param t2 second param
     * @param t3 third param
     * @param t4 forth param
     * @param <T> the class of the param
     * @return Optional
     */
    public <T> Optional<? super T> coalesce(T t1, T t2, T t3, T t4) {
        return COMPOSITE_NULL_SAFE.coalesce(t1, t2, t3, t4);
    }

    /**
     * Return a optional contains the first not null param. Optional will be empty if none of params exists.
     * @param t1 first param
     * @param t2 second param
     * @param t3 third param
     * @param t4 forth param
     * @param t5 fifth param
     * @param <T> the class of the param
     * @return Optional
     */
    public <T> Optional<? super T> coalesce(T t1, T t2, T t3, T t4, T t5) {
        return COMPOSITE_NULL_SAFE.coalesce(t1, t2, t3, t4, t5);
    }

    /**
     * Return a optional contains the first not null param. Optional will be empty if none of params exists.
     * @param t1 first param
     * @param t2 second param
     * @param t3 third param
     * @param t4 forth param
     * @param t5 fifth param
     * @param t6 sixth param
     * @param <T> the class of the param
     * @return Optional
     */
    public <T> Optional<? super T> coalesce(T t1, T t2, T t3, T t4, T t5, T t6) {
        return COMPOSITE_NULL_SAFE.coalesce(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Return a optional contains the first not null param. Optional will be empty if none of params exists.
     * @param t1 first param
     * @param t2 second param
     * @param t3 third param
     * @param t4 forth param
     * @param t5 fifth param
     * @param t6 sixth param
     * @param t7 seventh param
     * @param <T> the class of the param
     * @return Optional
     */
    public <T> Optional<? super T> coalesce(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return COMPOSITE_NULL_SAFE.coalesce(t1, t2, t3, t4, t5, t6, t7);
    }

}
