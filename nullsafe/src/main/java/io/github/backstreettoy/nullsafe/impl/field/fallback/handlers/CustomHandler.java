package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

import java.lang.reflect.Method;

/**
 * @author backstreettoy
 */
public class CustomHandler extends AbstractFieldHandler {

    private CustomHandlerCallback callback;

    public CustomHandler(CustomHandlerCallback callback) {
        this.callback = callback;
    }

    @Override
    public FallbackResult fallback(Object obj, Method getter, String fieldName) {
        return callback.fallback(obj, getter, fieldName);
    }

    public interface CustomHandlerCallback {

        FallbackResult fallback(Object obj, Method getter, String fieldName);
    }
}
