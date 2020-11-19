package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公车申请事件详情
 *
 * @author htxk
 * @date 2020-07-13 17:17:58
 */
@Data
@TableName("apply_car_order_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公车申请事件详情")
public class ApplyCarOrderDetail extends Model<ApplyCarOrderDetail> {
private static final long serialVersionUID = 1L;

    /**
     * 公车申请事件id(主键，不自增)
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="公车申请事件id(主键，不自增)")
    private Integer orderId;
    /**
     * 车牌号（私车公用时有）
     */
    @ApiModelProperty(value="车牌号（私车公用时有）")
    private String carNumber;
    /**
     * 驾车人
     */
    @ApiModelProperty(value="驾车人")
    private Integer driveUserId;//
    /**
     * 驾驶证（图片路径，逗号隔开）
     */
    @ApiModelProperty(value="驾驶证（图片路径，逗号隔开）")
    private String driveLicense;//
    /**
     * [抄送人员ID列表(有序)]
     */
    @ApiModelProperty(value="[抄送人员ID列表(有序)]")
    private String ccUserList;//
    /**
     * 出发地址（50字内）
     */
    //TODO 出发地改为自动获取
    @ApiModelProperty(value="出发地址（50字内）")
    private String staAddr;
    /**
     * 目的地址(列表','隔开)
     */
    //TODO 目的地改为自动获取
    @ApiModelProperty(value="目的地址(列表','隔开)")
    private String endAddrList;
    /**
     * [内部乘车人员ID列表]
     */
    @ApiModelProperty(value="[内部乘车人员ID列表]")
    private String innerPassengerList;
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

    @ApiModelProperty(value="提车上报信息填充状态：0=未提交；1=已提交；")
    private Integer giveCarStatus;

    @ApiModelProperty(value="回车上报信息状态：0=未提交；1=已提交；")
    private Integer retuenCarStatus;
}
