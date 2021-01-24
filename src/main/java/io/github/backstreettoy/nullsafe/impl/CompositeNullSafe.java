package io.github.backstreettoy.nullsafe.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
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

    public static CompositeNullSafe getInstance() {
        return INSTANCE;
    }

    /**
     * @see io.github.backstreettoy.nullsafe.NullSafe#coalesce(Object, Object)
     */
    @SafeVarargs
    public final <T> Optional<? super T> coalesce(T... params) {
        if (SINGLE_NULL_SAFE.isNull(params)) {
            return Optional.empty();
        }
        for (T param : params) {
            if (!SINGLE_NULL_SAFE.isNull(param)) {
                return Optional.of(param);
            }
        }
        return Optional.empty();
    }

    @SafeVarargs
    public final boolean allExistThen(Action action, Object... params) {
        return allExistThen(action, null, params);
    }

    @SafeVarargs
    public final boolean allExistThen(Action noneOfNullAction,
            Action someIsNullAction,
            Object... params) {
        boolean paramNotNull = SINGLE_NULL_SAFE.notNullThen(params, null, someIsNullAction);
        if (!paramNotNull) {
            return false;
        }

        int paramIndex = 0;
        List<Pair<Integer, Object>> pairs = new ArrayList<>(params.length);
        for (Object param : params) {
            pairs.add(Pair.of(paramIndex++, param));
        }

        return allExistThen(noneOfNullAction,
                x -> someIsNullAction.act(),
                pairs.toArray(new Pair[]{}));
    }

    @SafeVarargs
    public final <K> boolean allExistThen(Action noneOfNullAction,
            Consumer<List<K>> someOfNullConsumer,
            Pair<K, ?>... params) {
        boolean paramExist = SINGLE_NULL_SAFE.notNullThen(params, null, () -> {
            SINGLE_NULL_SAFE.notNullThen(someOfNullConsumer, x -> x.accept(Collections.emptyList()));
        });
        if (!paramExist) {
            return false;
        }

        List<K> nullValueKeys = Stream.of(params)
                .filter(x -> SINGLE_NULL_SAFE.isNull(x))
                .map(x -> x.getKey())
                .collect(Collectors.toList());
        return handleNullValueKeys(nullValueKeys, noneOfNullAction, someOfNullConsumer);
    }

    public final boolean allExistThenByOptional(
            Action action,
            Optional<?>... params) {
        int paramIndex = 0;
        List<OptionalValuePair<Integer, ?>> pairs = new ArrayList<>(params.length);
        for (Optional<?> param : params) {
            pairs.add(OptionalValuePair.of(paramIndex++, param));
        }

        return allExistThenByOptional(action, null, pairs.toArray(new OptionalValuePair[]{}));
    }

    @SafeVarargs
    public final <K> boolean allExistThenByOptional(Action noneOfNullAction,
            Consumer<List<K>> someOfNullConsumer,
            OptionalValuePair<K, ?>... params) {
        boolean paramExist = SINGLE_NULL_SAFE.notNullThen(params, null, () -> {
            SINGLE_NULL_SAFE.notNullThen(someOfNullConsumer, x -> x.accept(Collections.emptyList()));
        });
        if (!paramExist) {
            return false;
        }

        List<K> emptyOptionalKeys = Stream.of(params)
                .filter(x -> SINGLE_NULL_SAFE.isNull(x) || !x.getOptionalValue().isPresent())
                .map(x -> x.getKey())
                .collect(Collectors.toList());
        return handleNullValueKeys(emptyOptionalKeys, noneOfNullAction, someOfNullConsumer);
    }

    public <T1, T2, R> Optional<R> mapIfAllExistOrElse(T1 t1, T2 t2, Supplier<Optional<R>> map, R fallback) {
        boolean allParamExist = allExistThen(null, null, t1, t2);
        if (allParamExist) {
            return map.get();
        } else {
            return Optional.ofNullable(fallback);
        }
    }

    private <K> boolean handleNullValueKeys(List<K> keys,
            Action noneOfNullAction,
            Consumer<List<K>> someOfNullConsumer) {
        if (keys.isEmpty()) {
            noneOfNullAction.act();
            return true;
        } else {
            someOfNullConsumer.accept(keys);
            return false;
        }
    }
}
