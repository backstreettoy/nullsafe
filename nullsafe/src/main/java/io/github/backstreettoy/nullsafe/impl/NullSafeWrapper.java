package io.github.backstreettoy.nullsafe.impl;

import io.github.backstreettoy.nullsafe.impl.matchers.AbstractMatcher;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class NullSafeWrapper<T> {

    private static final Class[] ATTACHED_INTERFACES = new Class[] {NullSafeWrapped.class};

    public static <T> NullSafeObjectWrapper<T> wrap(T obj) {
        return null;
    }

    public static <T> NullSafeObjectWrapper<T> wrapRecursively(T obj) {
        return new NullSafeObjectWrapper<>(obj);
    }

    public static <T> T unwrap(T obj) {
        return null;
    }

    public static final <T> T apply(Class<T> clazz) {
        try {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setSuperclass(clazz);
            BeanInfo beanInfo = introspectBeanInfo(clazz);
            proxyFactory.setFilter(new MethodFilterImpl(beanInfo));
            proxyFactory.setInterfaces(ATTACHED_INTERFACES);
            Class<?> subClazz = proxyFactory.createClass();

            Object subInstance = subClazz.newInstance();
            ((Proxy) subInstance).setHandler(new GetterMethodHandler(beanInfo));
            return (T) subInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static BeanInfo introspectBeanInfo(Class<?> clazz) throws IntrospectionException {
        return Introspector.getBeanInfo(clazz);
    }

    /**
     * Indicator of a wrapped class
     */
    private static interface NullSafeWrapped {

    }

    private static class MethodFilterImpl implements MethodFilter {
        private Set<Method> interceptMethods;

        public MethodFilterImpl(BeanInfo beanInfo) {
            interceptMethods = new HashSet<>();
            if (beanInfo != null && beanInfo.getPropertyDescriptors() != null) {
                for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                    if ("class".equals(propertyDescriptor.getName())) {
                        continue;
                    }
                    Method readMethod = propertyDescriptor.getReadMethod();
                    if (readMethod != null) {
                        interceptMethods.add(readMethod);
                    }
                }
            }
        }

        @Override
        public boolean isHandled(Method m) {
            return interceptMethods.contains(m);
        }
    }

    private static class GetterMethodHandler implements MethodHandler {

        private Map<Method, PropertyDescriptor> getterMetodToPropertyMapping;

        public GetterMethodHandler(BeanInfo beanInfo) {

        }

        @Override
        public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
            Object result = proceed.invoke(self);
            return null;
        }
    }

}
