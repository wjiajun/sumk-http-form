package org.yx.sumk.form.resolver.method;

import org.yx.annotation.Bean;
import org.yx.asm.ParamPojo;
import org.yx.bean.Loader;
import org.yx.http.HttpJson;
import org.yx.http.handler.WebContext;
import org.yx.log.Log;
import org.yx.sumk.form.annotation.FormParam;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 表单格式参数解析
 *
 * @author : wjiajun
 * @description:
 */
@Bean
public class FormParamPojoResolver implements ParamPojoResolver<WebContext, String> {// todo <WebContext, String> refactor <WebContext, PojoParam>

    private ConcurrentMap<Class<? extends ParamPojo>, Field[]> cache = new ConcurrentHashMap<>();

    @Override
    public boolean match(WebContext ctx) {
        return ctx.node().getAnnotation(FormParam.class) != null;
    }

    @Override
    public String resolve(WebContext ctx) throws Exception {
        Class<? extends ParamPojo> clz = ctx.node().params().paramClz();
        Field[] fs = cache.get(clz);
        if (fs == null) {
            int i = 0;
            fs = new Field[ctx.node().params().paramLength()];
            for (String name : ctx.node().params().paramNames()) {
                Field f = clz.getDeclaredField(name);
                f.setAccessible(true);
                fs[i++] = f;
            }
            cache.putIfAbsent(clz, fs);
        }
        Object data = Loader.newInstance(clz);
        for (Field f : fs) {
            String httpParam = ctx.httpRequest().getParameter(f.getName());
            Log.get("sumk.http").debug("{}的参数{}={}", ctx.rawAct(), f.getName(), httpParam);
            Object value;
            // 字符串对特殊字符过滤问题
            if (f.getType() == String.class) {
                value = HttpJson.operator().fromJson(HttpJson.operator().toJson(httpParam), f.getType());
            } else {
                value = HttpJson.operator().fromJson(httpParam, f.getType());
            }
            f.set(data, value);
        }
        return HttpJson.operator().toJson(data);
    }
}
