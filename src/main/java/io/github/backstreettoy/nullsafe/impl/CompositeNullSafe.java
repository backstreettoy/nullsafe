package io.github.backstreettoy.nullsafe.impl;

import java.util.ArrayList;
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
        boolean paramNotNull = SINGLE_NULL_SAFE.notNullThen(params, null, anyIsNullAction);
        if (!paramNotNull) {
            return false;
        }

        int paramIndex = 0;
        List<Pair<Integer, Object>> pairs = new ArrayList<>(params.length);
        for (Object param : params) {
            pairs.add(Pair.of(paramIndex++, param));
        }

        return ifAllExistThenOrElse(noneOfNullAction,
                x -> anyIsNullAction.act(),
                pairs.toArray(new Pair[]{}));
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#ifAllExistThenOrElse(Action, Consumer, Pair[])
     */
    @SafeVarargs
    public final <K> boolean ifAllExistThenOrElse(Action noneOfNullAction,
            Consumer<List<K>> anyIsNullConsumer,
            Pair<K, ?>... params) {
        boolean paramExist = SINGLE_NULL_SAFE.notNullThen(params, null, () -> {
            SINGLE_NULL_SAFE.notNullThen(anyIsNullConsumer, x -> x.accept(Collections.emptyList()));
        });
        if (!paramExist) {
            return false;
        }

        List<K> nullValueKeys = Stream.of(params)
                .filter(x -> SINGLE_NULL_SAFE.isNull(x))
                .map(x -> x.getKey())
                .collect(Collectors.toList());
        return handleNullValueKeys(nullValueKeys, noneOfNullAction, anyIsNullConsumer);
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#ifAllExistThenByOptional(Action, Optional[])
     */
    @SafeVarargs
    public final boolean ifAllExistThenByOptional(Action action,
            Optional<?>... params) {
        Optional.ofNullable(action).orElseThrow(() -> new NullPointerException("action function must not be null"));
        int paramIndex = 0;
        List<OptionalValuePair<Integer, ?>> pairs = new ArrayList<>(params.length);
        for (Optional<?> param : params) {
            pairs.add(OptionalValuePair.of(paramIndex++, param));
        }

        return ifAllExistThenOrElseByOptional(action, null, pairs.toArray(new OptionalValuePair[]{}));
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#ifAllExistThenOrElseByOptional(Action, Consumer, OptionalValuePair[])
     */
    @SafeVarargs
    public final <K> boolean ifAllExistThenOrElseByOptional(Action noneOfNullAction,
            Consumer<List<K>> anyIsNullConsumer,
            OptionalValuePair<K, ?>... params) {
        boolean paramExist = SINGLE_NULL_SAFE.isNull(params);
        if (!paramExist) {
            anyIsNullConsumer.accept(Collections.EMPTY_LIST);
            return false;
        }

        List<K> emptyOptionalKeys = Stream.of(params)
                .filter(x -> SINGLE_NULL_SAFE.isNull(x) || !x.getOptionalValue().isPresent())
                .map(x -> x.getKey())
                .collect(Collectors.toList());
        return handleNullValueKeys(emptyOptionalKeys, noneOfNullAction, anyIsNullConsumer);
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

    private <K> boolean handleNullValueKeys(List<K> keys,
            Action noneOfNullAction,
            Consumer<List<K>> anyIsNullConsumer) {
        if (keys.isEmpty()) {
            noneOfNullAction.act();
            return true;
        } else {
            anyIsNullConsumer.accept(keys);
            return false;
        }
    }
}
