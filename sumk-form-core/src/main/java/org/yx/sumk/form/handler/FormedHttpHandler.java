package org.yx.sumk.form.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.yx.annotation.Bean;
import org.yx.asm.ParamPojo;
import org.yx.bean.Loader;
import org.yx.http.HttpJson;
import org.yx.http.handler.AbstractHttpHandler;
import org.yx.http.handler.WebContext;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.log.Log;
import org.yx.sumk.form.annotation.JsonParam;
import org.yx.sumk.form.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Bean
public class FormedHttpHandler extends AbstractHttpHandler {

    private ConcurrentMap<Class<? extends ParamPojo>, Field[]> cache = new ConcurrentHashMap<>();

    private String getParameter(JsonObject j, String name) {
        JsonElement v = j.get(name);
        return v == null ? null : v.getAsString();
    }

    @Override
    public void handle(WebContext ctx) throws Throwable {
        Class<? extends ParamPojo> clz = ctx.node().params().paramClz();

        String type;
        //json格式的参数
        if (ctx.node().getAnnotation(JsonParam.class) != null
                && (type = ctx.httpRequest().getContentType()) != null
                && type.toLowerCase().startsWith("application/json")) {
            HttpServletRequest req = ctx.httpRequest();
            byte[] bs = InnerHttpUtil.extractData(req.getInputStream(), req.getContentLength());
            if (bs == null || bs.length == 0) {
                this.setData(ctx, bs);
                return;
            }
            String json = new String(bs, ctx.charset());
            this.setData(ctx, json);
            return;
        }

        //字符串格式的参数
        if (ctx.node().getAnnotation(RequestBody.class) != null) {
            HttpServletRequest req = ctx.httpRequest();
            byte[] bs = InnerHttpUtil.extractData(req.getInputStream(), req.getContentLength());
            JsonObject jo = new JsonObject();
            jo.addProperty("body", new String(bs, ctx.charset()));
            this.setData(ctx, jo.toString());
            return;
        }

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
        this.setData(ctx, HttpJson.operator().toJson(data));
    }

    @Override
    public int order() {
        return 1299;
    }

}
