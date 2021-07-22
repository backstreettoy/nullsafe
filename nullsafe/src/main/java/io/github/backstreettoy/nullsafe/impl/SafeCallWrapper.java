package io.github.backstreettoy.nullsafe.impl;

import javassist.util.proxy.Proxy;

import io.github.backstreettoy.nullsafe.impl.proxywrap.GetterWrap;
import io.github.backstreettoy.nullsafe.impl.proxywrap.SafeCallMethodHandlerImpl;
import io.github.backstreettoy.nullsafe.impl.proxywrap.SafeCallWrapped;
import io.github.backstreettoy.nullsafe.impl.safecall.SafeCallConstants;

/**
 * @author backstreettoy
 */
public class SafeCallWrapper<T> {

    private T obj;

    public SafeCallWrapper(T obj) {
        this.obj = obj;
    }

    public T get() {
        SafeCallWrapped proxy = GetterWrap.wrap(obj.getClass(), SafeCallConstants.INTERFACES);
        ((Proxy)proxy).setHandler(new SafeCallMethodHandlerImpl());
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
