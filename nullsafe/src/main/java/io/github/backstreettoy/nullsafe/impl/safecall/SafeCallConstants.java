package io.github.backstreettoy.nullsafe.impl.safecall;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.github.backstreettoy.nullsafe.impl.proxywrap.SafeCallWrapped;

/**
 * @author backstreettoy
 */
public class SafeCallConstants {

    public static final Class<?>[] INTERFACES = new Class<?>[] {
            SafeCallWrapped.class
    };

    public static final String PROXY_TARGET_OPERATE = "__impl";
    public static final String GET_PROXY_TARGET_OPERATE = "__getimpl";


    public static final Map<Class<?>, Object> NULL_OBJECTS_MAPPING = new HashMap<>();
    public static final Set<Object> NULL_OBJECTS;

    static {
        NULL_OBJECTS_MAPPING.put(String.class, new String());
        NULL_OBJECTS_MAPPING.put(Byte.class, new Byte((byte)0));
        NULL_OBJECTS_MAPPING.put(Boolean.class, new Boolean(false));
        NULL_OBJECTS_MAPPING.put(Short.class, new Short((short)0));
        NULL_OBJECTS_MAPPING.put(Integer.class, new Integer(0));

        NULL_OBJECTS = new HashSet<>(NULL_OBJECTS_MAPPING.values());
    }
}
