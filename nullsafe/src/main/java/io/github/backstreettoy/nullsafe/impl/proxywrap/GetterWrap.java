package io.github.backstreettoy.nullsafe.impl.proxywrap;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.backstreettoy.nullsafe.extra.jodd.cache.Cache;
import io.github.backstreettoy.nullsafe.extra.jodd.cache.LRUCache;

/**
 * @author backstreettoy
 */
public class GetterWrap {

    private static Logger LOG = LoggerFactory.getLogger(GetterWrap.class);

    private static Cache<Class<?>, BeanInfo> BEAN_INFO_CACHE = new LRUCache<>(1024);

    public static <T> T wrap(Class<?> clazz, Class<?>[] interfaces, boolean exceptionWhenWrapMethodFail) {
        if (clazz == null) {
            throw new NullPointerException("Clazz should not be null");
        }
        BeanInfo beanInfo = parseBeanInfo(clazz);
        Class<?> subClazz = createSubClass(clazz, beanInfo, interfaces, exceptionWhenWrapMethodFail);
        return (T)createSubInstance(subClazz);
    }

    private static <T> T createSubInstance(Class<T> clazz) {
        try {
            return (T)clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> createSubClass(Class<?> baseClazz, BeanInfo beanInfo, Class<?>[] interfaces,
                                           boolean throwExceptionWhenWrapFail) {
        checkBaseClass(baseClazz, throwExceptionWhenWrapFail);
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(baseClazz);
        proxyFactory.setFilter(new MethodFilterImpl(beanInfo, interfaces, throwExceptionWhenWrapFail));
        proxyFactory.setInterfaces(interfaces);
        Class<?> subClazz = proxyFactory.createClass();
        return subClazz;
    }

    private static void checkBaseClass(Class<?> baseClazz, boolean throwExceptionWhenWrapFail) {
        int modifiers = baseClazz.getModifiers();
        if (Modifier.isFinal(modifiers)) {
            LOG.warn("Failed to create wrapped object to class:{}", baseClazz.getName());
            if (throwExceptionWhenWrapFail) {
                throw new RuntimeException("Failed to create wrapped object to class:" + baseClazz.getName());
            }
        }
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

        private static final Set<String> IGNORED_METHODS;

        static {
            IGNORED_METHODS = new HashSet<>();
            IGNORED_METHODS.add("getClass");
        }

        private BeanInfo beanInfo;
        private Set<Method> interceptMethods;
        private Set<Method> extraInterceptMethods;

        public MethodFilterImpl(BeanInfo beanInfo, Class<?>[] interfaces, boolean throwExceptionWhenWrapMethodFail) {
            this.beanInfo = beanInfo;
            interceptMethods = Arrays.stream(beanInfo.getPropertyDescriptors())
                    .map(x -> x.getReadMethod())
                    .filter(x -> x != null && !IGNORED_METHODS.contains(x.getName()))
                    .collect(Collectors.toSet());

            if (interfaces != null) {
                extraInterceptMethods = Arrays.stream(interfaces)
                        .flatMap(x -> Arrays.stream(x.getMethods()))
                        .collect(Collectors.toSet());
            }

            // Verify any final method exists.
            Set<Method> finalMethods = interceptMethods.stream().filter(x -> Modifier.isFinal(x.getModifiers()))
                    .collect(Collectors.toSet());
            if (!finalMethods.isEmpty()) {
                String methodStr = finalMethods.stream().map(x -> x.getName()).collect(Collectors.joining(","));
                LOG.warn("Final method can not be wrapped, class:{}, methods:{}", methodStr);
                if (throwExceptionWhenWrapMethodFail) {
                    throw new RuntimeException("Final method can not be wrapped, methods:" + methodStr);
                }
            }

        }

        @Override
        public boolean isHandled(Method m) {
            return (interceptMethods.contains(m)
                    || (extraInterceptMethods != null ? extraInterceptMethods.contains(m) : false));
        }
    }
}
