package io.github.backstreettoy.nullsafe.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.backstreettoy.nullsafe.functions.Action;

/**
 *
 * @author backstreettoy
 */
public final class CompositeNullSafe {
    private static final CompositeNullSafe INSTANCE = new CompositeNullSafe();
    private static final SingleNullSafe SINGLE_NULL_SAFE = SingleNullSafe.getInstance();
    private static final IterableNullSafe ITERABLE_NULL_SAFE = IterableNullSafe.getInstance();

    public static CompositeNullSafe getInstance() {
        return INSTANCE;
    }

    private CompositeNullSafe() {}

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#ifAllExistThen(Action, Object...)
     */
    @SafeVarargs
    public final boolean ifAllExistThen(Action action, Object... params) {
        Optional.ofNullable(action).orElseThrow(() -> new NullPointerException("action function must not be null"));
        return ifAllExistThenOrElse(action, null, params);
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#ifAllExistThenOrElse(Action, Action, Object...)
     */
    @SafeVarargs
    public final boolean ifAllExistThenOrElse(Action noneOfNullAction,
            Action anyIsNullAction,
            Object... params) {
        boolean paramNull = SINGLE_NULL_SAFE.isNull(params);
        if (paramNull) {
            SINGLE_NULL_SAFE.notNullThen(anyIsNullAction, x -> x.act());
            return false;
        }

        Pair<Integer, ?>[] keyValueParams = new Pair[params.length];
        for (int paramIndex = 0; paramIndex < params.length; ++paramIndex) {
            keyValueParams[paramIndex] = Pair.of(paramIndex, params[paramIndex]);
        }

        return namedIfAllExistThenOrElse(noneOfNullAction,
                x -> SINGLE_NULL_SAFE.notNullThen(anyIsNullAction, action -> action.act()),
                keyValueParams);
    }

    @SafeVarargs
    public final boolean ifAllExistThenOrElseByOptional(Action noneOfNullAction,
            Action anyIsNullAction,
            Optional<?>... params) {
        boolean paramNull = SINGLE_NULL_SAFE.isNull(params);
        if (paramNull) {
            SINGLE_NULL_SAFE.notNullThen(anyIsNullAction, x -> x.act());
            return false;
        }

        OptionalValuePair<Integer, ?>[] keyValueParams = new OptionalValuePair[params.length];
        for (int paramIndex = 0; paramIndex < params.length; ++paramIndex) {
            keyValueParams[paramIndex] = OptionalValuePair.of(paramIndex, params[paramIndex]);
        }

        return namedIfAllExistThenOrElseByOptional(noneOfNullAction,
                x -> SINGLE_NULL_SAFE.notNullThen(anyIsNullAction, action -> action.act()),
                keyValueParams);
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#ifAllExistThenOrElse(Action, Consumer, Pair[])
     */
    @SafeVarargs
    public final <K> boolean namedIfAllExistThenOrElse(Action noneOfNullAction,
            Consumer<List<K>> anyIsNullConsumer,
            Pair<K, ?>... params) {
        // handle null param
        if (SINGLE_NULL_SAFE.isNull(params)) {
            SINGLE_NULL_SAFE.notNullThen(anyIsNullConsumer, x -> x.accept(Collections.EMPTY_LIST));
            return false;
        }

        OptionalValuePair[] optionalValuePairs = wrapToOptionalValuePair(params);
        return namedIfAllExistThenOrElseByOptional(noneOfNullAction, anyIsNullConsumer, optionalValuePairs);
    }


    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#ifAllExistThenByOptional(Action, Optional[])
     */
    @SafeVarargs
    public final boolean ifAllExistThenByOptional(Action action,
            Optional<?>... params) {
        Optional.ofNullable(action).orElseThrow(() -> new NullPointerException("action function must not be null"));
        boolean allParamsExist = ifAllExistThenOrElse(null, null, params);
        if (!allParamsExist) {
            return false;
        }
        if (params.length == 0) {
            throw new IllegalArgumentException("expect at least one param");
        }
        int paramIndex = 0;
        List<OptionalValuePair<Integer, ?>> pairs = new ArrayList<>(params.length);
        for (Optional<?> param : params) {
            pairs.add(OptionalValuePair.of(paramIndex++, param));
        }

        return namedIfAllExistThenOrElseByOptional(action, null, pairs.toArray(new OptionalValuePair[]{}));
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#ifAllExistThenOrElseByOptional(Action, Consumer, OptionalValuePair[])
     */
    @SafeVarargs
    public final <K> boolean namedIfAllExistThenOrElseByOptional(Action noneOfNullAction,
            Consumer<List<K>> anyIsNullConsumer,
            OptionalValuePair<K, ?>... params) {
        if (SINGLE_NULL_SAFE.isNull(params)) {
            // call anyIsNullConsumer if present with empty key list
            SINGLE_NULL_SAFE.notNullThen(anyIsNullConsumer, x -> x.accept(Collections.EMPTY_LIST));
            return false;
        }

        if (params.length == 0) {
            throw new IllegalArgumentException("expect at least one param");
        }

        boolean existElementNull = existNullElement(params);
        List<K> emptyOptionalKeys = Stream.of(params)
                .filter(x -> SINGLE_NULL_SAFE.isOptionalValuePairEmpty(x))
                .filter(x -> x != null)
                .map(x -> x.getKey())
                .collect(Collectors.toList());
        return handleNullValueKeys(existElementNull, emptyOptionalKeys, noneOfNullAction, anyIsNullConsumer);
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#mapIfAllExistOrElse(Supplier, Object, Object...)
     */
    @SafeVarargs
    public final <R> Optional<? super R> mapIfAllExistOrElse(Supplier<Optional<R>> map, R fallback, Object... params) {
        boolean allParamExist = ifAllExistThenOrElse(null, null, params);
        if (allParamExist) {
            return map.get();
        } else {
            return Optional.ofNullable(fallback);
        }
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#mapIfAllExistOrElseGet(Supplier, Supplier, Object...)
     */
    public <R> Optional<? super R> mapIfAllExistOrElseGet(Supplier<Optional<R>> map,
            Supplier<Optional<? super R>> fallback,
            Object... params) {
        Optional.ofNullable(map)
                .orElseThrow(() -> new NullPointerException("map function must not be null"));
        Optional.ofNullable(fallback)
                .orElseThrow(() -> new NullPointerException("fallback function must not be null"));
        boolean allParamExist = ifAllExistThenOrElse(null, null, params);
        if (allParamExist) {
            return map.get();
        } else {
            return fallback.get();
        }
    }

    private <K> boolean handleNullValueKeys(boolean existNullElement, List<K> keys,
            Action noneOfNullAction,
            Consumer<List<K>> anyIsNullConsumer) {
        if (existNullElement || !keys.isEmpty()) {
            SINGLE_NULL_SAFE.notNullThen(anyIsNullConsumer, x -> x.accept(keys));
            return false;
        } else {
            SINGLE_NULL_SAFE.notNullThen(noneOfNullAction, x -> x.act());
            return true;
        }
    }

    private <K> OptionalValuePair<K, ?>[] wrapToOptionalValuePair(Pair<K,?>[] params) {
        return Arrays.stream(params)
                .map(x -> SINGLE_NULL_SAFE.isNull(x)
                        ? null : OptionalValuePair.of(x.getKey(), Optional.ofNullable(x.getValue())))
                .toArray(OptionalValuePair[]::new);
    }

    private boolean existNullElement(Object... params) {
        for (Object param : params) {
            if (param == null) {
                return true;
            }
        }
        return false;
    }
}
