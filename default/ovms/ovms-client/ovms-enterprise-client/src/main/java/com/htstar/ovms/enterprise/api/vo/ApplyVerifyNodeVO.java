package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/7
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ApplyVerifyNodeVO{
    @ApiModelProperty(value="审批人员列表(有序)")
    private List<VerifyUserVO> verifyUserVoList;


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
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
}
