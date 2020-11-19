package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/23
 * Company: 航通星空
 * Modified By:
 */
@Data
public class DetailApplyCarRecordVO {
    @ApiModelProperty(value = "1=同意；2=拒绝；3=退回；4=修改；5=作废；6=撤回；7=分配车辆；8=分配司机;9=提车上报;10=还车上报")
    private Integer operationType;

    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value="用户名")
    private String username;

    @ApiModelProperty(value="操作描述")
    private String operationDesc;

    @ApiModelProperty(value="姓名")
    private String nickName;

    @ApiModelProperty(value="节点类型：10=发起申请；20=审批；21=开始用车；22=结束用车；30=公车（交车）,私车（分配司机）；40=提车；50=还车；60=还车审核")
    private Integer nodeType;

    @ApiModelProperty(value="头像")
    private String avatar;
}
