package io.github.backstreettoy.nullsafe.impl.field.fallback.handlers;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author backstreettoy
 */
public abstract class AbstractFieldHandler {

    public abstract FallbackResult fallback(Object obj, Method getter, String fieldName);

    public static class FallbackResult {
        Optional<?> result;

        private FallbackResult(Optional<?> data) {
            this.result = data;
        }

        /**
         * Use the non-null result instead of the original {@code null} result.
         * @param result
         * @return
         */
        public static FallbackResult of(Object result) {
            return new FallbackResult(Optional.of(result));
        }

        /**
         * Use the result instead of the original {@code null} result.
         * @param result
         * @return
         */
        public static FallbackResult ofNullable(Object result) {
            return new FallbackResult(Optional.ofNullable(result));
        }

        /**
         * Give up the current of procedure and invoke the next of procedure if it presents.
         * @return
         */
        public static FallbackResult pass() {
            return new FallbackResult(null);
        }

        public Optional<?> get() {
            return result;
        }

    }
}
