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
 * 审批节点
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
@Data
@TableName("apply_verify_node")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "审批节点")
public class ApplyVerifyNode extends Model<ApplyVerifyNode> {
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
     * 流程id
     */
    @ApiModelProperty(value="流程id")
    private Integer processId;

    /**
     * 节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；
     */
    @ApiModelProperty(value="节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；")
    private Integer nodeType;

    /**
     * 短信提醒：0=否；1=是；
     */
    @ApiModelProperty(value="短信提醒：0=否；1=是；")
    private Integer smsNotifyStatus;
    /**
     * 允许退回：0=否；1=是；
     */
    @ApiModelProperty(value="允许退回：0=否；1=是；")
    private Integer backStatus;
    /**
     * 允许作废：0=否；1=是；
     */
    @ApiModelProperty(value="允许作废：0=否；1=是；")
    private Integer invalidStatus;
    /**
     * [审批人员ID列表(有序)]
     */
    @ApiModelProperty(value="[审批人员ID列表(有序)]")
    private String verifyUserList;
    /**
     * 创建人id
     */
    @ApiModelProperty(value="创建人id")
    private Integer createUserId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;


}
