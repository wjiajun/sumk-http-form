package org.yx.sumk.form.resolver;

public interface HandlerResolver<IN, OUT> {

	/**
	 * 当前param是否支持该解析器
	 *
	 * @param param
	 * @return
	 */
	boolean match(IN param);

	/**
	 * 执行参数解析
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	OUT resolve(IN param) throws Exception;

}