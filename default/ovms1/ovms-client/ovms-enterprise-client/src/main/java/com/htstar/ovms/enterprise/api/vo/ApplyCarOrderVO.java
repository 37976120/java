package com.htstar.ovms.enterprise.api.vo;

import com.htstar.ovms.enterprise.api.entity.ApplyProcessRecord;
import com.htstar.ovms.enterprise.api.entity.CarDriverInfo;
import com.htstar.ovms.enterprise.api.entity.CarInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
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
public class ApplyCarOrderVO {
    @ApiModelProperty(value="编号")
    private Integer orderId;

    /**
     * 企业id
     */
    @ApiModelProperty(value="企业id")
    private Integer etpId;
    /**
     * 申请类型:0=公车申请；1=私车公用；2=直接派车；
     */
    @ApiModelProperty(value="申请类型:0=公车申请；1=私车公用；2=直接派车；")
    private Integer applyType;
    /**
     * 申请人ID
     */
    @ApiModelProperty(value="申请人ID")
    private Integer applyUserId;

    /**
     * 申请人姓名
     */
    @ApiModelProperty(value="申请人姓名")
    private String applyUserNickName;

    /**
     * 申请人姓名
     */
    @ApiModelProperty(value="申请人手机号")
    private String applyUserPhone;

    /**
     * 事件状态:0=进行中；1=完成；2=拒绝；3=作废;4=撤回;；
     */
    @ApiModelProperty(value = "事件状态:0=进行中；1=完成；2=拒绝；3=作废;4=撤回;；")
    private Integer orderStatus;
    /**
     * 当前节点状态（正处于）：节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；
     */
    @ApiModelProperty(value="当前节点状态（正处于）：节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；")
    private Integer nowNodeType;
    /**
     * 下一个节点转态（待处理）：节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；
     */
    @ApiModelProperty(value="下一个节点转态（待处理）：节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；")
    private Integer nextNodeType;
    /**
     * 流程ID
     */
    @ApiModelProperty(value="流程ID")
    private Integer processId;
    /**
     * 最新操作记录id（新增时候要默认添加一条操作记录）
     */
    @ApiModelProperty(value="最新操作记录id（新增时候要默认添加一条操作记录）")
    private Integer lastRecordId;

    /**
     * 用车级别（公用字典）：0=A级(紧凑型车)；1=B级（中型车）；2=C级（中大型车）；3=D级（大型车）；
     */
    @ApiModelProperty(value="用车级别（公用字典）：0=A级(紧凑型车)；1=B级（中型车）；2=C级（中大型车）；3=D级（大型车）；")
    private Integer carLevel;

    /**
     * 驾车方式:1=司机；2=自驾；
     */
    @ApiModelProperty(value="驾车方式:1=司机；2=自驾；")
    private Integer driveType;
    /**
     * 同行人数
     */
    @ApiModelProperty(value="同行人数")
    private Integer passengers;
    /**
     * 用车开始时间
     */
    @ApiModelProperty(value="用车开始时间")
    private LocalDateTime staTime;
    /**
     * 用车结束时间
     */
    @ApiModelProperty(value="用车结束时间")
    private LocalDateTime endTime;
    /**
     * 用车原因（50字以内）
     */
    @ApiModelProperty(value="用车原因（50字以内）")
    private String applyReason;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;


    /**
     * 车牌号（私车公用时有）
     */
    @ApiModelProperty(value="车牌号（私车公用时有）")
    private String carNumber;
    /**
     * 驾车人
     */
    @ApiModelProperty(value="驾车人")
    private Integer driveUserId;
    /**
     * 驾车人
     */
    @ApiModelProperty(value="驾车人信息")
    private DriverVO driverVO;
    /**
     * 驾驶证（图片路径，逗号隔开）
     */
    @ApiModelProperty(value="驾驶证（图片路径，逗号隔开）")
    private String driveLicense;
    /**
     * [审批人员ID列表(有序)]
     */
    @ApiModelProperty(value="[审批人员ID列表(有序)]")
    private String verifyUserList;

    @ApiModelProperty(value="审批人员")
    List<VerifyUserVO> verifyUserVOS;
    /**
     * [抄送人员ID列表(有序)]
     */
    @ApiModelProperty(value="[抄送人员ID列表(有序)]")
    private String ccUserList;
    @ApiModelProperty(value="[抄送人员]")
    List<VerifyUserVO> ccUserListVOS;
    /**
     * 出发地址（50字内）
     */
    @ApiModelProperty(value="出发地址（50字内）")
    private String staAddr;
    /**
     * 目的地址(列表','隔开)
     */
    @ApiModelProperty(value="目的地址(列表','隔开)")
    private String endAddrList;
    /**
     * [内部乘车人员ID列表]
     */
    @ApiModelProperty(value="[内部乘车人员ID列表]")
    private String innerPassengerList;

