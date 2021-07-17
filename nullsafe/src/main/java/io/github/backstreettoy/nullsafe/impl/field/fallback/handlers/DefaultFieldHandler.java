package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

import java.lang.reflect.Method;

/**
 *
 * @author backstreettoy
 */
public class DefaultFieldHandler extends AbstractFieldHandler {

    private Object value;

    public DefaultFieldHandler(Object value) {
        this.value = value;
    }

    @Override
    public FallbackResult fallback(Object obj, Method getter, String fieldName) {
        return FallbackResult.of(value);
    }
}
