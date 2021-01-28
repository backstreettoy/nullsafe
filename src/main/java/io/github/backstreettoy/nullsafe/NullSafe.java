package io.github.backstreettoy.nullsafe;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import io.github.backstreettoy.nullsafe.functions.Action;
import io.github.backstreettoy.nullsafe.impl.CompositeNullSafe;
import io.github.backstreettoy.nullsafe.impl.IterableNullSafe;
import io.github.backstreettoy.nullsafe.impl.OptionalValuePair;
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
     * @param stream the input stream
     * @param map the map function
     * @param <T> the type of input stream class
     * @param <R> the type of return stream class
     * @return a stream of type R
     */
    public static final <T, R> Stream<? super R> mapEachExistElement(Stream<T> stream, Function<T,R> map) {
        return ITERABLE_ASSERTION.mapExistElements(stream, map);
    }

    /**
     * Return a optional contains the first not null param. Optional will be empty if none of params exists.
     * @param params the params
     * @param <T> the class of the param
     * @return Optional
     */
    public static final <T> Optional<? super T> coalesce(T... params) {
        return ITERABLE_ASSERTION.coalesce(params);
    }

    /**
     * Return a optional contains the first not null param in iterable. Optional will be empty if none of params exists.
     * @param iterable the iterable collection
     * @param <T> the class of the param
     * @return Optional
     */
    public static final <T> Optional<? super T> coalesce(Iterable<T> iterable) {
        return ITERABLE_ASSERTION.coalesce(iterable);
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
     * Invoke the noneOfNullAction function if all Optional params are present,
     * anyIsNullConsumer function otherwise.
     * @param noneOfNullAction the function called when all Optional params are present
     * @param anyIsNullConsumer the function called when some Optional params is not present,
     *                          the keys of the OptionalValuePair which value not present will be passed as param
     * @param params the OptionalValuePair params
     */
    public static <K> boolean ifAllExistThenOrElseByOptional(Action noneOfNullAction,
            Consumer<List<K>> anyIsNullConsumer,
            OptionalValuePair<K, ?>... params) {
        return COMPOSITE_NULL_SAFE.ifAllExistThenOrElseByOptional(noneOfNullAction,
                anyIsNullConsumer,
                params);
    }

    /**
     * Invoke the noneOfNullAction if all data params are not null, otherwise anyIsNullAction function invoked.
     * @param noneOfNullAction not required, the function called when all data params are not null
     * @param anyIsNullAction not required, the function called when any of data params is null,
     *                          param of the consumer is the key of null values
     */
    public final boolean ifAllExistThenOrElse(Action noneOfNullAction,
            Action anyIsNullAction,
            Object... params) {
        return COMPOSITE_NULL_SAFE.ifAllExistThenOrElse(noneOfNullAction, anyIsNullAction, params);
    }

    /**
     * Invoke map function if all data params are not null and return the return result of map function,
     * otherwise just return the fallback value.
     * @param map the map function
     * @param fallback the fallback value returned if any of t1 and t2 is null
     * @param <R> the class of return type
     * @return value of type R result from map function result or fallback value
     */
    public static <R> Optional<? super R> mapIfAllExistOrElse(Supplier<Optional<R>> map, R fallback, Object... params) {
        return COMPOSITE_NULL_SAFE.mapIfAllExistOrElse(map, fallback, params);
    }

    /**
     * Invoke map function if all data params are not null and return the return result of map function,
     * otherwise invoke fallback function and return the result of fallback function.
     * @param map the map function
     * @param fallback the fallback function called if any of t1 and t2 is null, if this param is null then return null
     * @param <R> the class of return type
     * @return value of type R result from map function result or fallback function
     */
    public <R> Optional<? super R> mapIfAllExistOrElseGet(Supplier<Optional<R>> map,
            Supplier<Optional<? super R>> fallback,
            Object... params) {
        return COMPOSITE_NULL_SAFE.mapIfAllExistOrElseGet(map, fallback, params);
    }

}
