package io.github.backstreettoy.nullsafe.impl.safecall;

import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public static final Map<Object, Object> NULL_OBJECTS;
    public static final Map<Class<?>, Object> PRIMITIVE_OBJECTS  = new HashMap<>();

    static {
        PRIMITIVE_OBJECTS.put(char.class, '0');
        PRIMITIVE_OBJECTS.put(byte.class, (byte)0);
        PRIMITIVE_OBJECTS.put(boolean.class, false);
        PRIMITIVE_OBJECTS.put(short.class, (short)0);
        PRIMITIVE_OBJECTS.put(int.class, 0);
        PRIMITIVE_OBJECTS.put(long.class, 0L);
        PRIMITIVE_OBJECTS.put(float.class, 0F);
        PRIMITIVE_OBJECTS.put(double.class, 0D);

        NULL_OBJECTS_MAPPING.put(Character.class, '0');
        NULL_OBJECTS_MAPPING.put(String.class, new String());
        NULL_OBJECTS_MAPPING.put(Byte.class, new Byte((byte)0));
        NULL_OBJECTS_MAPPING.put(Boolean.class, new Boolean(false));
        NULL_OBJECTS_MAPPING.put(Short.class, new Short((short)0));
        NULL_OBJECTS_MAPPING.put(Integer.class, new Integer(0));
        NULL_OBJECTS_MAPPING.put(Long.class, new Long(0));
        NULL_OBJECTS_MAPPING.put(Float.class, new Float(0));
        NULL_OBJECTS_MAPPING.put(Double.class, new Double(0));
        NULL_OBJECTS_MAPPING.put(Locale.class, Locale.getDefault().clone());
        NULL_OBJECTS_MAPPING.put(UUID.class, UUID.randomUUID());
        NULL_OBJECTS_MAPPING.put(Duration.class, Duration.ofDays(0));
        NULL_OBJECTS_MAPPING.put(Instant.class, Instant.now());
        NULL_OBJECTS_MAPPING.put(LocalDate.class, LocalDate.now());
        NULL_OBJECTS_MAPPING.put(LocalDateTime.class, LocalDateTime.now());
        NULL_OBJECTS_MAPPING.put(LocalTime.class, LocalTime.now());
        NULL_OBJECTS_MAPPING.put(MonthDay.class, MonthDay.now());
        NULL_OBJECTS_MAPPING.put(OffsetDateTime.class, OffsetDateTime.now());
        NULL_OBJECTS_MAPPING.put(OffsetTime.class, OffsetTime.now());
        NULL_OBJECTS_MAPPING.put(Period.class, Period.ofDays(0));
        NULL_OBJECTS_MAPPING.put(Year.class, Year.now());
        NULL_OBJECTS_MAPPING.put(YearMonth.class, YearMonth.now());
        NULL_OBJECTS_MAPPING.put(ZonedDateTime.class, ZonedDateTime.now());
        NULL_OBJECTS_MAPPING.put(ZoneOffset.class, ZoneOffset.ofHours(0));

        NULL_OBJECTS = new IdentityHashMap<>(NULL_OBJECTS_MAPPING.values().stream()
                .collect(Collectors.toMap(Function.identity(), Function.identity())));
    }
}
