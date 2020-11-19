package com.htstar.ovms.pay.handler.impl;

import com.htstar.ovms.common.sequence.sequence.Sequence;
import com.htstar.ovms.pay.entity.PayGoodsOrder;
import com.htstar.ovms.pay.entity.PayTradeOrder;
import com.htstar.ovms.pay.handler.PayOrderHandler;
import com.htstar.ovms.pay.mapper.PayGoodsOrderMapper;
import com.htstar.ovms.pay.utils.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ovms
 * @date 2019-05-31
 */
public abstract class AbstractPayOrderHandler implements PayOrderHandler {
	@Autowired
	private PayGoodsOrderMapper goodsOrderMapper;
	@Autowired
	private Sequence paySequence;


	/**
	 * 创建商品订单
	 *
	 * @param goodsOrder 商品订单
	 * @return
	 */
	@Override
	public void createGoodsOrder(PayGoodsOrder goodsOrder) {
		goodsOrder.setPayOrderId(paySequence.nextNo());
		goodsOrder.setStatus(OrderStatusEnum.INIT.getStatus());
		goodsOrderMapper.insert(goodsOrder);
	}

	/**
	 * 调用入口
	 *
	 * @return
	 */
	@Override
	public Object handle(PayGoodsOrder payGoodsOrder) {
		createGoodsOrder(payGoodsOrder);
		PayTradeOrder tradeOrder = createTradeOrder(payGoodsOrder);
		Object result = pay(payGoodsOrder, tradeOrder);
		updateOrder(payGoodsOrder, tradeOrder);
		return result;
	}
}
