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
import java.time.LocalDateTime;


/**
 * etc通行记录

 *
 * @author lw
 * @date 2020-06-22 10:11:23
 */
@Data
@TableName("car_etc_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "etc通行记录 ")
public class CarEtcItem extends Model<CarEtcItem> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value="id")
    private Integer id;
    /**
     * 企业id
     */
    @ApiModelProperty(value="企业id")
    private Integer etpId;
    /**
     * 车辆Id
     */
    @ApiModelProperty(value="车辆Id",required = true)
    @NotBlank(message = "车辆信息不能为空")
    private Integer carId;
    /**
     * 录入人
     */
    @ApiModelProperty(value="录入人")
    private Integer userId;
    /**
     * 创建日期
     */
    @ApiModelProperty(value="创建日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改日期
     */

    @ApiModelProperty(value="修改日期")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    /**
     * 项目日期
     */
    @ApiModelProperty(value="项目日期",required = true)
    @NotBlank(message = "费用产生日期不能为空")
    private LocalDateTime itemTime;
    /**
     * 入口
     */
    @ApiModelProperty(value="入口")
    private String entrance;
    /**
     * 出口
     */
    @ApiModelProperty(value="出口")
    private String export;
    /**
     * 项目状态:0:待提交1:待审核2:已存档
     */
    @ApiModelProperty(value="项目状态:0:待提交1:待审核2:已存档")
    private Integer itemStatus;
    /**
     * 删除状态:0=正常，1=已删除
     */
    @ApiModelProperty(value="删除状态:0=正常，1=已删除")
    private Integer delFlag;
    /**
     * etc费用
     */
    @ApiModelProperty(value="etc费用",required = true)
    @NotBlank(message = "金额不能为空")
    private Integer etcMoney;

    /**
     * 用车记录
     */
    @ApiModelProperty(value="用车记录")
    private Integer orderId;
    /**
     * 凭证照片地址
     */
    @ApiModelProperty(value="凭证照片地址")
    private String billAddr;
    @ApiModelProperty(value = "油卡Id")
    private Integer cardId;
    @ApiModelProperty(value = "etc卡使用后余额")
    private Integer endCost;

    @ApiModelProperty(value ="司机的用户id")
    private Integer driverUserId;

    @ApiModelProperty(value = "审批备注")
    private String applyRemark;
    @TableField(exist = false)
    @ApiModelProperty(value = "卡号")
    private String cardNo;
    /**
     * 费用司机
     */
    @TableField(exist = false)
    @ApiModelProperty(value="费用司机")
    private String itemDriver;

}
