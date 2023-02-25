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
     * Evaluating the real value of obj. If obj is a wrapped object then the real value will be evaluated,
     * otherwise return obj itself.
     * @param obj
     * @param <T> The type of obj.
     * @return The evaluated real value.
     */
   public static <T> T eval(T obj) {
       return SafeCallWrapper.evaluate(obj);
   }

    /**
     * Evaluating the real value of obj and do the predication on the real value.
     * This method will fail when the real value is null and the overloaded method {@link NullSafe#evalMatch(Object, Predicate, Boolean)}
     * could return a default value instead of throwing exception.
     * @param obj
     * @param predicate
     * @param <T>
     * @return
     */
   public static <T> boolean evalMatch(T obj, Predicate<T> predicate) {
        return SafeCallWrapper.evaluateMatch(obj, predicate);
   }

    /**
     * Evaluating the real value of obj and do the predication on the real value. If the real value is null
     * then the defaultValue will be returned.
     * @param obj
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> Boolean evalMatch(T obj, Predicate<T> predicate, Boolean defaultValue) {
        return SafeCallWrapper.evaluateMatchWithDefault(obj, predicate, defaultValue);
    }

}
