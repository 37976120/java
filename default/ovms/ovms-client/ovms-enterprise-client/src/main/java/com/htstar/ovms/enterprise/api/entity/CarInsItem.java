package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.impl.MapEntrySerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 公车保险表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Data
@TableName("car_ins_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公车保险表")
public class CarInsItem extends Model<CarInsItem> {
private static final long serialVersionUID = 1L;

    /**
     * 保险主键
     */
    @TableId
    @ApiModelProperty(value="保险主键")
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
     * 保险单位
     */
    @ApiModelProperty(value="保险公司")
    private String  insCompany;
    @ApiModelProperty(value="保险类型",required = true)
    @NotBlank(message = "保险类型不能为空")
    private Integer  insType;
    /**
     * 保险费用
     */
    @ApiModelProperty(value="保险费用",required = true)
    @NotBlank(message = "保险费用不能为空")
    private Integer insMoney;
    /**
     * 开始日期
     */
    @ApiModelProperty(value="开始日期",required = true)
    @NotBlank(message = "保险日期不能为空")
    private LocalDateTime startTime;
    /**
     * 结束日期
     */
    @ApiModelProperty(value="结束日期",required = true )
    @NotBlank(message = "保险到期日期不能为空")
    private LocalDateTime endTime;
    @ApiModelProperty(value="办理人")
    private String trustees;
    @ApiModelProperty(value="经办人",required =true)
    @NotBlank(message = "保险经办人不能为空")
    private String operator;
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
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    /**
     * 票据保存地址
     */
    @ApiModelProperty(value="票据保存地址")
    private String billAddr;
    /**
     * 备注
     */
    @ApiModelProperty(value="保险项目内容")
    private String remark;
    /**
     * 删除状态:0=正常，1=已删除
     */
    @ApiModelProperty(value="删除状态:0=正常，1=已删除")
    private Integer delFlag;
    @ApiModelProperty(value="用户Id")
    private Integer userId;
    @ApiModelProperty(value=" 项目状态 0:待提交 1:待审核 2:已存档")
    private Integer itemStatus;

    @ApiModelProperty(value="审批备注")
    private String applyRemark;

    }
