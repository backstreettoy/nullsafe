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
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NullSafeWrapper<T> {

    private static final Class[] ATTACHED_INTERFACES = new Class[] {NullSafeWrapped.class};

    private WrapContext<T> context;

    public static <T> NullSafeWrapper<T> wrap(Class<T> clazz) {
        WrapContext<T> context = new WrapContext<>(clazz);
        return new NullSafeWrapper(context);
    }

    public static <T> NullSafeWrapper<T> wrapRecursively(Object obj) {
        return null;
    }

    public static <T> T unwrap(T obj) {
        return null;
    }

    public NullSafeWrapper fallback(AbstractMatcher matcher, Object defaultValue) {
        return this;
    }

    public <T> NullSafeWrapper fallbackGet(AbstractMatcher matcher, Supplier<? extends T> supplier) {
        return this;
    }

    public <X> NullSafeWrapper fallbackThrow(AbstractMatcher matcher, Supplier<? extends X> supplier) {
        return this;
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

    private NullSafeWrapper(WrapContext<T> context) {
        this.context = context;
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

    /**
     * Context for handle
     */
    public static class WrapContext<T> {
        private Class<T> clazz;
        private List<Pair<AbstractMatcher, Consumer<?>>> nullHandlers = new ArrayList<>();

        public WrapContext(Class<T> clazz) {
            this.clazz = clazz;
        }

        public Class<T> getClazz() {
            return clazz;
        }

        public void setClazz(Class<T> clazz) {
            this.clazz = clazz;
        }

        public List<Pair<AbstractMatcher, Consumer<?>>> getNullHandlers() {
            return nullHandlers;
        }

        public void setNullHandlers(List<Pair<AbstractMatcher, Consumer<?>>> nullHandlers) {
            this.nullHandlers = nullHandlers;
        }
    }

}
