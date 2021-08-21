package org.yx.sumk.form.bind.convert.generic;

import org.yx.util.SumkDate;

/**
 * long -> sumkDate
 * @author : wjiajun
 */
public class LongToSumkDateConverter implements GenericConverter {

    @Override
    public Object convert(Object value, Class<?> sourceType, Class<?> targetType) {
        if(value instanceof String) {
            value = Long.valueOf((String) value);
        }
        return SumkDate.of((long) value);
    }
}
