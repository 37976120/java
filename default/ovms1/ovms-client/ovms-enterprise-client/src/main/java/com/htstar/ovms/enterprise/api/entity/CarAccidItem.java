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
 * 车辆事故信息
 *
 * @author lw
 * @date 2020-06-22 10:11:23
 */
@Data
@TableName("car_accid_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车辆事故信息")
public class CarAccidItem extends Model<CarAccidItem> {
private static final long serialVersionUID = 1L;

    /**
     * 事故记录主键
     */
    @TableId
    @ApiModelProperty(value="事故记录主键")
    private Integer id;
    /**
     * 车辆编号
     */
    @ApiModelProperty(value="车辆编号",required = true)
    @NotBlank(message = "车辆信息不能为空")
    private Integer carId;
    /**
     * 录入人
     */
    @ApiModelProperty(value="录入人")
    private Integer userId;
    /**
     * 企业id
     */
    @ApiModelProperty(value="企业id")
    private Integer etpId;
    /**
     * 事故时间
     */
    @ApiModelProperty(value="事故时间",required=true)
    @NotBlank(message = "事故时间不能为空")
    private LocalDateTime accidTime;
    /**
     * 事故地点
     */
    @ApiModelProperty(value="事故地点",required = true)
    @NotBlank(message = "事故地点不能为空")
    private String accidAddr;
    /**
     * 驾驶人
     */
    @ApiModelProperty(value="驾驶人",required = true)
    @NotBlank(message = "驾驶人不能为空")
    private String driver;
    /**
     * 事故性质 0:一般 1:重大 2:特大
     */
    @ApiModelProperty(value="事故性质 0:一般 1:重大 2:特大")
    private Integer accidType;
    /**
     * 事故责任  0:全责 1:主要责任 2:同等责任 3:次要责任 4:无责任
     */
    @ApiModelProperty(value="事故责任  0:全责 1:主要责任 2:同等责任 3:次要责任 4:无责任")
    private Integer accidDuty;
    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建日期")
    private LocalDateTime createTime;
    /**
     * 修改日期
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value="修改日期")
    private LocalDateTime updateTime;
    /**
     * 车损情况
     */
    @ApiModelProperty(value="车损情况")
    private String remark;
    /**
     * 票据地址
     */
    @ApiModelProperty(value="票据地址")
    private String billAddr;
    /**
     * 项目状态:0:待提交1:待审核2:已存档
     */
    @ApiModelProperty(value="项目状态:0:待提交1:待审核2:已存档")
    private Integer itemStatus;
    @ApiModelProperty(value="删除状态:0=正常，1=已删除")
    private Integer delFlag;
    @ApiModelProperty(value = "审批退回备注")
    private String applyRemark;
}
