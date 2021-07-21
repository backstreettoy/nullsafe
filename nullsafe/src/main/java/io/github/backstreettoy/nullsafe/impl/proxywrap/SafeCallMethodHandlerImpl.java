package io.github.backstreettoy.nullsafe.impl.proxywrap;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import javassist.util.proxy.MethodHandler;

/**
 * @author backstreettoy
 */
public class SafeCallMethodHandlerImpl implements MethodHandler {

    private ConcurrentHashMap<Method, SafeCallWrapped> returnValueWrappers;

    public SafeCallMethodHandlerImpl() {
        returnValueWrappers = new ConcurrentHashMap<>();
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        if (returnValueWrappers.contains(thisMethod)) {
            return returnValueWrappers.get(thisMethod);
        }

        Class<?> returnType = thisMethod.getReturnType();
        //基于已有object构造wrapper
        //基于returnType构造wrapper

        thisMethod.setAccessible(true);
        Object returnValue = thisMethod.invoke(self);
    }
}
