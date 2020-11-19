package com.htstar.ovms.pay.handler.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.jpay.alipay.AliPayApi;
import com.jpay.alipay.AliPayApiConfigKit;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.pay.config.PayCommonProperties;
import com.htstar.ovms.pay.entity.PayGoodsOrder;
import com.htstar.ovms.pay.entity.PayTradeOrder;
import com.htstar.ovms.pay.mapper.PayGoodsOrderMapper;
import com.htstar.ovms.pay.mapper.PayTradeOrderMapper;
import com.htstar.ovms.pay.utils.OrderStatusEnum;
import com.htstar.ovms.pay.utils.PayChannelNameEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ovms
 * @date 2019-05-31
 * <p>
 * 支付宝手机支付
 */
@Slf4j
@Service("ALIPAY_WAP")
@AllArgsConstructor
public class AlipayWapPayOrderHandler extends AbstractPayOrderHandler {
	private final PayCommonProperties payCommonProperties;
	private final PayTradeOrderMapper tradeOrderMapper;
	private final PayGoodsOrderMapper goodsOrderMapper;
	private final HttpServletRequest request;
	private final HttpServletResponse response;

	/**
	 * 创建交易订单
	 *
	 * @param goodsOrder
	 * @return
	 */
	@Override
	public PayTradeOrder createTradeOrder(PayGoodsOrder goodsOrder) {
		PayTradeOrder tradeOrder = new PayTradeOrder();
		tradeOrder.setOrderId(goodsOrder.getPayOrderId());
		tradeOrder.setAmount(goodsOrder.getAmount());
		tradeOrder.setChannelId(PayChannelNameEnum.ALIPAY_WAP.getName());
		tradeOrder.setChannelMchId(AliPayApiConfigKit.getAliPayApiConfig().getAppId());
		tradeOrder.setClientIp(ServletUtil.getClientIP(request));
		tradeOrder.setCurrency("cny");
		tradeOrder.setExpireTime(Long.parseLong(payCommonProperties.getAliPayConfig().getExpireTime()));
		tradeOrder.setStatus(OrderStatusEnum.INIT.getStatus());
		tradeOrder.setBody(goodsOrder.getGoodsName());
		tradeOrderMapper.insert(tradeOrder);
		return tradeOrder;
	}

	/**
	 * 调起渠道支付
	 *
	 * @param goodsOrder 商品订单
	 * @param tradeOrder 交易订单
	 */
	@Override
	public PayTradeOrder pay(PayGoodsOrder goodsOrder, PayTradeOrder tradeOrder) {
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setBody(tradeOrder.getBody());
		model.setSubject(tradeOrder.getBody());
		model.setOutTradeNo(tradeOrder.getOrderId());
		model.setTimeoutExpress(payCommonProperties.getAliPayConfig().getExpireTime() + "m");

		//分转成元 并且保留两位
		model.setTotalAmount(NumberUtil.div(tradeOrder.getAmount(), "100", 2).toString());
		model.setProductCode(goodsOrder.getGoodsId());
		model.setPassbackParams(String.valueOf(TenantContextHolder.getEtpId()));
		try {
			AliPayApi.wapPay(response, model, payCommonProperties.getAliPayConfig().getReturnUrl()
					, payCommonProperties.getAliPayConfig().getNotifyUrl());
		} catch (AlipayApiException e) {
			log.error("支付宝手机支付失败", e);
			tradeOrder.setErrMsg(e.getErrMsg());
			tradeOrder.setErrCode(e.getErrCode());
			tradeOrder.setStatus(OrderStatusEnum.FAIL.getStatus());
			goodsOrder.setStatus(OrderStatusEnum.FAIL.getStatus());
		} catch (IOException e) {
			log.error("支付宝手机支付失败", e);
			tradeOrder.setErrMsg(e.getMessage());
			tradeOrder.setStatus(OrderStatusEnum.FAIL.getStatus());
			goodsOrder.setStatus(OrderStatusEnum.FAIL.getStatus());
		}
		return tradeOrder;
	}

	/**
	 * 更新订单信息
	 *
	 * @param goodsOrder 商品订单
	 * @param tradeOrder 交易订单
	 */
	@Override
	public void updateOrder(PayGoodsOrder goodsOrder, PayTradeOrder tradeOrder) {
		tradeOrderMapper.updateById(tradeOrder);
		goodsOrderMapper.updateById(goodsOrder);
	}
}
