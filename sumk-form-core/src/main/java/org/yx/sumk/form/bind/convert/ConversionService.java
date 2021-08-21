package org.yx.sumk.form.bind.convert;

import org.yx.annotation.Bean;
import org.yx.sumk.form.bind.convert.generic.GenericConverter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用类型转换service
 * @author : wjiajun
 */
@Bean
public class ConversionService {

    private static final GenericConverter NO_MATCH = new NoOpConverter("NO_MATCH");

    private final Map<ConverterCacheKey, GenericConverter> converterCache = new ConcurrentHashMap<>(64);

    @SuppressWarnings("unchecked")
    public <T> T resolve(Object value, Class<T> requiredType) {
        GenericConverter converter = getConverter(value.getClass(), requiredType);
        if (converter != null) {
            return (T) converter.convert(value, value.getClass(), requiredType);
        }
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        ConverterCacheKey key = new ConverterCacheKey(sourceType, targetType);
        GenericConverter converter = this.converterCache.get(key);
        if (converter != null) {
            return (converter != NO_MATCH ? converter : null);
        }

        converter = Converters.find(sourceType, targetType);
        if (converter == null) {
            converter = NO_MATCH;
        }

        this.converterCache.put(key, converter);
        return converter;
    }

    private static final class ConverterCacheKey implements Comparable<ConverterCacheKey> {

        private final Class<?> sourceType;

        private final Class<?> targetType;

        public ConverterCacheKey(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ConverterCacheKey)) {
                return false;
            }
            ConverterCacheKey otherKey = (ConverterCacheKey) other;
            return (this.sourceType.equals(otherKey.sourceType)) &&
                    this.targetType.equals(otherKey.targetType);
        }

        @Override
        public int hashCode() {
            return (this.sourceType.hashCode() * 29 + this.targetType.hashCode());
        }

        @Override
        public String toString() {
            return ("ConverterCacheKey [sourceType = " + this.sourceType +
                    ", targetType = " + this.targetType + "]");
        }

        @Override
        public int compareTo(ConverterCacheKey other) {
            int result = this.sourceType.toString().compareTo(other.sourceType.toString());
            if (result == 0) {
                result = this.targetType.toString().compareTo(other.targetType.toString());
            }
            return result;
        }
    }

    private static class NoOpConverter implements GenericConverter {

        private final String name;

        public NoOpConverter(String name) {
            this.name = name;
        }

        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            return source;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
