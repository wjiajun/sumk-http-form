package org.yx.sumk.form.bind.property;

import org.yx.annotation.Bean;
import org.yx.sumk.form.bind.bean.BeanPropertyHandler;
import org.yx.sumk.form.bind.bean.FormBeanPropertyHandler;

/**
 * Bean处理器访问者
 *
 * @author : wjiajun
 */
@Bean
public class BeanPropertyHandlerVisitors {

    public interface PropertyVisitor<T> {
        /**
         * 属性处理器访问者
         * @param target
         *
         * @return T
         */
        T visit(Object target);
    }

    public static final PropertyVisitor<BeanPropertyHandler> BEAN_PROPERTY_HANDLER = BeanPropertyHandler::new;

    public static final PropertyVisitor<FormBeanPropertyHandler> FORM_BEAN_PROPERTY_HANDLER = FormBeanPropertyHandler::new;

    public final <T> T createBeanPropertyHandler(Object target, PropertyVisitor<T> visitor) {
        return visitor.visit(target);
    }
}
