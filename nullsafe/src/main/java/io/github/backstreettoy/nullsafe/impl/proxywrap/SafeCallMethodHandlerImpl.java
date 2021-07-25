package io.github.backstreettoy.nullsafe.impl.proxywrap;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;

import io.github.backstreettoy.nullsafe.impl.config.SafeCallConfig;
import io.github.backstreettoy.nullsafe.impl.safecall.SafeCallConstants;

/**
 * @author backstreettoy
 */
public class SafeCallMethodHandlerImpl implements MethodHandler {

    private ConcurrentHashMap<Method, SafeCallWrapped> returnValueWrappers;

    private Object proxyTarget;

    private SafeCallConfig config;

    public SafeCallMethodHandlerImpl(SafeCallConfig config) {
        this.config = config;
        returnValueWrappers = new ConcurrentHashMap<>();
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        String methodName = thisMethod.getName();
        if (methodName.equals(SafeCallConstants.PROXY_TARGET_OPERATE)
                && args != null && args.length == 1) {
            handleProxyTarget(args);
            return null;
        } else if (methodName.equals(SafeCallConstants.GET_PROXY_TARGET_OPERATE)) {
            return parseProxyTarget();
        }

        Class<?> returnType = thisMethod.getReturnType();
        Object returnValue = null;
        SafeCallWrapped proxy = null;
        if (!Modifier.isFinal(returnType.getModifiers())) {
            // Create proxy
            proxy = returnValueWrappers.computeIfAbsent(thisMethod, key -> {
                Proxy subInstance = GetterWrap.wrap(returnType,
                        SafeCallConstants.INTERFACES,
                        config.isThrowExceptionWhenWrapMethodFail());
                subInstance.setHandler(new SafeCallMethodHandlerImpl(this.config));
                return (SafeCallWrapped)subInstance;
            });

        }
        if (proxyTarget != null) {
            returnValue = thisMethod.invoke(proxyTarget);
        }
        if (proxy != null) {
            proxy.__impl(returnValue);
            return proxy;
        } else if (SafeCallConstants.NULL_OBJECTS_MAPPING.containsKey(returnType)) {
            // Basic boxed data object of jdk.
            // It will not reference more objects and return fallback value.
            return Optional.ofNullable(returnValue).orElse(SafeCallConstants.NULL_OBJECTS_MAPPING.get(returnType));
        } else if (returnType.isPrimitive()) {
            // Primitive type

        }
        else {
            throw new RuntimeException("Unable create proxy sub-class of type:" + returnType.getName());
        }
    }

    private Object parseProxyTarget() {
        return this.proxyTarget;
    }

    /**
     * Store the proxy target of this proxy object.
     * @param args
     */
    private void handleProxyTarget(Object[] args) {
        this.proxyTarget = args[0];
    }
}
