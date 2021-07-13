package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

import java.util.Optional;

/**
 * @author backstreettoy
 */
public abstract class AbstractFieldHandler {

    public abstract FallbackResult fallback();

    public static class FallbackResult {
        Optional<?> result;

        private FallbackResult(Optional<?> data) {
            this.result = data;
        }

        /**
         * Use the fallback result instead of the original {@code null} result.
         * @param result
         * @return
         */
        public static FallbackResult fallback(Object result) {
            return new FallbackResult(Optional.ofNullable(result));
        }

        /**
         * Give up the current fallback procedure and invoke the next fallback procedure if it presents.
         * @return
         */
        public static FallbackResult pass() {
            return new FallbackResult(null);
        }

    }
}
