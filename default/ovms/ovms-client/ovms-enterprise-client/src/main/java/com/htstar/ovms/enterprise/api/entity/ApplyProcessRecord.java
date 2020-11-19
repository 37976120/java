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

import java.time.LocalDateTime;

/**
 * 流程记录
 *
 * @author htxk
 * @date 2020-07-02 14:04:00
 */
@Data
@TableName("apply_process_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "流程记录")
public class ApplyProcessRecord extends Model<ApplyProcessRecord> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 公车申请事件id
     */
    @ApiModelProperty(value="公车申请事件id")
    private Integer orderId;
    /**
     * 1=同意；2=拒绝；3=退回；4=修改；5=作废；6=撤回；7=分配车辆；8=分配司机;9=提车上报;10=还车上报
     */
    @ApiModelProperty(value="1=同意；2=拒绝；3=退回；4=修改；5=作废；6=撤回；7=分配车辆；8=分配司机;9=提车上报;10=还车上报")
    private Integer operationType;
    /**
     * 节点ID
     */
    @ApiModelProperty(value="节点ID")
    private Integer nodeId;
    /**
     * 操作人id
     */
    @ApiModelProperty(value="操作人id")
    private Integer operationUserId;
    /**
     * 备注（30字以内）
     */
    @ApiModelProperty(value="备注（30字以内）")
    private String remark;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;


}
