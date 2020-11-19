package com.alibaba.nacos.console.config;

/**
 * @author ovms
 * @date 2019-10-31
 * <p>
 * 覆盖nacos 默认配置
 */
public interface ConfigConstants {
	/**
	 * The System property name of  Standalone mode
	 */
	String STANDALONE_MODE = "nacos.standalone";

	/**
	 * 是否开启认证
	 */
	String AUTH_ENABLED = "nacos.core.auth.enabled";
}
