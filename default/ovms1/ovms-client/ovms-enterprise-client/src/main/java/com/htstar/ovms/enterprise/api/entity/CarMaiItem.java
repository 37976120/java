package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 公车保养表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Data
@TableName("car_mai_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公车保养表")
public class CarMaiItem extends Model<CarMaiItem> {
    private static final long serialVersionUID = 1L;

    /**
     * 保养主键
     */
    @TableId
    @ApiModelProperty(value="保养主键")
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
     * 保养单位
     */
    @ApiModelProperty(value="保养单位")
    private String maiEtp;
    /**
     * 保养花费金额
     */
    @ApiModelProperty(value="保养花费金额",required = true)
    @NotBlank(message =  "保养花费不能为空")
    private Integer maiMoney;
    /**
     * 保养日期
     */
    @ApiModelProperty(value="保养日期",required = true)
    @NotBlank(message ="保养日期不能为空")
    private LocalDateTime maiTime;
    /**
     * 下次保养日期
     */
    @ApiModelProperty(value="下次保养日期",required = true)
    @NotBlank(message = "下次保养日期不能为空")
    private LocalDateTime nextTime;
    /**
     * 本次保养里程
     */
    @ApiModelProperty(value="本次保养里程",required = true)
    @NotBlank(message = "本次保养时里程不能为空")
    private Double maiMileage;
    /**
     * 下次保养里程interval_mileage
     */
    @ApiModelProperty(value="间隔保养里程",required = true)
    @NotBlank(message = "间隔保养里程不能为空")
    private Double intervalMileage;
    /**
     * 保养负责人
     */
    @ApiModelProperty(value="保养负责人")
    private String maiAdmin;
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
     * 票据地址
     */
    @ApiModelProperty(value="票据地址")
    private String billAddr;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;
    /**
     * 删除状态:0=正常，1=已删除
     */
    @ApiModelProperty(value="删除状态:0=正常，1=已删除")
    private Integer delFlag;
    @ApiModelProperty(value=" 项目状态 0:待提交 1:待审核 2:已存档")
    private Integer itemStatus;
    @ApiModelProperty(value="保养项目")
    private String maiPro;
    @ApiModelProperty(value="用户Id")
    private Integer userId;
    @ApiModelProperty(value="保养类别")
    private String maiType;
    @ApiModelProperty(value = "审批退回备注")
    private String applyRemark;
}
