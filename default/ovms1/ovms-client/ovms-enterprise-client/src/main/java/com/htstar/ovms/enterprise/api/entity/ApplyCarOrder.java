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
 * 公车申请事件
 *
 * @author flr
 * @date 2020-06-30 18:24:20
 */
@Data
@TableName("apply_car_order")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公车申请事件")
public class ApplyCarOrder extends Model<ApplyCarOrder> {
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "")
    private Integer orderId;

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    private Integer etpId;
    /**
     * 申请类型:0=公车申请；1=私车公用；2=直接派车；
     */
    @ApiModelProperty(value = "申请类型:0=公车申请；1=私车公用；2=直接派车；")
    private Integer applyType;
    /**
     * 申请人ID
     */
    @ApiModelProperty(value = "申请人ID")
    private Integer applyUserId;

    @ApiModelProperty(value = "流程状态：1=等待审批；2=审批拒绝；3=等待分配；4=等待交车；5=交车拒绝；6=等待提车；7=提车拒绝；8=等待还车；9=等待开始用车；10=等待结束用车；11=等待收车；12=收车退回；13=已撤回（撤销）；14=完成；")
    private Integer processStatus;
    /**
     * 当前节点状态（正处于）：节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；
     */
    @ApiModelProperty(value = "当前节点状态（正处于）：节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；")
    private Integer nowNodeType;
    /**
     * 下一个节点转态（待处理）：节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；
     */
    @ApiModelProperty(value = "下一个节点转态（待处理）：节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；")
    private Integer nextNodeType;
    /**
     * 流程ID
     */
    @ApiModelProperty(value = "流程ID")
    private Integer processId;
    /**
     * 最新操作记录id（新增时候要默认添加一条操作记录）
     */
    @ApiModelProperty(value = "最新操作记录id（新增时候要默认添加一条操作记录）")
    private Integer lastRecordId;

    /**
     * 用车级别（公用字典）：0=A级(紧凑型车)；1=B级（中型车）；2=C级（中大型车）；3=D级（大型车）；
     */
    @ApiModelProperty(value = "用车级别（公用字典）：0=A级(紧凑型车)；1=B级（中型车）；2=C级（中大型车）；3=D级（大型车）；")
    private Integer carLevel;

    /**
     * 驾车方式:1=司机；2=自驾；
     */
    @ApiModelProperty(value = "驾车方式:1=司机；2=自驾；")
    private Integer driveType;
    /**
     * 同行人数
     */
    @ApiModelProperty(value = "同行人数")
    private Integer passengers;
    /**
     * 用车开始时间
     */
    @ApiModelProperty(value = "用车开始时间")
    private LocalDateTime staTime;
    /**
     * 用车结束时间
     */
    @ApiModelProperty(value = "用车结束时间")
    private LocalDateTime endTime;
    /**
     * 用车原因（50字以内）
     */
    @ApiModelProperty(value = "用车原因（50字以内）")
    private String applyReason;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    /**
     * 车辆id（分配后有大于0的记录，未分配默认0）
     */
    @ApiModelProperty(value = "车辆id（分配后有大于0的记录，未分配默认0）")
    private Integer carId;
    /**
     * 司机id（分配后有大于0的记录，未分配默认0）
     */
    @ApiModelProperty(value = "司机id（分配后有大于0的记录，未分配默认0）")
    private Integer driverId;

    /**
     * [审批人员ID列表(有序)]
     */
    @ApiModelProperty(value = "[审批人员ID列表(有序)]")
    private String verifyUserList;

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
