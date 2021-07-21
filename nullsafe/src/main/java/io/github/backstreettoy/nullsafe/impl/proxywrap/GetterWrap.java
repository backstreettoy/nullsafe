package io.github.backstreettoy.nullsafe.impl.proxywrap;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;

import io.github.backstreettoy.nullsafe.extra.jodd.cache.Cache;
import io.github.backstreettoy.nullsafe.extra.jodd.cache.LRUCache;
import io.github.backstreettoy.nullsafe.impl.Pair;
import io.github.backstreettoy.nullsafe.impl.config.WrapConfig;
import io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.AbstractFieldHandler;
import io.github.backstreettoy.nullsafe.impl.matchers.AbstractMatcher;

/**
 * @author backstreettoy
 */
public class GetterWrap {

    private static Cache<Class<?>, BeanInfo> BEAN_INFO_CACHE = new LRUCache<>(1024);

    public static <T> T wrap(T obj, Class<?>[] interfaces) {
        if (obj == null) {
            throw new NullPointerException("Obj should not be null");
        }
        Class<?> baseClazz = obj.getClass();
        BeanInfo beanInfo = parseBeanInfo(baseClazz);
        Class<?> subClazz = createSubClass(baseClazz, beanInfo, interfaces);
        return (T)createSubInstance(subClazz);
    }

    private static <T> void wrapProperty(T subInstance,
                                         Supplier<T> supplier,
                                         List<Pair<AbstractMatcher, AbstractFieldHandler>> policies,
                                         WrapConfig config,
                                         BeanInfo beanInfo) {
        MethodHandlerImpl<T> methodHandler = new MethodHandlerImpl<>(supplier, policies, config, beanInfo);
        ((Proxy)subInstance).setHandler(methodHandler);
    }

    private static <T> T createSubInstance(Class<T> clazz) {
        try {
            return (T)clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> createSubClass(Class<?> baseClazz, BeanInfo beanInfo, Class<?>[] interfaces) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(baseClazz);
        proxyFactory.setFilter(new MethodFilterImpl(beanInfo));
        proxyFactory.setInterfaces(interfaces);
        Class<?> subClazz = proxyFactory.createClass();
        return subClazz;
    }

    private static BeanInfo parseBeanInfo(Class<?> clazz) {
        BeanInfo beanInfo = BEAN_INFO_CACHE.get(clazz);
        if (beanInfo == null) {
            try {
                beanInfo = Introspector.getBeanInfo(clazz);
                BEAN_INFO_CACHE.put(clazz, beanInfo);
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
        }
        return beanInfo;
    }

    private static class MethodFilterImpl implements MethodFilter {

        private BeanInfo beanInfo;
        private Set<Method> interceptMethods;

        public MethodFilterImpl(BeanInfo beanInfo) {
            this.beanInfo = beanInfo;
            interceptMethods = Arrays.stream(beanInfo.getPropertyDescriptors())
                    .map(x -> x.getReadMethod())
                    .filter(x -> x != null)
                    .collect(Collectors.toSet());

        }

        @Override
        public boolean isHandled(Method m) {
            return interceptMethods.contains(m);
        }
    }
}
