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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 费用审批记录
 *
 * @author lw
 * @date 2020-07-14 10:59:15
 */
@Data
@TableName("apply_cost_process_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "费用审批记录")
public class ApplyCostProcessRecord extends Model<ApplyCostProcessRecord> {
private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
    @ApiModelProperty(value="自增主键")
    private Integer id;
    /**
     * 企业Id
     */
    @ApiModelProperty(value="企业Id")
    private Integer etpId;
    /**
     * 审批人id
     */
    @ApiModelProperty(value="审批人id")
    private Integer operationUserId;
    /**
     * 申请人Id
     */
    @ApiModelProperty(value="申请人Id")
    private Integer applyUserId;

    /**
     * 费用id
     */
    @ApiModelProperty(value="费用id")
    private Integer costId;
    /**
     * 车辆id
     */
    @ApiModelProperty(value="车辆id")
    private Integer carId;
    /**
     * 费用类型
     */
    @ApiModelProperty(value="台账相关的费用类型 1:停车费 2:违章罚款 3:洗车美容 4:汽车用品 5:其他费用 6:通行费费用 7:加油费用 8:保险费用 9:保养费用 10:年检费用 11:维修费用 12:事故费用")
    private Integer costType;
    /**
     *  审核状态 0:待审核 1:存档 2:退回 3:删除
     */
    @ApiModelProperty(value="审核状态 1:待审2:存档 3:退回 4:删除 ")
    private Integer operationType;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 删除状态 0:正常 1:删除
     */
    @ApiModelProperty(value="删除状态 0:正常 1:删除")
    private Integer delFlag;

    @ApiModelProperty(value = "费用金额")
    private Integer cost;
    @ApiModelProperty(value ="备注")
    private String remark;
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value="费用时间")
    private LocalDateTime costTime;
    @ApiModelProperty(value="修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
