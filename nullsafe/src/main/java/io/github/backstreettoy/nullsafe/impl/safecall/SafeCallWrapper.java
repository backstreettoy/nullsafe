package io.github.backstreettoy.nullsafe.impl.safecall;

import java.util.function.Predicate;

import javassist.util.proxy.Proxy;

import io.github.backstreettoy.nullsafe.impl.config.SafeCallConfig;
import io.github.backstreettoy.nullsafe.impl.proxywrap.GetterWrap;
import io.github.backstreettoy.nullsafe.impl.proxywrap.SafeCallMethodHandlerImpl;
import io.github.backstreettoy.nullsafe.impl.proxywrap.SafeCallWrapped;

/**
 * @author backstreettoy
 */
public class SafeCallWrapper<T> {

    private static final SafeCallConfig DEFAULT_CONFIG;

    static {
        DEFAULT_CONFIG = new SafeCallConfig();
        DEFAULT_CONFIG.setThrowExceptionWhenWrapMethodFail(false);
    }

    private T obj;
    private SafeCallConfig config;

    public SafeCallWrapper(T obj) {
        if (obj == null) {
            throw new NullPointerException("Obj is null");
        }

        this.obj = obj;
        try {
            this.config = DEFAULT_CONFIG.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public SafeCallWrapper<T> throwExceptionWhenWrapMethodFail() {
        config.setThrowExceptionWhenWrapMethodFail(true);
        return this;
    }

    public T get() {
        SafeCallWrapped proxy = GetterWrap.wrap(obj.getClass(),
                SafeCallConstants.INTERFACES,
                config.isThrowExceptionWhenWrapMethodFail());
        ((Proxy)proxy).setHandler(new SafeCallMethodHandlerImpl(config));
        proxy.__impl(obj);
        return (T)proxy;
    }

    public static <V> V evaluate(V obj) {
        if (obj instanceof SafeCallWrapped) {
            SafeCallWrapped proxy = (SafeCallWrapped) obj;
            return (V)proxy.__getimpl();
        } else {
            Object nullIndicator = SafeCallConstants.NULL_OBJECTS.get(obj);
            if (nullIndicator != null && nullIndicator == obj) {
                // Exactly the obj is null indicator itself.
                return null;
            } else {
                // Otherwise
                return obj;
            }
        }
    }

    public static <T> boolean evaluateMatch(T obj, Predicate<T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        T realValue = evaluate(obj);
        return predicate.test(realValue);
    }

    public static <T> Boolean evaluateMatchWithDefault(T obj, Predicate<T> predicate, Boolean defaultValue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        T realValue = evaluate(obj);
        if (realValue != null) {
            return predicate.test(realValue);
        }
        return defaultValue;
    }
}
