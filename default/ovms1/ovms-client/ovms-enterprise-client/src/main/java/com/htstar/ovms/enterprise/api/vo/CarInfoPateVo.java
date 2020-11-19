package com.htstar.ovms.enterprise.api.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 公车表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Data
public class CarInfoPateVo extends Model<CarInfoPateVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 车辆Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "车辆Id")
    private Integer id;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String licCode;
    /**
     * 车架号
     */
    @ApiModelProperty(value = "车架号")
    private String frameCode;
    /**
     * 发动机号
     */
    @ApiModelProperty(value = "发动机号")
    private String engineCode;
    /**
     * 车辆发证日期
     */
    @ApiModelProperty(value = "车辆发证日期")
    private LocalDateTime issuedTime;
    /**
     * 车辆注册日期
     */
    @ApiModelProperty(value = "车辆注册日期")
    private LocalDateTime registerTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private Integer deptId;
    /**
     * 所属企业
     */
    @ApiModelProperty(value = "所属企业")
    private Integer etpId;

    /**
     * 司机
     */
    @ApiModelProperty(value = "司机")
    private Integer driverId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
    /**
     * 行驶证照片地址
     */
    @ApiModelProperty(value = "行驶证照片地址")
    private String licenseAddr;
    @ApiModelProperty(value = "车辆照片地址")
    private String carAddr;

    /**
     * 删除状态:0=正常，1=已删除
     */
    @ApiModelProperty(value = "删除状态:0=正常，1=已删除")
    private Integer delFlag;
    /**
     * 操作人（添加数据管理员）
     */
    @ApiModelProperty(value = "操作人（添加数据管理员）")
    private Integer userId;
    /**
     * 排量
     */
    @ApiModelProperty(value = "排量")
    private Integer carEmission;
    /**
     * 燃油标号
     */
    @ApiModelProperty(value = "燃油标号")
    private Integer fuelType;
    /**
     * 座位数
     */
    @ApiModelProperty(value = "座位数")
    private Integer seateNum;
    /**
     * 级别
     */
    @ApiModelProperty(value = "级别")
    private Integer carLevel;
    /**
     * 车辆性质
     */
    @ApiModelProperty(value = "车辆性质")
    private Integer carRole;
    /**
     * 行驶证编号
     */
    @ApiModelProperty(value = "行驶证编号")
    private String licenseCode;


    @TableField(exist = false)
    @ApiModelProperty(value = "企业名称")
    private String etpName;

    @ApiModelProperty(value = "车辆类型")
    private Integer carType;
    @ApiModelProperty(value = "车辆状态 0:正常 1：维修 2：保养 3:年检 4:故障")
    private Integer carStatus;
    @ApiModelProperty(value = "vin码")
    private String vinCode;
    @ApiModelProperty(value = "车辆属有人")
    private String carBelongUser;
    @TableField(exist = false)
    @ApiModelProperty(value = "绑定设备编号")
    private String deviceSn;

    @TableField(exist = false)
    @ApiModelProperty(value = "创建人账号")
    private String username;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @TableField(exist = false)
    @ApiModelProperty(value = "车辆类型具体名称")
    private String typeName;
    @TableField(exist = false)
    @ApiModelProperty(value = "车辆是否使用，大于0为未使用")
    private Integer orderStatus = 1;

    @TableField(exist = false)
    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "购置日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime purchaseDate;

    @ApiModelProperty(value = "购置价格")
    private String purchaseValue;

    @ApiModelProperty(value = "注销日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime invalidDate;

    @ApiModelProperty(value = "车辆申请状态  0  未申请 ， 1已申请")
    private Integer applyStatus;

    @ApiModelProperty(value = "车辆申请状态  0  未申请 ， 1已申请")
    private String initialMile;


}
