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

    public static CompositeNullSafe getInstance() {
        return INSTANCE;
    }

    @SafeVarargs
    public final boolean ifAllExistThen(Action action, Object... params) {
        return ifAllExistThenOrElse(action, null, params);
    }

    @SafeVarargs
    public final boolean ifAllExistThenOrElse(Action noneOfNullAction,
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

        return ifAllExistThenOrElse(noneOfNullAction,
                x -> someIsNullAction.act(),
                pairs.toArray(new Pair[]{}));
    }

    @SafeVarargs
    public final <K> boolean ifAllExistThenOrElse(Action noneOfNullAction,
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

    public final boolean ifAllExistThenByOptional(Action action,
            Optional<?>... params) {
        int paramIndex = 0;
        List<OptionalValuePair<Integer, ?>> pairs = new ArrayList<>(params.length);
        for (Optional<?> param : params) {
            pairs.add(OptionalValuePair.of(paramIndex++, param));
        }

        return ifAllExistThenOrElseByOptional(action, null, pairs.toArray(new OptionalValuePair[]{}));
    }

    @SafeVarargs
    public final <K> boolean ifAllExistThenOrElseByOptional(Action noneOfNullAction,
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

    public <R> Optional<? super R> mapIfAllExistOrElse(Supplier<Optional<R>> map, R fallback, Object... params) {
        boolean allParamExist = ifAllExistThenOrElse(null, null, params);
        if (allParamExist) {
            return map.get();
        } else {
            return Optional.ofNullable(fallback);
        }
    }

    public <R> Optional<? super R> mapIfAllExistOrElseGet(Supplier<Optional<R>> map,
            Supplier<Optional<? super R>> fallback,
            Object... params) {
        boolean allParamExist = ifAllExistThenOrElse(null, null, params);
        if (allParamExist) {
            return map.get();
        } else {
            return SINGLE_NULL_SAFE.isNull(fallback) ? Optional.empty() : fallback.get();
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
