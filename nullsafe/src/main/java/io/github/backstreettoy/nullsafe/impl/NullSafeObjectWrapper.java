package io.github.backstreettoy.nullsafe.impl;

import io.github.backstreettoy.nullsafe.NullSafe;
import io.github.backstreettoy.nullsafe.impl.exception.EnrichedNullPointerException;
import io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.AbstractFieldHandler;
import io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.DefaultFieldHandler;
import io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.SupplierFieldHandler;
import io.github.backstreettoy.nullsafe.impl.matchers.AbstractMatcher;
import io.github.backstreettoy.nullsafe.impl.proxywrap.SimpleWrap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NullSafeObjectWrapper<T> {

    private T object;
    private List<Pair<AbstractMatcher, AbstractFieldHandler>> policies;

    NullSafeObjectWrapper(T obj) {
        if (obj == null) {
            throw new EnrichedNullPointerException();
        }
        this.object = obj;
        this.policies = new ArrayList<>();
    }

    public NullSafeObjectWrapper<T> fallback(AbstractMatcher matcher, Object defaultValue) {
        NullSafe.ifAllExistThen(() -> policies.add(Pair.of(matcher, new DefaultFieldHandler(defaultValue))), matcher, defaultValue);
        return this;
    }

    public NullSafeObjectWrapper<T> fallbackGet(AbstractMatcher matcher, Supplier<? extends T> supplier) {
        NullSafe.ifAllExistThen(() -> policies.add(Pair.of(matcher, new SupplierFieldHandler(supplier))), matcher, supplier);
        return this;
    }

    public NullSafeObjectWrapper<T> fallbackThrow(AbstractMatcher matcher) {
        NullSafe.ifAllExistThen(() -> policies.add(Pair.of(matcher, new RaiseExceptionFieldHandler())), matcher);
        return this;
    }

    public T get() {
        return SimpleWrap.wrap((Class<T>)object.getClass(), object, policies);
    }

}
