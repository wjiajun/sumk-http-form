package org.yx.sumk.form.resolver.param.match;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Loader;
import org.yx.sumk.form.bind.bean.FormBeanPropertyHandler;
import org.yx.sumk.form.bind.property.BeanPropertyHandlerVisitors;
import org.yx.sumk.form.resolver.param.MethodParameter;
import org.yx.sumk.form.resolver.param.ParamInfoResolver;
import org.yx.validate.Validators;

/**
 * 嵌套类型解析器
 * @author : wjiajun
 */
@Bean
public class ParamObjectResolver extends ParamInfoResolver {

    @Override
    public boolean match(MethodParameter param) {
        return Validators.supportComplex(param.getParamInfo().getParamType());
    }

    @Override
    public Object resolve(MethodParameter param) {
        BeanPropertyHandlerVisitors handlerFactory = IOC.get(BeanPropertyHandlerVisitors.class);
        FormBeanPropertyHandler handler = handlerFactory.createBeanPropertyHandler(instantiateClass(param.getParamInfo().getParamType()),
                BeanPropertyHandlerVisitors.FORM_BEAN_PROPERTY_HANDLER);

        return handler.parse(param);
    }

    @SuppressWarnings("unchecked")
    public static <T> T instantiateClass(Class<?> type) {
        try {
            return (T) Loader.newInstance(type);
        } catch (Exception ex) {
            throw new IllegalArgumentException("instantiateClass fail", ex);
        }
    }
}
