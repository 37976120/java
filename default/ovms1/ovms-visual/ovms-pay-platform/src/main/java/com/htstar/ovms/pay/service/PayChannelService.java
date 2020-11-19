package com.htstar.ovms.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.pay.entity.PayChannel;

/**
 * 渠道
 *
 * @author ovms
 * @date 2019-05-28 23:57:58
 */
public interface PayChannelService extends IService<PayChannel> {

	/**
	 * 新增支付渠道
	 *
	 * @param payChannel 支付渠道
	 * @return
	 */
	Boolean saveChannel(PayChannel payChannel);
}
