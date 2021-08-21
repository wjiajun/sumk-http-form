package org.yx.sumk.form.bind.bean;

import org.yx.sumk.form.bind.property.FormParamPropertyValues;
import org.yx.sumk.form.bind.property.MutablePropertyValues;
import org.yx.sumk.form.resolver.param.MethodParameter;

/**
 * bean 表单属性处理类
 * @author : wjiajun
 */
public class FormBeanPropertyHandler extends BeanPropertyHandler{

    public FormBeanPropertyHandler(Object target) {
        super(target);
    }

    public Object parse(MethodParameter methodParameter) {
        MutablePropertyValues propertyValues =
                new FormParamPropertyValues(methodParameter.getHttpServletRequest());
        return super.parse(propertyValues);
    }
}
