package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplyOrderPageVO {
    @ApiModelProperty(value = "编号")
    private Integer orderId;
    @ApiModelProperty(value = "车辆ID")
    private Integer carId;
    @ApiModelProperty(value = "申请人ID")
    private Integer applyUserId;
    @ApiModelProperty(value = "申请人姓名")
    private String applyUserNickName;
    @ApiModelProperty(value = "申请类型:0=公车申请；1=私车公用；2=直接派车；")
    private Integer applyType;
    @ApiModelProperty(value = "当前节点状态（正处于）：节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；")
    private Integer nowNodeType;
    @ApiModelProperty(value = "下一个节点转态（待处理）：节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；")
    private Integer nextNodeType;
    @ApiModelProperty(value = "用车开始时间")
    private LocalDateTime staTime;
    @ApiModelProperty(value = "用车结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "流程状态：1=等待审批；2=审批拒绝；3=等待分配；4=等待交车；5=交车拒绝；6=等待提车；7=提车拒绝；8=等待还车；9=等待开始用车；10=等待结束用车；11=等待收车；12=收车退回；13=已撤回（撤销）；14=完成；")
    private Integer processStatus;
    @ApiModelProperty(value = "用车原因（50字以内）")
    private String applyReason;
    @ApiModelProperty(value = "流程ID")
    private Integer processId;
    @ApiModelProperty(value = "目的地址(列表','隔开)")
    private String endAddrList;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "出发地址（50字内）")
    private String staAddr;
    @ApiModelProperty(value = "部门名称")
    private String deptName;
    @ApiModelProperty(value = "部门ID")
    private String deptId;
    @ApiModelProperty(value = "车牌号")
    private String carNumber;
    @ApiModelProperty(value = "审批类型：1=待我审批；2=已审批；3=抄送我的；4=我发起的；")
    private Integer taskType;
    @ApiModelProperty(value = "司机id（分配后有大于0的记录，未分配默认0）")
    private Integer driverId;
    @ApiModelProperty(value = "司机姓名")
    private String driverName;
    @ApiModelProperty(value = "1=同意；2=拒绝；3=退回；4=修改；5=作废；6=撤回；7=分配车辆；8=分配司机;9=提车上报;10=还车上报")
    private Integer operationType;
    @ApiModelProperty(value = "回车上报信息状态：0=未提交；1=已提交；")
    private Integer retuenCarStatus;
    @ApiModelProperty(value = "同行人数")
    private Integer passengers;
    @ApiModelProperty(value = "驾车方式:1=司机；2=自驾；")
    private Integer driveType;
    @ApiModelProperty(value = "同意操作：0=无；1=有；")
    private int agreeIf = 0;
    @ApiModelProperty(value = "拒绝操作：0=无；1=有；")
    private int refuseIf = 0;
    @ApiModelProperty(value = "退回操作：0=无；1=有；")
    private int backIf = 0;
    @ApiModelProperty(value = "修改操作：0=无；1=有；")
    private int updateIf = 0;
    @ApiModelProperty(value = "撤回操作：0=无；1=有；")
    private int goStartIf = 0;
    @ApiModelProperty(value = "提车上报信息填充状态：0=未提交；1=已提交；")
    private Integer giveCarStatus;
    @ApiModelProperty(value = "一键提车：0=无；1=有；")
    private int getCarIf = 0;

    @ApiModelProperty(value = "结束用车：0=无；1=有；")
    private int endCarIf = 0;

    @ApiModelProperty(value = "还车：0=无；1=有；")
    private int retuenCarIf = 0;

    /**
     * 用车级别（公用字典）：0=A级(紧凑型车)；1=B级（中型车）；2=C级（中大型车）；3=D级（大型车）；
     */
    @ApiModelProperty(value = "用车级别（公用字典）：0=A级(紧凑型车)；1=B级（中型车）；2=C级（中大型车）；3=D级（大型车）；")
    private Integer carLevel;


    /**
     * 提车还车时需录入里程:0=否；1=是；
     */
    @ApiModelProperty(value = "提车还车时需录入里程:0=否；1=是；")
    private Integer mileageStatus;

//    /**
//     * 阅读状态:0=未读；1=已读；
//     */
//    @ApiModelProperty(value="阅读状态:0=未读；1=已读；")
//    private Integer readFalg;

    /**
     * 提车时OBD里程
     */
    @ApiModelProperty(value = "提车时OBD里程")
    private String startMile;

    /**
     * 还车时OBD里程
     */
    @ApiModelProperty(value = "还车时OBD里程")
    private String endMile;

}
