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
     * Invoke the map function if obj is not null.
     * @param obj target object
     * @param map the map function, return an <code>Optional</code> result
     * @param <T> the class of param obj
     * @param <R> the class of result
     * @return an <code>Optional</code> class instance, if obj is null then the result is not present
     */
    public static <T, R> Optional<? super R> mapNotNull(T obj, Function<T, Optional<R>> map) {
        return SINGLE_ASSERTION.mapNotNull(obj, map);
    }

    /**
     * Invoke the action function if obj is not null.
     * @param obj target object
     * @param action the function called when obj is not null
     * @param <T> the class of the param obj
     * @return boolean indicate if obj is exist
     */
    public static <T> boolean notNullThen(T obj, Consumer<T> action) {
        return SINGLE_ASSERTION.notNullThen(obj, action);
    }
    /**
     * Invoke the action function if the optional is present.
     * @param optional required, optional object
     * @param action required, the function applied when optional is present
     * @param <T> the class of the of
     * @return boolean indicate if optional is exist and present
     */
    public static <T> boolean notNullThenByOptional(Optional<T> optional, Consumer<T> action) {
        return SINGLE_ASSERTION.notNullThenByOptional(optional, action);
    }

    /**
     * Invoke the notNullAction function when obj is not null, otherwise nullAction function invoked.
     * @param obj target object
     * @param notNullAction not required, the function called when obj is not null
     * @param nullAction not required, the function called when obj is null
     * @param <T> the class of the of
     * @return boolean indicate if obj is exist
     */
    public static <T> boolean notNullThenOrElse(T obj, Consumer<T> notNullAction, Action nullAction) {
        return SINGLE_ASSERTION.notNullThenOrElse(obj, notNullAction, nullAction);
    }

    /**
     * Invoke the notNullAction function when optional is present, otherwise nullAction function invoked.
     * @param optional required, optional object
     * @param notNullAction not required, the function called when obj is not null
     * @param nullAction not required, the function called when obj is null
     * @param <T> the class of the of
     * @return boolean indicate if optional is present
     */
    public static final <T> boolean notNullThenOrElseByOptional(Optional<T> optional,
            Consumer<T> notNullAction,
            Action nullAction) {
        return SINGLE_ASSERTION.notNullThenByOptional(optional, notNullAction, nullAction);
    }

    /**
     * Apply the action function to each element not null in iterable.
     * @param iterable an iterable object
     * @param action required, the function called for each element
     * @param <T> the class of the element
     */
    public static final <T> void notNullElementsThen(Iterable<T> iterable, Consumer<T> action) {
        ITERABLE_ASSERTION.notNullElementsThen(iterable, action);
    }

    /**
     * Create a new stream filtering out null elements and applying map function to each exist element.
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
     * Return an <code>Optional</code> contains the first not null param.
     * Optional will be empty if none of params exists.
     * @param params the params
     * @param <T> the class of the param
     * @return instance of <code>Optional</code> contains the first not null param
     */
    public static final <T> Optional<? super T> coalesce(T... params) {
        return ITERABLE_ASSERTION.coalesce(params);
    }

    /**
     * Return an <code>Optional</code> contains the first not null param in iterable.
     * Optional will be empty if none of params exists.
     * @param iterable the iterable collection
     * @param <T> the class of the param
     * @return instance of <code>Optional</code> contains the first not null param
     */
    public static final <T> Optional<? super T> coalesce(Iterable<T> iterable) {
        return ITERABLE_ASSERTION.coalesce(iterable);
    }

    /**
     * Invoke the action function if all data params exist.
     * @param params the data params
     * @param action the function called when all data params exist
     * @return boolean indicate if all data params are exist
     */
    public static final boolean ifAllExistThen(Action action, Object... params) {
        return COMPOSITE_NULL_SAFE.ifAllExistThen(action, params);
    }

    /**
     * Invoke the noneOfNullAction if all data params are exist, otherwise anyIsNullAction function invoked.
     * @param noneOfNullAction not required, the function called when all data params are not null
     * @param anyIsNullAction not required, the function called when any of data params is null,
     *                          param of the consumer is the key of null values
     * @param params the data params
     * @return boolean indicate if all data params are exist
     */
    public static final boolean ifAllExistThenOrElse(Action noneOfNullAction,
            Action anyIsNullAction,
            Object... params) {
        return COMPOSITE_NULL_SAFE.ifAllExistThenOrElse(noneOfNullAction, anyIsNullAction, params);
    }

    public static final <K> boolean ifAllExistThenOrElse(Action noneOfNullAction,
            Consumer<List<K>> anyIsNullAction,
            Pair<K, ?>... params) {
        return COMPOSITE_NULL_SAFE.namedIfAllExistThenOrElse(noneOfNullAction, anyIsNullAction, params);
    }

    /**
     * Invoke the action function if all <code>Optional</code> params are present.
     * @param params the <code>Optional</code> params
     * @param action required, the function called when all <code>Optional</code> params are present
     * @return boolean indicate if all <code>Optional</code> params are present
     */
    public static final boolean ifAllExistThenByOptional(Action action, Optional<?>... params) {
        return COMPOSITE_NULL_SAFE.ifAllExistThenByOptional(action, params);
    }

    /**
     * Invoke the noneOfNullAction function if all <code>Optional</code> params are present,
     * anyIsNullConsumer function otherwise.
     * @param noneOfNullAction not required, the function called when all <code>Optional</code> params are present
     * @param anyIsNullConsumer not required, the function called when some <code>Optional</code> params is not present,
     *                          the keys of the <code>OptionalValuePair</code> which of not present will
     *                          be passed as param
     * @param params the <code>OptionalValuePair</code> params, at least on param needed
     * @return boolean indicate if all <code>OptionalValuePair</code> params are present
     */
    public static <K> boolean ifAllExistThenOrElseByOptional(Action noneOfNullAction,
            Consumer<List<K>> anyIsNullConsumer,
            OptionalValuePair<K, ?>... params) {
        return COMPOSITE_NULL_SAFE.namedIfAllExistThenOrElseByOptional(noneOfNullAction,
                anyIsNullConsumer,
                params);
    }

    /**
     * Invoke map function if all data params are exist and return the result of map function,
     * otherwise just return the of of.
     * @param map required, the map function
     * @param fallback the of of returned if any data param is null
     * @param <R> the class of return type
     * @return of of type R result from map function result or of of
     */
    public static <R> Optional<? super R> mapIfAllExistOrElse(Supplier<Optional<R>> map, R fallback, Object... params) {
        return COMPOSITE_NULL_SAFE.mapIfAllExistOrElse(map, fallback, params);
    }

    /**
     * Invoke the map function if all data params are not null and return the result of map function,
     * otherwise invoke the of function and return the result.
     * @param map required, the map function
     * @param fallback required, the of function called if any data param is null
     * @param <R> the class of return type
     * @return of of type R result from map function result or of function
     */
    public <R> Optional<? super R> mapIfAllExistOrElseGet(Supplier<Optional<R>> map,
            Supplier<Optional<? super R>> fallback,
            Object... params) {
        return COMPOSITE_NULL_SAFE.mapIfAllExistOrElseGet(map, fallback, params);
    }

}
