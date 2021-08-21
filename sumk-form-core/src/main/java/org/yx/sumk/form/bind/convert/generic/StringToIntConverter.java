package org.yx.sumk.form.bind.convert.generic;

/**
 * string -> int
 * @author : wjiajun
 */
public class StringToIntConverter implements GenericConverter {

    @Override
    public Object convert(Object value, Class<?> sourceType, Class<?> targetType) {
        return Integer.valueOf((String) value);
    }
}
