package com.htstar.ovms.pay.handler.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jpay.alipay.AliPayApiConfig;
import com.jpay.alipay.AliPayApiConfigKit;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.pay.entity.PayGoodsOrder;
import com.htstar.ovms.pay.entity.PayNotifyRecord;
import com.htstar.ovms.pay.entity.PayTradeOrder;
import com.htstar.ovms.pay.handler.MessageDuplicateCheckerHandler;
import com.htstar.ovms.pay.service.PayGoodsOrderService;
import com.htstar.ovms.pay.service.PayNotifyRecordService;
import com.htstar.ovms.pay.service.PayTradeOrderService;
import com.htstar.ovms.pay.utils.PayConstants;
import com.htstar.ovms.pay.utils.TradeStatusEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author ovms
 * @date 2019-06-27
 * <p>
 * 支付宝回调处理
 */
@Slf4j
@AllArgsConstructor
@Service("alipayCallback")
public class AlipayPayNotifyCallbackHandler extends AbstractPayNotifyCallbakHandler {
	private final MessageDuplicateCheckerHandler duplicateCheckerHandler;
	private final PayTradeOrderService tradeOrderService;
	private final PayGoodsOrderService goodsOrderService;
	private final PayNotifyRecordService recordService;

	/**
	 * 维护租户信息
	 *
	 * @param params
	 */
	@Override
	public void before(Map<String, String> params) {
		Integer tenant = MapUtil.getInt(params, "passback_params");
		TenantContextHolder.setEtpId(tenant);
	}

	/**
	 * 去重处理
	 *
	 * @param params 回调报文
	 * @return
	 */
	@Override
	public Boolean duplicateChecker(Map<String, String> params) {
		//判断是否是为支付中
		if (StrUtil.equals(TradeStatusEnum.WAIT_BUYER_PAY.getDescription(), params.get(PayConstants.TRADE_STATUS))) {
			log.info("支付宝订单待支付 {} 不做处理", params);
			return true;
		}

		// 判断10秒内是否已经回调处理
		if (duplicateCheckerHandler.isDuplicate(params.get(PayConstants.OUT_TRADE_NO))) {
			log.info("支付宝订单重复回调 {} 不做处理", params);
			this.saveNotifyRecord(params, "重复回调");
			return true;
		}
		return false;
	}

	/**
	 * 验签逻辑
	 *
	 * @param params 回调报文
	 * @return
	 */
	@Override
	public Boolean verifyNotify(Map<String, String> params) {
		String callReq = MapUtil.join(params, StrUtil.DASHED, StrUtil.DASHED);
		log.info("支付宝发起回调 报文: {}", callReq);
		String appId = params.get("app_id");

		if (StrUtil.isBlank(appId)) {
			log.warn("支付宝回调报文 appid 为空 {}", callReq);
			return false;
		}

		AliPayApiConfig apiConfig = AliPayApiConfigKit.getApiConfig(appId);
		if (apiConfig == null) {
			log.warn("支付宝回调报文 appid 不合法 {}", callReq);
			return false;
		}

		try {
			return AlipaySignature.rsaCheckV1(params, apiConfig.getAlipayPublicKey()
					, CharsetUtil.UTF_8, "RSA2");
		} catch (AlipayApiException e) {
			log.error("支付宝验签失败", e);
			return false;
		}
	}

	/**
	 * 解析报文
	 *
	 * @param params 回调报文
	 * @return
	 */
	@Override
	public String parse(Map<String, String> params) {
		String tradeStatus = EnumUtil.fromString(TradeStatusEnum.class, params.get(PayConstants.TRADE_STATUS)).getStatus();

		String orderNo = params.get(PayConstants.OUT_TRADE_NO);
		PayGoodsOrder goodsOrder = goodsOrderService.getOne(Wrappers.<PayGoodsOrder>lambdaQuery()
				.eq(PayGoodsOrder::getPayOrderId, orderNo));
		goodsOrder.setStatus(tradeStatus);
		goodsOrderService.updateById(goodsOrder);

		PayTradeOrder tradeOrder = tradeOrderService.getOne(Wrappers.<PayTradeOrder>lambdaQuery()
				.eq(PayTradeOrder::getOrderId, orderNo));
		Long succTime = MapUtil.getLong(params, "time_end");
		tradeOrder.setPaySuccTime(succTime);
		tradeOrder.setChannelOrderNo(params.get("trade_no"));
		tradeOrder.setStatus(TradeStatusEnum.TRADE_SUCCESS.getStatus());
		tradeOrder.setChannelOrderNo(params.get("transaction_id"));
		tradeOrderService.updateById(tradeOrder);

		return "success";
	}

	/**
	 * 保存回调记录
	 *
	 * @param result 处理结果
	 * @param params 回调报文
	 */
	@Override
	public void saveNotifyRecord(Map<String, String> params, String result) {
		PayNotifyRecord record = new PayNotifyRecord();
		String notifyId = params.get("notify_id");
		saveRecord(params, result, record, notifyId, recordService);
	}

}
