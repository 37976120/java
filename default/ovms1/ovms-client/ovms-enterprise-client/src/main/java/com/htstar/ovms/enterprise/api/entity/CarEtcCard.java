package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * etc卡
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@Data
@TableName("car_etc_card")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "etc卡")
public class CarEtcCard extends Model<CarEtcCard> {
private static final long serialVersionUID = 1L;

    /**
     * etc主键
     */
    @TableId
    @ApiModelProperty(value="etc主键")
    private Integer id;
    /**
     * 企业ID
     */
    @ApiModelProperty(value="企业ID")
    private Integer etpId;
    /**
     * 卡名
     */
    @ApiModelProperty(value="卡名",required = true)
    @NotEmpty(message = "卡名不能为空")
    private String cardName;
    /**
     * etc编号
     */
    @ApiModelProperty(value="etc编号",required = true)
    @NotEmpty(message = "卡号不能为空")
    private String cardNo;
    /**
     * 持卡人
     */
    @ApiModelProperty(value="持卡人")
    private String cardUser;
    /**
     * 持卡人手机号
     */
    @ApiModelProperty(value="持卡人手机号")
    private String mobile;
    /**
     * 卡里余额
     */
    @ApiModelProperty(value="卡里余额")
    private Integer cardBalance;
    /**
     * 绑定车辆id
     */
    @ApiModelProperty(value="绑定车辆id")
    private Integer bindingId;
    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建日期")
    private LocalDateTime createTime;
    /**
     * 修改日期
     */
    @ApiModelProperty(value="修改日期")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    /**
     * 删除状态 0:正常 1:删除
     */
    @ApiModelProperty(value="删除状态 0:正常 1:删除")
    private Integer delFlag;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value = "卡的类型 0:借记卡 1:信用卡")
    private Integer cardType;
}
