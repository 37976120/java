package com.htstar.ovms.pay.handler;

import java.util.Map;

/**
 * @author ovms
 * @date 2019-06-27
 * <p>
 * 支付回调处理器
 */
public interface PayNotifyCallbakHandler {

	/**
	 * 初始化执行
	 *
	 * @param params
	 */
	void before(Map<String, String> params);

	/**
	 * 去重处理
	 *
	 * @param params 回调报文
	 * @return
	 */
	Boolean duplicateChecker(Map<String, String> params);

	/**
	 * 验签逻辑
	 *
	 * @param params 回调报文
	 * @return
	 */
	Boolean verifyNotify(Map<String, String> params);

	/**
	 * 解析报文
	 *
	 * @param params
	 * @return
	 */
	String parse(Map<String, String> params);

	/**
	 * 调用入口
	 *
	 * @param params
	 * @return
	 */
	String handle(Map<String, String> params);

	/**
	 * 保存回调记录
	 *
	 * @param result 处理结果
	 * @param params 回调报文
	 */
	void saveNotifyRecord(Map<String, String> params, String result);
}
