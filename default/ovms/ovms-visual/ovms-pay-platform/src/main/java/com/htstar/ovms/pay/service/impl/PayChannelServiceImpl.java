
package com.htstar.ovms.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.pay.entity.PayChannel;
import com.htstar.ovms.pay.mapper.PayChannelMapper;
import com.htstar.ovms.pay.service.PayChannelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 渠道
 *
 * @author ovms
 * @date 2019-05-28 23:57:58
 */
@Service
@AllArgsConstructor
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements PayChannelService {

	/**
	 * 新增支付渠道
	 *
	 * @param payChannel 支付渠道
	 * @return
	 */
	@Override
	public Boolean saveChannel(PayChannel payChannel) {
		return save(payChannel);
	}
}
