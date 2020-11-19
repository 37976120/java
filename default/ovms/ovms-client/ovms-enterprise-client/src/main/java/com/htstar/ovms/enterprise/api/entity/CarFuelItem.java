package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 公车加油表
 *
 * @author lw
 * @date 2020-06-08 13:48:44
 */
@Data
@TableName("car_fuel_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公车加油表")
public class CarFuelItem extends Model<CarFuelItem> {
    private static final long serialVersionUID = 1L;

    /**
     * 加油信息编号
     */
    @TableId
    @ApiModelProperty(value="编号")
    private Integer id;
    /**
     * 所属企业
     */
    @ApiModelProperty(value="所属企业")
    private Integer etpId;
    /**
     * 车辆编号
     */
    @ApiModelProperty(value="车辆编号",required = true)
    @NotBlank(message = "车辆信息不能为空")
    private Integer carId;
    /**
     * 加油时间
     */
    @ApiModelProperty(value="加油时间",required = true)
    @NotBlank(message = "加油时间不能为空")
    private LocalDateTime fuelTime;
    /**
     * 加油费用
     */
    @ApiModelProperty(value="加油费用",required = true)
    @NotBlank(message = "加油费用不能为空")
    private Integer fuelMoney;
    /**
     * 油号
     */
    @ApiModelProperty(value="加油类型油号 0:0号,1:89 2:92 3:95 4:98",required = true)
    @NotBlank(message = "加油类型不能为空")
    private Integer fuelType ;
    /**
     * 单价
     */
    @ApiModelProperty(value="单价",required = true)
    @NotBlank(message = "加油单价不能为空")
    private Integer unitPrice;
    /**
     * 加油站
     */
    @ApiModelProperty(value="加油站")
    private String fuelAddr;
    /**
     * 仪表板总里程
     */
    @ApiModelProperty(value="加油时公里数",required = true)
    @NotBlank(message = "加油时公里数不能为空")
    private Double instrMileage;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;
    /**
     * 票据保存地址
     */
    @ApiModelProperty(value="票据保存地址")
    private String billAddr;
    /**
     * 删除状态:0=正常，1=已删除
     */
    @ApiModelProperty(value="删除状态:0=正常，1=已删除")
    private Integer delFlag;
    @ApiModelProperty(value=" 用户id")
    private Integer userId;
    @ApiModelProperty(value=" 项目状态 0:待提交 1:待审核 2:已存档")
    private Integer itemStatus;
    @ApiModelProperty(value=" 用车记录号")
    private Integer orderId;
    @ApiModelProperty(value = "油卡Id")
    private Integer cardId;
    @ApiModelProperty(value = "加油后余额")
    private Integer endCost;
    @ApiModelProperty(value ="司机的用户id")
    private Integer driverUserId;
    @ApiModelProperty(value = "审批退回备注")
    private String applyRemark;
    @TableField(exist = false)
    @ApiModelProperty(value = "卡号")
    private String cardNo;
    /**
     * 负责人
     */
    @TableField(exist = false)
    @ApiModelProperty(value="加油司机")
    private String fuelAdmin;
}
