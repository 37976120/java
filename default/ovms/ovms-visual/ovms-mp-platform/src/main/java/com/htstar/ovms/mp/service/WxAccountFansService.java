
package com.htstar.ovms.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.mp.entity.WxAccountFans;

/**
 * 微信公众号粉丝
 *
 * @author ovms
 * @date 2019-03-26 22:08:08
 */
public interface WxAccountFansService extends IService<WxAccountFans> {

	/**
	 * 同步指定公众号粉丝
	 *
	 * @param appId
	 * @return
	 */
	Boolean syncAccountFans(String appId);
}
