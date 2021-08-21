package org.yx.sumk.form.bind.convert;

import org.yx.sumk.form.bind.convert.generic.GenericConverter;
import org.yx.sumk.form.bind.convert.generic.LongToSumkDateConverter;
import org.yx.sumk.form.bind.convert.generic.StringToIntConverter;
import org.yx.sumk.form.bind.convert.generic.StringToListConverter;
import org.yx.sumk.form.bind.convert.generic.StringToMapConverter;
import org.yx.sumk.form.bind.convert.generic.StringToSumkDateConverter;
import org.yx.util.SumkDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用类型转换工具
 * @author wjiajun
 */
public class Converters {

    public static GenericConverter find(Class<?> sourceType, Class<?> targetType) {
        return RegistryConverter.getConverter(sourceType, targetType);
    }

    /**
     * 通用类型转换注册
     */
    private enum RegistryConverter {

        /**
         * long -> sumdate 
         */
        LONG_SUMK_DATE(Long.class, SumkDate.class, new LongToSumkDateConverter()),
        LONG_PRIMITIVE_SUMK_DATE(long.class, SumkDate.class, new LongToSumkDateConverter()),

        /**
         * string -> sumdate
         */
        STRING_SUMK_DATE(String.class, SumkDate.class, new StringToSumkDateConverter()),

        /**
         * string -> List
         */
        STRING_LIST(String.class, List.class, new StringToListConverter()),

        /**
         * string -> map
         */
        STRING_MAP(String.class, Map.class, new StringToMapConverter()),

        /**
         * string -> int
         */
        STRING_INT(String.class, Integer.class, new StringToIntConverter()),
        STRING_INT_PRIMITIVE(String.class, int.class, new StringToIntConverter()),
        ;

        private final Class<?> sourceType;
        private final Class<?> targetType;
        private final GenericConverter converter;

        private static final Map<String, GenericConverter> CONVERTER_MAP = new HashMap<>();

        static {
            for (RegistryConverter registryConverter : RegistryConverter.values()) {
                CONVERTER_MAP.put(registryConverter.sourceType.getTypeName() + registryConverter.targetType.getTypeName(), registryConverter.converter);
            }
        }

        RegistryConverter(Class<?> sourceType, Class<?> targetType, GenericConverter converter) {
            this.sourceType = sourceType;
            this.targetType = targetType;
            this.converter = converter;
        }

        public static GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
            return CONVERTER_MAP.get(sourceType.getTypeName() + targetType.getTypeName());
        }
    }
}