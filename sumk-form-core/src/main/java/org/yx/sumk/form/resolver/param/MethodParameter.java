package org.yx.sumk.form.resolver.param;

import org.yx.sumk.form.resolver.ParamInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * ParamPojo 方法参数封装类
 * @author : wjiajun
 */
public class MethodParameter {

    private ParamInfo paramInfo;

    private HttpServletRequest httpServletRequest;

    public static MethodParameter build(ParamInfo paramInfo, HttpServletRequest httpServletRequest) {
        MethodParameter methodParameter = new MethodParameter();
        methodParameter.setParamInfo(paramInfo);
        methodParameter.setHttpServletRequest(httpServletRequest);
        return methodParameter;
    }

    public ParamInfo getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(ParamInfo paramInfo) {
        this.paramInfo = paramInfo;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }
}
