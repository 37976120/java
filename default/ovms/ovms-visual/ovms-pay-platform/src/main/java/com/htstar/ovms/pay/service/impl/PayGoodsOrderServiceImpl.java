
package com.htstar.ovms.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.pay.entity.PayGoodsOrder;
import com.htstar.ovms.pay.handler.PayOrderHandler;
import com.htstar.ovms.pay.mapper.PayGoodsOrderMapper;
import com.htstar.ovms.pay.service.PayGoodsOrderService;
import com.htstar.ovms.pay.utils.PayChannelNameEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品
 *
 * @author ovms
 * @date 2019-05-28 23:58:27
 */
@Slf4j
@Service
@AllArgsConstructor
public class PayGoodsOrderServiceImpl extends ServiceImpl<PayGoodsOrderMapper, PayGoodsOrder> implements PayGoodsOrderService {
	private final Map<String, PayOrderHandler> orderHandlerMap;
	private final HttpServletRequest request;


	/**
	 * 下单购买
	 *
	 * @param goodsOrder
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> buy(PayGoodsOrder goodsOrder) {
		String ua = request.getHeader(HttpHeaders.USER_AGENT);
		Enum channel = PayChannelNameEnum.getChannel(ua);
		PayOrderHandler orderHandler = orderHandlerMap.get(channel.name());
		goodsOrder.setGoodsName("测试产品");
		goodsOrder.setGoodsId("10001");
		Object params = orderHandler.handle(goodsOrder);

		Map<String, Object> result = new HashMap<>(4);
		result.put("channel", channel.name());
		result.put("goods", goodsOrder);
		result.put("params", params);
		return result;
	}

}
