package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

/**
 * @author backstreettoy
 */
public class RaiseExceptionFieldHandler extends AbstractFieldHandler {

    @Override
    public boolean raiseException() {
        return true;
    }

    @Override
    public Object fallback() {
        return null;
    }
}
