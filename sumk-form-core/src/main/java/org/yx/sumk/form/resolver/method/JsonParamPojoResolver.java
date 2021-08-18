package org.yx.sumk.form.resolver.method;

import org.yx.annotation.Bean;
import org.yx.http.handler.WebContext;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.sumk.form.annotation.JsonParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author : wjiajun
 * @description: json方法解析类
 */
@Bean
public class JsonParamPojoResolver implements ParamPojoResolver<WebContext, String> {

    @Override
    public boolean match(WebContext ctx) {
        String type;
        //json格式的参数
        if (ctx.node().getAnnotation(JsonParam.class) != null
                && (type = ctx.httpRequest().getContentType()) != null
                && type.toLowerCase().startsWith("application/json")) {
            return true;
        }
        return false;
    }

    @Override
    public String resolve(WebContext ctx) throws Exception {
        HttpServletRequest req = ctx.httpRequest();
        byte[] bs = InnerHttpUtil.extractData(req.getInputStream(), req.getContentLength());
        if (Objects.isNull(bs) || bs.length == 0) {
            return null;
        }
        return new String(bs, ctx.charset());
    }
}