    @ApiModelProperty(value="[内部乘车人员]")
    List<VerifyUserVO> innerPassengerListVOS;

    /**
     * 外部乘车人列表[{name:'姓名(必填)',mobile:'手机号'}]
     */
    @ApiModelProperty(value="外部乘车人列表[{name:'姓名(必填)',mobile:'手机号'}]")
    private String outPassengerList;
    /**
     * 照片列表[{photoUrl:'图片路径'}]
     */
    @ApiModelProperty(value="照片列表[{photoUrl:'图片路径'}]")
    private String photoList;
    /**
     * 车辆id（分配后有大于0的记录，未分配默认0）
     */
    @ApiModelProperty(value="车辆id（分配后有大于0的记录，未分配默认0）")
    private Integer carId;

    @ApiModelProperty(value="车辆信息")
    private CarInfo carInfo;
    /**
     * 司机id（分配后有大于0的记录，未分配默认0）
     */
    @ApiModelProperty(value="司机id（分配后有大于0的记录，未分配默认0）")
    private Integer driverId;

    /**
     * 司机id（分配后有大于0的记录，未分配默认0）
     */
    @ApiModelProperty(value="司机")
    private DriverVO driver;


    /**
     * 提车里程
     */
    @ApiModelProperty(value="提车里程")
    private BigDecimal giveCarMileage;
    /**
     * 提车续航里程
     */
    @ApiModelProperty(value="提车续航里程")
    private BigDecimal giveCarKeepMileage;
    /**
     * 提车时间
     */
    @ApiModelProperty(value="提车时间")
    private LocalDateTime giveCarTime;
    /**
     * 提车凭证照片,逗号隔开
     */
    @ApiModelProperty(value="提车凭证照片,逗号隔开")
    private String giveCarPhoto;
    /**
     * 提车备注（30字内）
     */
    @ApiModelProperty(value="提车备注（30字内）")
    private String giveCarRemark;
    /**
     * 回车里程
     */
    @ApiModelProperty(value="回车里程")
    private BigDecimal retuenCarMileage;
    /**
     * 回车续航里程
     */
    @ApiModelProperty(value="回车续航里程")
    private BigDecimal retuenCarKeepMileage;
    /**
     * 回车时间
     */
    @ApiModelProperty(value="回车时间")
    private LocalDateTime retuenCarTime;
    /**
     * 回车凭证照片
     */
    @ApiModelProperty(value="回车凭证照片")
    private String retuenCarPhoto;
    /**
     * 提车备注（30字内）
     */
    @ApiModelProperty(value="提车备注（30字内）")
    private String returnCarRemark;


    /**
     * 私车公用(停车费)
     */
    @ApiModelProperty(value="私车公用(停车费)")
    private BigDecimal privateCarStopFee;
    /**
     * 私车公用(通行费)
     */
    @ApiModelProperty(value="私车公用(通行费)")
    private BigDecimal privateCarPassFee;
    /**
     * 私车公用(加油费)
     */
    @ApiModelProperty(value="私车公用(加油费)")
    private BigDecimal privateCarOilFee;
    /**
     * 私车公用(洗车费)
     */
    @ApiModelProperty(value="私车公用(洗车费)")
    private BigDecimal privateCarWashFee;
    /**
     * 私车公用(其他费用)
     */
    @ApiModelProperty(value="私车公用(其他费用)")
    private BigDecimal privateCarOtherFee;

    @ApiModelProperty(value="流程状态：1=等待审批；2=审批拒绝；3=等待分配；4=等待交车；5=交车拒绝；6=等待提车；7=提车拒绝；8=等待还车；9=等待开始用车；10=等待结束用车；11=等待收车；12=收车退回；13=已撤回（撤销）；14=完成；")
    private Integer processStatus;

    /**
     * 用车评分(1-5分)
     */
    @ApiModelProperty(value="用车评分(1-5分)")
    private Integer useScore;

    /**
     * 用车评价
     */
    @ApiModelProperty(value="用车评价")
    private String useRemark;


    @ApiModelProperty(value="最后一次操作记录：（操作原因）")
    private ApplyProcessRecord record;

    @ApiModelProperty(value="派车时间")
    private LocalDateTime distributionTime;


    @ApiModelProperty(value="用车审批人姓名")
    private String useVerifyName;



    @ApiModelProperty(value="回车审批人姓名")
    private String receiveVerrifyName;


    @ApiModelProperty(value="派车人姓名")
    private String giveCarUserName;


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
