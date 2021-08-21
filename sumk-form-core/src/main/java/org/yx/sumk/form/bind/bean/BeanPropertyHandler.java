package org.yx.sumk.form.bind.bean;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yx.bean.IOC;
import org.yx.sumk.form.bind.convert.TypeConverterProxy;
import org.yx.sumk.form.bind.property.MutablePropertyValues;
import org.yx.sumk.form.bind.property.PropertyValue;
import org.yx.util.S;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * bean属性处理类
 *
 * @author : wjiajun
 */
public class BeanPropertyHandler {

    private static final Logger logger = LoggerFactory.getLogger(BeanPropertyHandler.class);

    private final Object target;

    private final TypeConverterProxy typeConverterProxy;

    public BeanPropertyHandler(Object target) {
        this.target = target;
        typeConverterProxy = IOC.get(TypeConverterProxy.class);
    }

    public Object parse(MutablePropertyValues mutablePropertyValues) {
        List<PropertyValue> propertyValueList = mutablePropertyValues.getPropertyValueList();
        for (PropertyValue propertyValue : propertyValueList) {
            try {
                setPropertyValue(propertyValue.getName(), propertyValue.getValue());
            } catch (Exception e) {
                logger.info("bean parse exception", e);
            }
        }
        return target;
    }

    public void setPropertyValue(String propertyName, Object value) {
        // 转换
        Object convertValue = null;

        Optional<? extends Class<?>> type = Optional.ofNullable(findField(target.getClass(), propertyName)).map(Field::getType);
        if(type.isPresent()) {
            convertValue = typeConverterProxy.convert(propertyName, value, type.get());
        }

        setField(target, propertyName, convertValue);
    }

    private void setField(Object target, String propertyName, Object value) {
        Field field = findField(target.getClass(), propertyName);
        Optional.ofNullable(field).ifPresent(f -> {
            try {
                FieldUtils.writeField(f, target, value);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(String.format("class: %s name: %s e: %s", target.getClass(), f.getName(), e));
            }
        });
    }

    private static Field findField(Class<?> clazz, String name) {
        Field[] fields = S.bean().getFields(clazz);
        for (Field field : fields) {
            if ((Objects.isNull(name) || name.equals(field.getName()))) {
                return field;
            }
        }
        return null;
    }
}
