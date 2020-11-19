package com.htstar.ovms.enterprise.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PageApplyOrderReq extends Page implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "事件状态:0=进行中；1=完成；2=拒绝；3=作废;4=撤回;；")
    private Integer orderStatus;

    @ApiModelProperty(value = "用车编号")
    private Integer orderId;

    @ApiModelProperty(value="申请类型:0=公车申请；1=私车公用；2=直接派车；")
    private Integer applyType;
    @ApiModelProperty(value="开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime staTime;
    @ApiModelProperty(value="结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "车辆ID")
    private Integer carId;
    @ApiModelProperty(value="车牌号")
    private String carNumber;
    @ApiModelProperty(value="流程状态：1=等待审批；2=审批拒绝；3=等待分配；4=等待交车；5=交车拒绝；6=等待提车；7=提车拒绝；8=等待还车；9=等待开始用车；10=等待结束用车；11=等待收车；12=收车退回；13=已撤回（撤销）；14=完成；")
    private String processStatus;
    private Integer deptId;
    @ApiModelProperty(value="类型：1=待我审批；2=已审批；3=抄送我的；4=我发起的；")
    private String taskType;

    /**
     * 阅读状态:0=未读；1=已读；
     */
    @ApiModelProperty(value="阅读状态:0=未读；1=已读；")
    private Integer readFalg;

    @ApiModelProperty(value="申请人姓名")
    private String nickName;


    @ApiModelProperty(hidden = true)
    private Integer nowNodeType;
    @ApiModelProperty(hidden = true)
    private Integer nextNodeType;
    @ApiModelProperty(hidden = true)
    private Integer operationType;



    /**
     * 服务端自动装填
     */
    @ApiModelProperty(value="申请人ID")
    private Integer userId;

    /**
     * 服务端自动装填 企业ID
     */
    @ApiModelProperty(hidden = true)
    private Integer etpId;

}
