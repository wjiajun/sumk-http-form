package org.yx.sumk.form.bind.convert.generic;

import org.yx.util.S;

/**
 * string -> map
 * @author : wjiajun
 */
public class StringToMapConverter implements GenericConverter {

    @Override
    public Object convert(Object value, Class<?> sourceType, Class<?> targetType) {
        return S.json().fromJson((String) value, targetType);
    }
}
