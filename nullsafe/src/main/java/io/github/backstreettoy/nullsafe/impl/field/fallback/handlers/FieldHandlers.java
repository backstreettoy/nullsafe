package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

import java.util.function.Supplier;

/**
 * @author backstreettoy
 */
public final class FieldHandlers {

    /**
     * Create a custom handler that handles of of by custom.
     * @param callback
     * @return
     */
    public static CustomHandler custom(CustomHandler.CustomHandlerCallback callback) {
        return new CustomHandler(callback);
    }

    /**
     * Create a handler with a default of of.
     * @param value
     * @return
     */
    public static DefaultFieldHandler value(Object value) {
        return new DefaultFieldHandler(value);
    }

    /**
     * Create a handler with a supplier.
     * @param supplier
     * @return
     */
    public static SupplierFieldHandler valueGet(Supplier<AbstractFieldHandler.FallbackResult> supplier) {
        return new SupplierFieldHandler(supplier);
    }
}
