package org.yx.sumk.form.bind.property;

import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * 表单 参数属性 list
 *
 * @author : wjiajun
 */
public class FormParamPropertyValues extends MutablePropertyValues {

    public FormParamPropertyValues(ServletRequest request) {
        super(formatRequest(request));
    }

    public static Map<String, Object> formatRequest(ServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] values = request.getParameterValues(paramName);
            if (values == null || values.length == 0) {
                // Do nothing, no values found at all.
            } else if (values.length > 1) {
                params.put(paramName, values);
            } else {
                params.put(paramName, values[0]);
            }
        }
        return params;
    }
}
