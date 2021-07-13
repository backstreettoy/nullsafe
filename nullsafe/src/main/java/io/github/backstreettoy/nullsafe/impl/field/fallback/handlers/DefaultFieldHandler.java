package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

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
    public FallbackResult fallback() {
        return FallbackResult.fallback(value);
    }
}
