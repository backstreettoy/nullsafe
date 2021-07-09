package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

import java.util.function.Supplier;

/**
 * @author backstreettoy
 */
public class SupplierFieldHandler extends AbstractFieldHandler {

    private Supplier<?> supplier;

    public SupplierFieldHandler(Supplier<?> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean raiseException() {
        return false;
    }

    @Override
    public Object fallback() {
         return supplier.get();
    }
}
