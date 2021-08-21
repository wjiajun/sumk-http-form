package org.yx.sumk.form.resolver;

import java.util.Objects;

/**
 * 方法参数解析传输类
 * @author wjiajun
 */
public class ParamInfo {

    private final String paramName;

    private final Class<?> paramType;

    public ParamInfo(String paramName, Class<?> type) {
        this.paramName = Objects.requireNonNull(paramName);
        this.paramType = type;
    }

    public String getParamName() {
        return paramName;
    }

    public Class<?> getParamType() {
        return paramType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParamInfo paramInfo = (ParamInfo) o;
        return paramName.equals(paramInfo.paramName) && paramType.equals(paramInfo.paramType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramName, paramType);
    }
}
