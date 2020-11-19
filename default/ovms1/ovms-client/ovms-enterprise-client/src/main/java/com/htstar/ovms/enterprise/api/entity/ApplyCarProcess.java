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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公车申请流程
 *
 * @author htxk
 * @date 2020-07-09 17:34:12
 */
@Data
@TableName("apply_car_process")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公车申请流程")
public class ApplyCarProcess extends Model<ApplyCarProcess> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 企业id
     */
    @ApiModelProperty(value="企业id")
    private Integer etpId;
    /**
     * 流程类型:0=公车申请；1=私车公用；
     */
    @ApiModelProperty(value="流程类型:0=公车申请；1=私车公用；")
    private Integer processType;
    /**
     * 是否由分配司机提车：0=否，1=是；
     * 目前默认否，
     */
    @ApiModelProperty(value="是否由分配司机提车：0=否，1=是；")
    private Integer driverGetCarStatus;
    /**
     * 提车还车时需录入里程:0=否；1=是；
     * 目前默认否
     */
    @ApiModelProperty(value="提车还车时需录入里程:0=否；1=是；")
    private Integer mileageStatus;
    /**
     * [审批节点ID列表(有序)]
     */
    @ApiModelProperty(value="[审批节点ID列表(有序)]")
    private String verifyNodeList;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;


}
