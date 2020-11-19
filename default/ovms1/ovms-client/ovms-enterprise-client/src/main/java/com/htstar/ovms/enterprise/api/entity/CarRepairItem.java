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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公车维修表

 *
 * @author lw
 * @date 2020-06-08 13:48:46
 */
@Data
@TableName("car_repair_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公车维修表 ")
public class CarRepairItem extends Model<CarRepairItem> {
private static final long serialVersionUID = 1L;

    /**
     * 维修表主键
     */
    @TableId
    @ApiModelProperty(value="维修表主键")
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
    @NotBlank(message = "车辆不能为空")
    private Integer carId;
    /**
     * 维修单位
     */
    @ApiModelProperty(value="维修单位")
    private String repairEtp;
    /**
     * 维修日期
     */
    @ApiModelProperty(value="维修日期",required = true)
    @NotBlank(message = "维修日期不能为空")
    private LocalDateTime repTime;
    /**
     * 维修花费
     */
    @ApiModelProperty(value="维修金额",required = true)
    @NotBlank(message = "维修金额不能为空")
    private Integer repMoney;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDateTime updateTime;
    /**
     * 票据保存信息
     */
    @ApiModelProperty(value="票据保存信息")
    private String billAddr;
    /**
     * 备注
     */
    @ApiModelProperty(value="维修项目内容")
    private String remark;
    /**
     * 删除状态:0=正常，1=已删除
     */
    @ApiModelProperty(value="删除状态:0=正常，1=已删除")
    private Integer delFlag;
    @ApiModelProperty(value="报修人",required = true)
    @NotBlank(message = "报修人不能为空")
    private String reportPeople;
    @ApiModelProperty(value="送修人")
    private String sendPeople;
    private Integer userId;
    @ApiModelProperty(value=" 项目状态 0:待提交 1:待审核 2:已存档")
    private Integer itemStatus;
    @ApiModelProperty(value="配件费用")
    private Integer partsMoney;
    @ApiModelProperty(value = "审批退回备注")
    private String applyRemark;
    @ApiModelProperty(value = "事故记录id")
    private Integer accidId;
}
