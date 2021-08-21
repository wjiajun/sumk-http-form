package org.yx.sumk.form.bind.convert.generic;

/**
 * 通用类型转换接口
 * @author wjiajun
 */
public interface GenericConverter {

	/**
	 * 通用解析转换接口
	 *
	 * @param value 原值
	 * @param sourceType 原始类型
	 * @param targetType 目标类型
	 * @return
	 */
	Object convert(Object value, Class<?> sourceType, Class<?> targetType);
}