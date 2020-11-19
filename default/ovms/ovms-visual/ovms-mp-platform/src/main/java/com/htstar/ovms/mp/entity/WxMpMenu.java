
package com.htstar.ovms.mp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 微信菜单表
 *
 * @author ovms
 * @date 2019-03-27 20:45:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxMpMenu extends Model<WxMpMenu> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;

	/**
	 * 菜单
	 */
	private String menu;

	/**
	 * 微信公众号ID
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
	 * 是否发布 0 未发布 1 已发布
	 */
	private String pubFlag;

}
