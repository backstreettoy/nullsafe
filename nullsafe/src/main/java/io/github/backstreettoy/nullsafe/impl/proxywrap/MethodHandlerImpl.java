package io.github.backstreettoy.nullsafe.impl.proxywrap;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javassist.util.proxy.MethodHandler;

import io.github.backstreettoy.nullsafe.impl.Pair;
import io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.AbstractFieldHandler;
import io.github.backstreettoy.nullsafe.impl.matchers.AbstractMatcher;


/**
 * @author backstreettoy
 */
public class MethodHandlerImpl<T> implements MethodHandler {

//    private Map<Method, NullSafeWrapped> methodProxies;
    private Map<Method, List<AbstractFieldHandler>> propertyfallbackHandlers;
    private List<Pair<AbstractMatcher, AbstractFieldHandler>> policies;
    private Supplier<T> supplier;

    public MethodHandlerImpl(Supplier<T> supplier, List<Pair<AbstractMatcher, AbstractFieldHandler>> policies, BeanInfo beanInfo) {
        this.supplier = supplier;
        this.policies = policies;
//        methodProxies = new HashMap<>();
        propertyfallbackHandlers = new HashMap<>();
        initProxy(policies, beanInfo);
    }

    private void initProxy(List<Pair<AbstractMatcher, AbstractFieldHandler>> policies, BeanInfo beanInfo) {
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            if (propertyType.isPrimitive()) {
                continue;
            }
            for (Pair<AbstractMatcher, AbstractFieldHandler> policy : policies) {
                AbstractMatcher matcher = policy.getKey();
                AbstractFieldHandler handler = policy.getValue();
                if (matcher.match(propertyDescriptor)) {
                    addFieldHandlerToMethod(readMethod, handler);
                }
            }
        }
    }

    private void addFieldHandlerToMethod(Method method, AbstractFieldHandler handler) {
        // Indicating this method need be proxy
//        methodProxies.put(method, null);
        List<AbstractFieldHandler> handlers = propertyfallbackHandlers.get(method);
        if (handlers == null) {
            handlers = new ArrayList<>();
            propertyfallbackHandlers.put(method, handlers);
        }
        handlers.add(handler);
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        boolean needProxy = propertyfallbackHandlers.containsKey(thisMethod);
        T referenceObj = supplier.get();
        Object result = thisMethod.invoke(referenceObj);
        if (!needProxy || result != null) {
            return result;
        }

        List<AbstractFieldHandler> handlers = propertyfallbackHandlers.get(thisMethod);
        if (!handlers.isEmpty()) {
            return handlers.get(0).fallback();
        }
        throw new RuntimeException("code error");
    }

    private static class Getter implements Supplier<Object> {

        private Method method;
        private Object target;

        public Getter(Method method, Object target) {
            this.method = method;
            this.target = target;
        }

        @Override
        public Object get() {
            method.setAccessible(true);
            try {
                return method.invoke(target);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
