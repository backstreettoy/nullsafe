package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * @author backstreettoy
 */
public class SupplierFieldHandler extends AbstractFieldHandler {

    private Supplier<FallbackResult> supplier;

    public SupplierFieldHandler(Supplier<FallbackResult> supplier) {
        this.supplier = supplier;
    }

    @Override
    public FallbackResult fallback(Object obj, Method getter, String fieldName) {
         return supplier.get();
    }

}
