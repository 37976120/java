
package com.htstar.ovms.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.mp.entity.WxAccount;

/**
 * 公众号账户
 *
 * @author ovms
 * @date 2019-03-26 22:07:53
 */
public interface WxAccountService extends IService<WxAccount> {

	/**
	 * 生成公众号二维码
	 *
	 * @param appId
	 * @return
	 */
	R generateQr(String appId);

	/**
	 * 获取公众号统计数据
	 *
	 * @param appId
	 * @param interval
	 * @return
	 */
	R statistics(String appId, String interval);
}
