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
 * 异步通知记录
 *
 * @author ovms
 * @date 2019-05-28 23:57:23
 */
@Data
@TableName("pay_notify_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "异步通知记录")
public class PayNotifyRecord extends Model<PayNotifyRecord> {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Long id;
	/**
	 * 响应ID
	 */
	private String notifyId;
	/**
	 * 请求报文
	 */
	private String request;
	/**
	 * 响应报文
	 */
	private String response;
	/**
	 * 系统订单号
	 */
	private String orderNo;
	/**
	 * http状态
	 */
	private String httpStatus;
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
