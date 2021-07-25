package io.github.backstreettoy.nullsafe;

import java.util.function.Predicate;

import io.github.backstreettoy.nullsafe.impl.safecall.SafeCallWrapper;

/**
 * The facade class of nullsafe utility
 * @author backstreettoy
 */
public final class NullSafe {

   public static <T> SafeCallWrapper<T> safeCall(T obj) {
       SafeCallWrapper<T> wrapper = new SafeCallWrapper(obj);
       return wrapper;
   }

    /**
     * Evaluate the real value of obj. If obj is a wrapped object then the real value will be evaluated,
     * otherwise return obj itself.
     * @param obj
     * @param <T> The type of obj.
     * @return The evaluated real value.
     */
   public static <T> T eval(T obj) {
       return SafeCallWrapper.evaluate(obj);
   }

    /**
     * Evaluate the real value of obj and verify whether the real value match the given predicate.
     * @param obj
     * @param predicate
     * @param <T>
     * @return
     */
   public static <T> boolean evalMatch(T obj, Predicate<T> predicate) {
        return SafeCallWrapper.evaluateMatch(obj, predicate);
   }

    /**
     * Evaluate the real value of obj, and if the real value is null then {@false} will be returned,
     * otherwise return the test result whether it match the given predicate.
     * @param obj
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> boolean evalNotNullThenMatch(T obj, Predicate<T> predicate) {
        return SafeCallWrapper.evaluateNotNullThenMatch(obj, predicate);
    }

}
