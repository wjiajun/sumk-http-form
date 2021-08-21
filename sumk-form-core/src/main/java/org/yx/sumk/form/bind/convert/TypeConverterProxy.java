package org.yx.sumk.form.bind.convert;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yx.annotation.Bean;
import org.yx.annotation.Inject;
import org.yx.util.StringUtil;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author : wjiajun
 * @description:
 */
@Bean
public class TypeConverterProxy {

    private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);

    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    private static final Logger logger = LoggerFactory.getLogger(TypeConverterProxy.class);

    @Inject
    private ConversionService conversionService;

    @SuppressWarnings("unchecked")
    public <T> T convert(String propertyName, Object value, Class<T> requiredType) {
        if (requiredType == Object.class) {
            return (T) value;
        } else if (requiredType.isArray()) {
            if (value instanceof String && Enum.class.isAssignableFrom(requiredType.getComponentType())) {
                value = StringUtil.splitAndTrim((String) value, ",");
            }
            return (T) convertToTypedArray(value, propertyName, requiredType);
        } else if (value instanceof Collection) {
            value = convertToTypedCollection((Collection<?>) value, propertyName, requiredType);
        } else if (value instanceof Map) {
            value = convertToTypedMap((Map<?, ?>) value, propertyName, requiredType);
        }

        if (String.class == requiredType && ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
            return (T) value.toString();
        } else if (value instanceof Number && Number.class.isAssignableFrom(requiredType)) {
            value = convertNumberToTargetClass((Number) value, (Class<Number>) requiredType);
        }

        if (!Objects.equals(value.getClass(), requiredType)) {
            value = conversionService.resolve(value, requiredType);
        }
        return (T) value;
    }

    private Object convertToTypedArray(Object input, String propertyName, Class<?> componentType) {
        if (input instanceof Collection) {
            // 集合 -> array
            Collection<?> coll = (Collection<?>) input;
            Object result = Array.newInstance(componentType, coll.size());
            int i = 0;
            for (Iterator<?> it = coll.iterator(); it.hasNext(); i++) {
                Object value = convert(buildIndexedPropertyName(propertyName, i), it.next(), componentType);
                Array.set(result, i, value);
            }
            return result;
        } else if (input.getClass().isArray()) {
            // array -> array
            if (componentType.equals(input.getClass().getComponentType())) {
                return input;
            }
            int arrayLength = Array.getLength(input);
            Object result = Array.newInstance(componentType, arrayLength);
            for (int i = 0; i < arrayLength; i++) {
                Object value = convert(buildIndexedPropertyName(propertyName, i), Array.get(input, i), componentType);
                Array.set(result, i, value);
            }
            return result;
        } else {
            // plain value -> array
            Object result = Array.newInstance(componentType, 1);
            Object value = convert(buildIndexedPropertyName(propertyName, 0), input, componentType);
            Array.set(result, 0, value);
            return result;
        }
    }

    private Collection<?> convertToTypedCollection(Collection<?> original, String propertyName, Class<?> requiredType) {
        if (!Collection.class.isAssignableFrom(requiredType)) {
            return original;
        }

        if (requiredType.isInstance(original)) {
            return original;
        }

        Iterator<?> it;
        try {
            it = original.iterator();
        } catch (Throwable ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot access Collection of type [" + original.getClass().getName() +
                        "] - injecting original Collection as-is: " + ex);
            }
            return original;
        }

        Collection<Object> convertedCopy = createCollection(original, original.size());
        for (int i = 0; it.hasNext(); i++) {
            Object element = it.next();
            String indexedPropertyName = buildIndexedPropertyName(propertyName, i);
            Object convertedElement = convert(indexedPropertyName, element, requiredType);
            try {
                convertedCopy.add(convertedElement);
            } catch (Throwable ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Collection type [" + original.getClass().getName() +
                            "] seems to be read-only - injecting original Collection as-is: " + ex);
                }
                return original;
            }
        }
        return convertedCopy;
    }

    @SuppressWarnings("unchecked")
    private Map<?, ?> convertToTypedMap(Map<?, ?> original, String propertyName, Class<?> requiredType) {
        if (!Map.class.isAssignableFrom(requiredType)) {
            return original;
        }

        if (requiredType.isInstance(original)) {
            return original;
        }

        Iterator<?> it;
        try {
            it = original.entrySet().iterator();
        } catch (Throwable ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot access Collection of type [" + original.getClass().getName() +
                        "] - injecting original Collection as-is: " + ex);
            }
            return original;
        }

        Map<Object, Object> convertedCopy = createMap(original, original.size());

        while (it.hasNext()) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            String keyedPropertyName = buildKeyedPropertyName(propertyName, key);
            Object convertedKey = convert(keyedPropertyName, key, Object.class);
            Object convertedValue = convert(keyedPropertyName, value, Object.class);
            try {
                convertedCopy.put(convertedKey, convertedValue);
            } catch (Throwable ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Map type [" + original.getClass().getName() +
                            "] seems to be read-only - injecting original Map as-is: " + ex);
                }
                return original;
            }
        }
        return convertedCopy;
    }

    private String buildIndexedPropertyName(String propertyName, int index) {
        return (Objects.nonNull(propertyName) ? propertyName + "[" + index + "]" : null);
    }

    private String buildKeyedPropertyName(String propertyName, Object key) {
        return (Objects.nonNull(propertyName) ? propertyName + "[" + key + "]" : null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass)
            throws IllegalArgumentException {

        if (targetClass.isInstance(number)) {
            return (T) number;
        } else if (Byte.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) Byte.valueOf(number.byteValue());
        } else if (Short.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) Short.valueOf(number.shortValue());
        } else if (Integer.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) Integer.valueOf(number.intValue());
        } else if (Long.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            return (T) Long.valueOf(value);
        } else if (BigInteger.class == targetClass) {
            if (number instanceof BigDecimal) {
                return (T) ((BigDecimal) number).toBigInteger();
            } else {
                return (T) BigInteger.valueOf(number.longValue());
            }
        } else if (Float.class == targetClass) {
            return (T) Float.valueOf(number.floatValue());
        } else if (Double.class == targetClass) {
            return (T) Double.valueOf(number.doubleValue());
        } else if (BigDecimal.class == targetClass) {
            return (T) new BigDecimal(number.toString());
        } else {
            throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                    number.getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]");
        }
    }

    private static long checkedLongValue(Number number, Class<? extends Number> targetClass) {
        BigInteger bigInt = null;
        if (number instanceof BigInteger) {
            bigInt = (BigInteger) number;
        } else if (number instanceof BigDecimal) {
            bigInt = ((BigDecimal) number).toBigInteger();
        }
        if (bigInt != null && (bigInt.compareTo(LONG_MIN) < 0 || bigInt.compareTo(LONG_MAX) > 0)) {
            raiseOverflowException(number, targetClass);
        }
        return number.longValue();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <K, V> Map<K, V> createMap(Object map, int capacity) {
        if (map instanceof EnumMap) {
            EnumMap enumMap = new EnumMap((EnumMap) map);
            enumMap.clear();
            return enumMap;
        } else if (map instanceof SortedMap) {
            return new TreeMap<>(((SortedMap<K, V>) map).comparator());
        } else {
            return new LinkedHashMap<>(capacity);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked", "cast"})
    public static <E> Collection<E> createCollection(Object collection, int capacity) {
        if (collection instanceof LinkedList) {
            return new LinkedList<>();
        } else if (collection instanceof List) {
            return new ArrayList<>(capacity);
        } else if (collection instanceof EnumSet) {
            Collection<E> enumSet = (Collection<E>) EnumSet.copyOf((EnumSet) collection);
            enumSet.clear();
            return enumSet;
        } else if (collection instanceof SortedSet) {
            return new TreeSet<>(((SortedSet<E>) collection).comparator());
        } else {
            return new LinkedHashSet<>(capacity);
        }
    }

    private static void raiseOverflowException(Number number, Class<?> targetClass) {
        throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
    }
}