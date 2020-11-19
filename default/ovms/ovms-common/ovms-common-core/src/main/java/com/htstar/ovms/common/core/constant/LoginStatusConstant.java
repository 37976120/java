package com.htstar.ovms.common.core.constant;

/**
 * Description: 登录状态
 * Author: flr
 * Date: 2020/7/15 14:46
 * Company: 航通星空
 * Modified By:
 */
public interface LoginStatusConstant {
	int OK = 0;
	/**
	 * 您所在企业企业已被冻结
	 */
	int ETP_FROZEN = 1001;

	/**
	 * 您的账户已被冻结，请联系企业管理员
	 */
	int USER_FROZEN = 1002;

	/**
	 * 您所在企业对平台的使用时间已过期
	 */
	int ETP_EXPIRE = 1003;
}
