package io.github.backstreettoy.nullsafe.impl.proxywrap;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

import javassist.util.proxy.MethodHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.backstreettoy.nullsafe.extra.jodd.typeconverter.TypeConverterManager;
import io.github.backstreettoy.nullsafe.impl.BoxUtil;
import io.github.backstreettoy.nullsafe.impl.Pair;
import io.github.backstreettoy.nullsafe.impl.config.WrapConfig;
import io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.AbstractFieldHandler;
import io.github.backstreettoy.nullsafe.impl.matchers.AbstractMatcher;


/**
 * @author backstreettoy
 */
public class MethodHandlerImpl<T> implements MethodHandler {

    private static final TypeConverterManager TYPE_CONVERTER_MANAGER =
            TypeConverterManager.get();
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandlerImpl.class);

//    private Map<Method, NullSafeWrapped> methodProxies;
    private Map<Method, String> getterToFieldMapping;
    private Map<Method, List<AbstractFieldHandler>> propertyfallbackHandlers;
    private List<Pair<AbstractMatcher, AbstractFieldHandler>> policies;
    private WrapConfig config;
    private Supplier<T> supplier;

    public MethodHandlerImpl(Supplier<T> supplier, List<Pair<AbstractMatcher, AbstractFieldHandler>> policies,
                             WrapConfig config, BeanInfo beanInfo) {
        this.supplier = supplier;
        this.policies = policies;
        this.config = config;
//        methodProxies = new HashMap<>();
        propertyfallbackHandlers = new HashMap<>();
        getterToFieldMapping = new HashMap<>();
        initProxy(policies, config, beanInfo);
    }

    private void initProxy(List<Pair<AbstractMatcher, AbstractFieldHandler>> policies, WrapConfig config, BeanInfo beanInfo) {
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod == null) {
                continue;
            }

            addGetterToMapping(readMethod, propertyDescriptor.getName());
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

    private void addGetterToMapping(Method readMethod, String name) {
        getterToFieldMapping.put(readMethod, name);
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
        thisMethod.setAccessible(true);
        Object result = thisMethod.invoke(referenceObj);
        if (!needProxy || result != null) {
            return result;
        }

        List<AbstractFieldHandler> handlers = propertyfallbackHandlers.get(thisMethod);
        if (handlers.isEmpty()) {
            return null;
        }

        String fieldName = getterToFieldMapping.get(thisMethod);
        for (AbstractFieldHandler handler : handlers) {
            try {
                AbstractFieldHandler.FallbackResult fallbackResult = handler.fallback(self, thisMethod, fieldName);
                Optional<?> holder = fallbackResult.get();
                if (holder == null) {
                    // Null value indicates this handler can not handle
                    continue;
                }
                return returnValueFromHolder(holder, thisMethod.getReturnType());
            } catch (UnableFallbackException e) {
                handleException(e);
                continue;
            }
        }
        return null;
    }

    private Object returnValueFromHolder(Optional<?> holder, Class<?> returnType) {
        Object value = holder.orElse(null);
        if (value == null) {
            return null;
        }

        // Check whether returnType could be assigned.
        int maxLoopCount = 10;
        while (maxLoopCount-- > 0) {
            Class<?> valueClazz = value.getClass();
            boolean assignable = returnType.isAssignableFrom(valueClazz);
            if (assignable) {
                return value;
            } else if (BoxUtil.isBoxedType(valueClazz, returnType)) {
                value = BoxUtil.box(value);
            } else if (BoxUtil.isUnboxedType(valueClazz, returnType)) {
                value = BoxUtil.unbox(value);
            } else if (config.isEnableConvert()) {
                // Try convert if enabled.
                try {
                    value = TYPE_CONVERTER_MANAGER.convertType(value, returnType);
                } catch (Exception e) {
                    throw new UnableFallbackException(e);
                }
            } else {
                throw new UnableFallbackException();
            }
        }
        throw new RuntimeException("UnExpected! Over max loop count!");
    }

    private void handleException(Exception e) {
        if (config.isSilent()) {
            LOG.error(e.toString(), e);
        } else if (e instanceof RuntimeException) {
            throw (RuntimeException)e;
        } else {
            throw new RuntimeException(e);
        }
    }

    /**
     * Thrown when fallback operation could not be complete.
     */
    private static class UnableFallbackException extends RuntimeException {
        public UnableFallbackException(Throwable cause) {
            super(cause);
        }

        public UnableFallbackException() {
        }
    }
}
