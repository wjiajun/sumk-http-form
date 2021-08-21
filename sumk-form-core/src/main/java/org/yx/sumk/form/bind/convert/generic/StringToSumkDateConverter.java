package org.yx.sumk.form.bind.convert.generic;

import org.yx.util.SumkDate;

/**
 * string -> sumkdate
 * @author : wjiajun
 */
public class StringToSumkDateConverter implements GenericConverter {

    @Override
    public Object convert(Object value, Class<?> sourceType, Class<?> targetType) {
        try {
            long date = Long.parseLong((String) value);
            return SumkDate.of(date);
        } catch (Exception ignore) {
        }
        return SumkDate.of((String) value);
    }
}
