package com.htstar.ovms.pay.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 商品
 *
 * @author ovms
 * @date 2019-05-28 23:58:27
 */
@Data
@TableName("pay_goods_order")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商品")
public class PayGoodsOrder extends Model<PayGoodsOrder> {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品订单ID
	 */
	@TableId
	private String goodsOrderId;
	/**
	 * 商品ID
	 */
	private String goodsId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 金额,单位分
	 */
	private String amount;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 订单状态,订单生成(0),支付成功(1),处理完成(2),处理失败(-1)
	 */
	private String status;
	/**
	 * 支付订单号
	 */
	private String payOrderId;
	/**
	 * 0-正常,1-删除
	 */
	@TableLogic
	private String delFlag;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 租户ID
	 */
	private Integer etpId;

}
