package io.github.backstreettoy.nullsafe.impl.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author backstreettoy
 */
public class BoxUtil {

    private static final Map<Class<?>, Class<?>> BOXED_TYPE;
    private static final Map<Class<?>, Class<?>> UNBOXED_TYPE;
    private static final Map<Class<?>, Function<Object, ?>> UNBOX;
    private static final Map<Class<?>, Function<Object, ?>> BOX;

    static {
        BOXED_TYPE = new HashMap<>();
        BOXED_TYPE.put(byte.class, Byte.class);
        BOXED_TYPE.put(short.class, Short.class);
        BOXED_TYPE.put(int.class, Integer.class);
        BOXED_TYPE.put(long.class, Long.class);
        BOXED_TYPE.put(float.class, Float.class);
        BOXED_TYPE.put(double.class, Double.class);
        BOXED_TYPE.put(boolean.class, Boolean.class);
        BOXED_TYPE.put(char.class, Character.class);

        BOX = new HashMap<>();
        BOX.put(byte.class, x -> new Byte((byte)x));
        BOX.put(short.class, x -> new Short((short)x));
        BOX.put(int.class, x -> new Integer((int)x));
        BOX.put(long.class, x -> new Long((long)x));
        BOX.put(float.class, x -> new Float((float)x));
        BOX.put(double.class, x -> new Double((double)x));
        BOX.put(boolean.class, x -> new Boolean((boolean)x));
        BOX.put(char.class, x -> new Character((char)x));

        UNBOXED_TYPE = new HashMap<>();
        UNBOXED_TYPE.put(Byte.class, byte.class);
        UNBOXED_TYPE.put(Short.class, short.class);
        UNBOXED_TYPE.put(Integer.class, int.class);
        UNBOXED_TYPE.put(Long.class, long.class);
        UNBOXED_TYPE.put(Float.class, float.class);
        UNBOXED_TYPE.put(Double.class, double.class);
        UNBOXED_TYPE.put(Boolean.class, boolean.class);
        UNBOXED_TYPE.put(Character.class, char.class);

        UNBOX = new HashMap<>();
        UNBOX.put(Byte.class, x -> ((Byte)x).byteValue());
        UNBOX.put(Short.class, x -> ((Short)x).shortValue());
        UNBOX.put(Integer.class, x -> ((Integer)x).intValue());
        UNBOX.put(Long.class, x -> ((Long)x).longValue());
        UNBOX.put(Float.class, x -> ((Float)x).floatValue());
        UNBOX.put(Double.class, x -> ((Double)x).doubleValue());
        UNBOX.put(Boolean.class, x -> ((Boolean)x).booleanValue());
        UNBOX.put(Character.class, x -> ((Character)x).charValue());
    }

    public static final boolean isBoxedType(Class<?> from, Class<?> to) {
        return from.isPrimitive()
                && BOXED_TYPE.containsKey(from) ? BOXED_TYPE.get(from).equals(to) : false;
    }

    public static final boolean isUnboxedType(Class<?> from, Class<?> to) {
        return to.isPrimitive()
                && UNBOXED_TYPE.containsKey(from) ? UNBOXED_TYPE.get(from).equals(to) : false;
    }

    public static final Object box(Object obj) {
        return BOX.get(obj.getClass()).apply(obj);
    }

    public static final Object unbox(Object obj) {
        return UNBOX.get(obj.getClass()).apply(obj);
    }
}
