
package com.htstar.ovms.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.mp.entity.WxFansMsgRes;

/**
 * 微信消息回复业务
 *
 * @author ovms
 * @date 2019-03-27 20:45:48
 */
public interface WxFansMsgResService extends IService<WxFansMsgRes> {

	/**
	 * 保存并发送回复
	 *
	 * @param wxFansMsgRes
	 * @return
	 */
	R saveAndSend(WxFansMsgRes wxFansMsgRes);
}
