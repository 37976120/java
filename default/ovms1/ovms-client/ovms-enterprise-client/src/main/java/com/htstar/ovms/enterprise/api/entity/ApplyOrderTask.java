package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 审批任务
 *
 * @author htxk
 * @date 2020-08-03 13:42:21
 */
@Data
@TableName("apply_order_task")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "审批任务")
public class ApplyOrderTask extends Model<ApplyOrderTask> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 审批类型：1=待我审批；2=已审批；3=抄送我的；4=我发起的；
     */
    @ApiModelProperty(value="审批类型：1=待我审批；2=已审批；3=抄送我的；4=我发起的；")
    private Integer taskType;
    /**
     * 公车申请事件id
     */
    @ApiModelProperty(value="公车申请事件id")
    private Integer orderId;
    /**
     * 审批类型:1=指定用户ID；2=角色类型；
     */
    @ApiModelProperty(value="审批类型:1=指定用户ID；2=角色类型；")
    private Integer verifyType;
    /**
     * 审批人ID
     */
    @ApiModelProperty(value="审批人ID")
    private Integer verifyUserId;
    /**
     * 审批人角色
     */
    @ApiModelProperty(value="审批人角色")
    private Integer verifyRoleId;
    /**
     * 流程状态：1=等待审批；2=审批拒绝；3=等待分配；4=等待交车；5=交车拒绝；6=等待提车；7=提车拒绝；8=等待还车；9=等待开始用车；10=等待结束用车；11=等待收车；12=收车退回；13=已撤回（撤销）；14=完成；
     */
    @ApiModelProperty(value="流程状态：1=等待审批；2=审批拒绝；3=等待分配；4=等待交车；5=交车拒绝；6=等待提车；7=提车拒绝；8=等待还车；9=等待开始用车；10=等待结束用车；11=等待收车；12=收车退回；13=已撤回（撤销）；14=完成；")
    private Integer processStatus;
    /**
     * 阅读状态:0=未读；1=已读；
     */
    @ApiModelProperty(value="阅读状态:0=未读；1=已读；")
    private Integer readFalg;
    /**
     * 阅读时间
     */
    @ApiModelProperty(value="阅读时间")
    private LocalDateTime readTime;
    /**
     * 删除标记，0=正常；1=删除
     */
    @ApiModelProperty(value="删除标记，0=正常；1=删除")
    private String delFlag;


}
