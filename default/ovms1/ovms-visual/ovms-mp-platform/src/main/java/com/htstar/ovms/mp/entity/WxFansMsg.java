
package com.htstar.ovms.mp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 微信消息
 *
 * @author ovms
 * @date 2019-03-27 20:45:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxFansMsg extends Model<WxFansMsg> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	/**
	 * 用户标识
	 */
	private String openid;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 头像地址
	 */
	private String headimgUrl;
	/**
	 * 微信账号ID
	 */
	private Integer wxAccountId;

	/**
	 * 微信公众号appid
	 */
	private String wxAccountAppid;

	/**
	 * 微信公众号名
	 */
	private String wxAccountName;

	/**
	 * 消息ID
	 */
	private Long msgId;

	/**
	 * 消息类型
	 */
	private String msgType;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 最近一条回复内容
	 */
	private String resContent;
	/**
	 * 是否已回复
	 */
	private String isRes;
	/**
	 * 微信素材ID
	 */
	private String mediaId;
	/**
	 * 微信图片URL
	 */
	private String picUrl;
	/**
	 * 本地图片路径
	 */
	private String picPath;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 是否删除  -1：已删除  0：正常
	 */
	@TableLogic
	private String delFlag;

	/**
	 * 租户
	 */
	private Integer etpId;

}
