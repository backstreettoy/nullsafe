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


    public static <T> NullSafeObjectWrapper<T> wrap(T obj) {
        return null;
    }

    public static <T> NullSafeObjectWrapper<T> wrapRecursively(T obj) {
        return new NullSafeObjectWrapper(obj);
    }

    public static <T> T unwrap(T obj) {
        return null;
    }

    public static final <T> T apply(Class<T> clazz) {
        return null;
    }


    private static BeanInfo introspectBeanInfo(Class<?> clazz) throws IntrospectionException {
        return Introspector.getBeanInfo(clazz);
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
