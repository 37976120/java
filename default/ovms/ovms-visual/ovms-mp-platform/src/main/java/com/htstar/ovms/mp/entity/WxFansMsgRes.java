
package com.htstar.ovms.mp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 微信粉丝消息回复表
 *
 * @author ovms
 * @date 2019-03-27 20:45:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxFansMsgRes extends Model<WxFansMsgRes> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	/**
	 * 粉丝消息ID
	 */
	private Integer fansMsgId;
	/**
	 * 回复内容
	 */
	private String resContent;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

}
