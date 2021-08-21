package org.yx.sumk.form.bind.property;

import java.io.Serializable;

/**
 * 属性封装类
 * @author : wjiajun
 */
public class PropertyValue implements Serializable {

    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
