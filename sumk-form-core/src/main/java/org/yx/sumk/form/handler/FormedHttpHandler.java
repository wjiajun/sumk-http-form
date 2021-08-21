package org.yx.sumk.form.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.http.handler.AbstractHttpHandler;
import org.yx.http.handler.WebContext;
import org.yx.sumk.form.resolver.method.ParamPojoResolver;
import org.yx.sumk.form.resolver.method.ParamPojoResolvers;

/**
 * 表单处理器
 * @author wjiajun
 */
@Bean
public class FormedHttpHandler extends AbstractHttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(FormedHttpHandler.class);

    @Override
    public void handle(WebContext ctx) throws Throwable {
        // 方法参数解析
        ParamPojoResolvers paramPojoResolvers = IOC.get(ParamPojoResolvers.class);
        for (ParamPojoResolver<WebContext, Object> paramPojoResolver : paramPojoResolvers.getParamPojoResolvers()) {
            if(paramPojoResolver.match(ctx)) {
                this.setData(ctx, paramPojoResolver.resolve(ctx));
                return;
            }
        }

        logger.info("使用默认json解析器...");
    }

    @Override
    public int order() {
        return 1299;
    }

}
