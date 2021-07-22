package io.github.backstreettoy.nullsafe.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import io.github.backstreettoy.nullsafe.NullSafe;
import io.github.backstreettoy.nullsafe.impl.config.WrapConfig;
import io.github.backstreettoy.nullsafe.impl.exception.EnrichedNullPointerException;
import io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.AbstractFieldHandler;
import io.github.backstreettoy.nullsafe.impl.matchers.AbstractMatcher;
import io.github.backstreettoy.nullsafe.impl.proxywrap.SimpleWrap;

public class NullSafeObjectWrapper<T> {

    private static final WrapConfig DEFAULT_CONFIG;

    static {
        DEFAULT_CONFIG = new WrapConfig();
        DEFAULT_CONFIG.setEnableConvert(true);
        DEFAULT_CONFIG.setSilent(false);
    }

    private T object;
    private List<Pair<AbstractMatcher, AbstractFieldHandler>> policies;
    private WrapConfig config;

    NullSafeObjectWrapper(T obj) {
        if (obj == null) {
            throw new EnrichedNullPointerException();
        }
        this.object = obj;
        this.policies = new ArrayList<>();
        initConfig();

    }

    private void initConfig() {
        try {
            this.config = (WrapConfig)DEFAULT_CONFIG.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public NullSafeObjectWrapper<T> fallback(AbstractMatcher matcher, AbstractFieldHandler handler) {
        NullSafe.ifAllExistThen(() -> policies.add(Pair.of(matcher, handler)), matcher, handler);
        return this;
    }

    /**
     * Do not throw {@code Exception} just log failure and try next fallback handler when exception occur.
     * @return
     */
    public NullSafeObjectWrapper<T> silent() {
        config.setSilent(true);
        return this;
    }

    /**
     * Disable type convert when return type and fallback value type not match.
     * @return
     */
    public NullSafeObjectWrapper<T> disableConvert() {
        config.setEnableConvert(false);
        return this;
    }

    /**
     * The getter methods of the wrapped object return a fake stubbed object when property is {@code null}
     * preventing from {@code NullPointerException} be thrown in the successive operation. Invoking the
     * getter method of fake stubbed object
     * will only return a new fake stubbed object corresponding to the target type and there will no any affect when
     * setters and other methods called.
     *
     * @return
     */
    public SafeCallWrapper<T> safeCall() {
        return new SafeCallWrapper<>(object);
    }

    public T get() {
        return SimpleWrap.wrap((Class<T>)object.getClass(), object, policies, config);
    }


}
