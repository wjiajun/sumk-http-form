package org.yx.sumk.form.resolver.param.match;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.sumk.form.bind.convert.TypeConverterProxy;
import org.yx.sumk.form.resolver.param.MethodParameter;
import org.yx.sumk.form.resolver.param.ParamInfoResolver;
import org.yx.validate.Validators;

import java.util.Objects;

/**
 * 普通类型解析器
 * @author : wjiajun
 */
@Bean
public class ParamResolver extends ParamInfoResolver {

    @Override
    public boolean match(MethodParameter param) {
        return !Validators.supportComplex(param.getParamInfo().getParamType());
    }

    @Override
    public Object resolve(MethodParameter param) {
        TypeConverterProxy typeConverterProxy = IOC.get(TypeConverterProxy.class);

        String[] parameterValues = param.getHttpServletRequest().getParameterValues(param.getParamInfo().getParamName());
        if (Objects.nonNull(parameterValues)) {
            Object value = parameterValues.length == 1 ? parameterValues[0] : parameterValues;
            return typeConverterProxy.convert(param.getParamInfo().getParamName(), value, param.getParamInfo().getParamType());
        }

        return null;
    }
}
