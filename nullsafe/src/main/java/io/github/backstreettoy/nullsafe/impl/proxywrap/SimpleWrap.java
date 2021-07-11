package io.github.backstreettoy.nullsafe.impl.proxywrap;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.backstreettoy.nullsafe.impl.Pair;
import io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.AbstractFieldHandler;
import io.github.backstreettoy.nullsafe.impl.matchers.AbstractMatcher;

/**
 * @author backstreettoy
 */
public class SimpleWrap {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleWrap.class);

    private static final Class[] ATTACHED_INTERFACES = new Class[] {NullSafeWrapped.class};

    private static final Map<Class<?>, BeanInfo> BEAN_INFO_CACHE = Collections.synchronizedMap(new WeakHashMap<>());


    public static <T> T wrap(Class<T> clazz, T obj, List<Pair<AbstractMatcher, AbstractFieldHandler>> policies) {
        return wrapSupplier(clazz, () -> obj, policies);
    }

    public static <T> T wrapSupplier(Class<T> clazz, Supplier<T> supplier, List<Pair<AbstractMatcher, AbstractFieldHandler>> policies) {
        BeanInfo beanInfo = parseBeanInfo(clazz);
        Class<?> subClazz = createSubClass(clazz, beanInfo);
        T subInstance = (T)createSubInstance(subClazz);
        wrapProperty(subInstance, supplier, policies, beanInfo);
        return subInstance;
    }

    private static <T> void wrapProperty(T subInstance,
                                         Supplier<T> supplier,
                                         List<Pair<AbstractMatcher, AbstractFieldHandler>> policies,
                                         BeanInfo beanInfo) {
        MethodHandlerImpl<T> methodHandler = new MethodHandlerImpl<>(supplier, policies, beanInfo);
        ((Proxy)subInstance).setHandler(methodHandler);
    }

    private static <T> T createSubInstance(Class<T> clazz) {
        try {
            return (T)clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> createSubClass(Class<?> baseClazz, BeanInfo beanInfo) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(baseClazz);
        proxyFactory.setFilter(new MethodFilterImpl(beanInfo));
        proxyFactory.setInterfaces(ATTACHED_INTERFACES);
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
