package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

import java.lang.reflect.Method;

/**
 * @author backstreettoy
 */
public class SafeCallHandler extends AbstractFieldHandler {
    @Override
    public FallbackResult fallback(Object obj, Method getter, String fieldName) {
        return null;
    }
}
