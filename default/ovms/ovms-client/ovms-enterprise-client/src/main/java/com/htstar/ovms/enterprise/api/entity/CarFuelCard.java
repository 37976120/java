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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 油卡
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@Data
@TableName("car_fuel_card")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "油卡")
public class CarFuelCard extends Model<CarFuelCard> {
private static final long serialVersionUID = 1L;

    /**
     * 油卡主键
     */
    @TableId
    @ApiModelProperty(value="油卡主键")
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
    @NotBlank(message = "卡名不能为空")
    private String cardName;
    /**
     * 油卡编号
     */
    @ApiModelProperty(value="油卡编号",required = true)
    @NotBlank(message = "卡号不能为空 ")
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
     * 油卡余额
     */
    @ApiModelProperty(value="油卡余额")
    private Integer cardBalance;
    /**
     * 绑定类型0:未绑定 1:车辆 2:部门 3:成员
     */
    @ApiModelProperty(value="绑定类型0:未绑定 1:车辆 2:部门 3:成员")
    private Integer bindingType=0;
    /**
     * 绑定车辆id
     */
    @ApiModelProperty(value="绑定对象Id")
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


}
