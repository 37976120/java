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
 * 渠道
 *
 * @author ovms
 * @date 2019-05-28 23:57:58
 */
@Data
@TableName("pay_channel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "渠道")
public class PayChannel extends Model<PayChannel> {
	private static final long serialVersionUID = 1L;

	/**
	 * 渠道主键ID
	 */
	@TableId
	private Integer id;
	/**
	 * 应用ID
	 */
	private String appId;
	/**
	 * 渠道ID
	 */
	private String channelId;
	/**
	 * 渠道名称,如:alipay,wechat
	 */
	private String channelName;
	/**
	 * 渠道商户ID
	 */
	private String channelMchId;
	/**
	 * 渠道状态
	 */
	private String state;
	/**
	 * 配置参数,json字符串
	 */
	private String param;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 删除标记
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
