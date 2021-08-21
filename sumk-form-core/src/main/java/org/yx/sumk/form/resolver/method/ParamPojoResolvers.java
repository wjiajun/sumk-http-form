package org.yx.sumk.form.resolver.method;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.db.sql.DBSettings;
import org.yx.http.handler.WebContext;

import java.util.Collections;
import java.util.List;

/**
 * 参数类型解析门面
 * @author : wjiajun
 */
@Bean
public class ParamPojoResolvers implements Plugin {

    List<ParamPojoResolver<WebContext, Object>> resolvers = Collections.emptyList();

    @Override
    public void prepare() {
        DBSettings.init();
        buildParamPojoResolvers();
    }

    @Override
    public void startAsync() {
        // nothing
    }

    public List<ParamPojoResolver<WebContext, Object>> getParamPojoResolvers() {
        return resolvers;
    }

    private void buildParamPojoResolvers() {
        List beans = IOC.getBeans(ParamPojoResolver.class);
        resolvers = beans;
    }
}
