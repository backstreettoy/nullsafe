package io.github.backstreettoy.nullsafe;

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

   public static <T> T eval(T obj) {
       return SafeCallWrapper._eval(obj);
   }

}
