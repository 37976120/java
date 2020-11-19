package com.htstar.ovms.admin.service;

import com.htstar.ovms.common.core.util.R;

/**
 * @author ovms
 * @date 2018/11/14
 */
public interface MobileService {
	/**
	 * 发送手机验证码
	 *
	 * @param mobile mobile
	 * @return code
	 */
	R<Boolean> sendSmsCodeForExistUser(String mobile);

}
