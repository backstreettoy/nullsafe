package io.github.backstreettoy.nullsafe.impl;

import java.util.ArrayList;
import java.util.List;

import io.github.backstreettoy.nullsafe.NullSafe;
import io.github.backstreettoy.nullsafe.impl.exception.EnrichedNullPointerException;
import io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.AbstractFieldHandler;
import io.github.backstreettoy.nullsafe.impl.matchers.AbstractMatcher;
import io.github.backstreettoy.nullsafe.impl.proxywrap.SimpleWrap;

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

    public NullSafeObjectWrapper<T> fallback(AbstractMatcher matcher, AbstractFieldHandler handler) {
        NullSafe.ifAllExistThen(() -> policies.add(Pair.of(matcher, handler)), matcher, handler);
        return this;
    }

    public T get() {
        return SimpleWrap.wrap((Class<T>)object.getClass(), object, policies);
    }

}
