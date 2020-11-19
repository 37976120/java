package com.htstar.ovms.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.pay.entity.PayGoodsOrder;

import java.util.Map;

/**
 * 商品
 *
 * @author ovms
 * @date 2019-05-28 23:58:27
 */
public interface PayGoodsOrderService extends IService<PayGoodsOrder> {

	/**
	 * 购买商品
	 *
	 * @param goodsOrder goods
	 * @return
	 */
	Map<String, Object> buy(PayGoodsOrder goodsOrder);

}
