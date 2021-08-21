package org.yx.sumk.form.resolver.param;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.sumk.form.resolver.HandlerResolver;
import org.yx.sumk.form.resolver.ParamInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 方法参数解析门面
 *
 * @author : wjiajun
 */
@Bean
public class ParamInfoResolvers implements HandlerResolver<MethodParameter, Object>, Plugin {

    private final Map<Object, ParamInfoResolver> argumentResolverCache = new ConcurrentHashMap<>(256);

    private final List<ParamInfoResolver> argumentResolvers = new LinkedList<>();

    @Override
    public void startAsync() {
        buildParamInfoResolvers();
    }

    @Override
    public boolean match(MethodParameter param) {
        return Objects.nonNull(getArgumentResolver(param));
    }

    @Override
    public Object resolve(MethodParameter param) throws Exception {
        return getArgumentResolver(param).resolve(param);
    }

    private void buildParamInfoResolvers() {
        argumentResolvers.addAll(IOC.getBeans(ParamInfoResolver.class));
    }

    /**
     * 获取方法参数解析器
     *
     * @param param
     * @return
     */
    private ParamInfoResolver getArgumentResolver(MethodParameter param) {
        ParamInfo paramInfo = param.getParamInfo();
        ParamInfoResolver result = this.argumentResolverCache.get(paramInfo);
        if (result == null) {
            for (ParamInfoResolver resolver : this.argumentResolvers) {
                if (resolver.match(param)) {
                    result = resolver;
                    this.argumentResolverCache.put(paramInfo, result);
                    break;
                }
            }
        }
        return result;
    }
}
