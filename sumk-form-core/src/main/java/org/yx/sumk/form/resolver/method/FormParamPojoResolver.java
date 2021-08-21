package org.yx.sumk.form.resolver.method;

import org.yx.annotation.Bean;
import org.yx.asm.ParamPojo;
import org.yx.bean.IOC;
import org.yx.exception.BizException;
import org.yx.http.HttpErrorCode;
import org.yx.http.HttpJson;
import org.yx.http.act.HttpActionNode;
import org.yx.http.handler.WebContext;
import org.yx.log.Logs;
import org.yx.sumk.form.annotation.FormParam;
import org.yx.sumk.form.resolver.ParamInfo;
import org.yx.sumk.form.resolver.param.MethodParameter;
import org.yx.sumk.form.resolver.param.ParamInfoResolvers;
import org.yx.util.S;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 表单格式参数解析
 * @author : wjiajun
 */
@Bean
public class FormParamPojoResolver implements ParamPojoResolver<WebContext, ParamPojo> {

    @Override
    public boolean match(WebContext ctx) {
        return Objects.nonNull(ctx.node().getAnnotation(FormParam.class));
    }

    @Override
    public ParamPojo resolve(WebContext ctx) throws Exception {
        Map<String, Object> args = new HashMap<>();
        ParamInfoResolvers paramInfoResolvers = IOC.get(ParamInfoResolvers.class);
        for (ParamInfo paramInfo : buildParamInfo(ctx.node())) {
            MethodParameter methodParameter = MethodParameter.build(paramInfo, ctx.httpRequest());

            if (paramInfoResolvers.match(methodParameter)) {
                args.put(paramInfo.getParamName(), paramInfoResolvers.resolve(methodParameter));
                continue;
            }

            if (Objects.isNull(args.get(paramInfo.getParamName()))) {
                throw new IllegalStateException(String.format("Could not resolve method parameter name %s, action name %s", paramInfo.getParamName(), ctx.node().cnName()));
            }
        }

        try {
            return HttpJson.operator().fromJson(S.json().toJson(args), ctx.node().params().paramClz());
        } catch (Exception e) {
            Logs.http().warn("json解析异常", e);
            throw BizException.create(HttpErrorCode.DATA_FORMAT_ERROR, "数据格式错误");
        }
    }

    private List<ParamInfo> buildParamInfo(HttpActionNode node) {
        List<String> params = node.params().paramNames();
        List<Type> paramTypes = node.params().paramTypes();

        return IntStream.range(0, node.paramLength())
                .mapToObj(i -> new ParamInfo(params.get(i), parseClass(paramTypes.get(i))))
                .collect(Collectors.toList());
    }

    private Class<?> parseClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            return parseClass(rawType);
        }
        return type.getClass();
    }
}
