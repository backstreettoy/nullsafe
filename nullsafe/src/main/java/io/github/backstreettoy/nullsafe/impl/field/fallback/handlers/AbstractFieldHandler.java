package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

/**
 * @author backstreettoy
 */
public abstract class AbstractFieldHandler {

    /**
     * Indicator of raising an {@code EnrichedNullPointerException}
     * @return
     */
    public abstract boolean raiseException();

    public abstract Object fallback();
}
