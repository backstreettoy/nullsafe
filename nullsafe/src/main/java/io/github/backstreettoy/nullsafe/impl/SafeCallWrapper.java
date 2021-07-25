package io.github.backstreettoy.nullsafe.impl;

import javassist.util.proxy.Proxy;

import io.github.backstreettoy.nullsafe.impl.config.SafeCallConfig;
import io.github.backstreettoy.nullsafe.impl.proxywrap.GetterWrap;
import io.github.backstreettoy.nullsafe.impl.proxywrap.SafeCallMethodHandlerImpl;
import io.github.backstreettoy.nullsafe.impl.proxywrap.SafeCallWrapped;
import io.github.backstreettoy.nullsafe.impl.safecall.SafeCallConstants;

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

    public static <V> V eval(V obj) {
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
}
