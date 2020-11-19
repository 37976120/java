package com.htstar.ovms.enterprise.api.req;

import com.htstar.ovms.enterprise.api.vo.PassengerVO;
import com.htstar.ovms.enterprise.api.vo.PhotoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 提交公车申请
 * Author: flr
 * Date: Created in 2020/7/1
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ApplyCarOrderReq implements Serializable {
    private static final long serialVersionUID = 1L;



    /**
     * 申请类型:0=公车申请；1=私车公用；2=直接派车；
     */
    @ApiModelProperty(value="申请类型:0=公车申请；1=私车公用；2=直接派车；",required = true)
    private Integer applyType;

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
     * 驾驶证（图片路径，逗号隔开）
     */
    @ApiModelProperty(value="驾驶证（图片路径，逗号隔开）")
    private String driveLicense;

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
     * 流程ID
     */
    @ApiModelProperty(value="流程ID")
    private Integer processId;

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
     * [审批人员ID列表(有序)]
     */
    @ApiModelProperty(value="[审批人员ID列表(列表','隔开)]")
    private String verifyUserList;
    /**
     * [抄送人员ID列表(有序)]
     */
    @ApiModelProperty(value="[抄送人员ID(列表','隔开)]")
    private String ccUserList;
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
    @ApiModelProperty(value="内部乘车人员ID(列表','隔开)")
    private String innerPassengerList;
    /**
     * 外部乘车人列表[{name:'姓名(必填)',mobile:'手机号'}]
     */
    @ApiModelProperty(value="外部乘车人列表")
    private List<PassengerVO> outPassengerList;
    /**
     * 照片列表[{photoUrl:'图片路径'}]
     */
    @ApiModelProperty(value="照片列表")
    private List<PhotoVO> photoList;
    @ApiModelProperty(value="车辆id（直接派车）")
    private Integer carId;
    @ApiModelProperty(value="司机id（直接派车）")
    private Integer driverId;
}
