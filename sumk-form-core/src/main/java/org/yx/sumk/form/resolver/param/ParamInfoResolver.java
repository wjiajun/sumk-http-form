package org.yx.sumk.form.resolver.param;

import org.yx.annotation.Bean;
import org.yx.sumk.form.resolver.HandlerResolver;

/**
 * @author : wjiajun
 * @description:
 */
@Bean
public class ParamInfoResolver implements HandlerResolver<Object, Object> {

    @Override
    public boolean match(Object param) {
        return false;
    }

    @Override
    public Object resolve(Object param) throws Exception {
        return null;
    }
}
