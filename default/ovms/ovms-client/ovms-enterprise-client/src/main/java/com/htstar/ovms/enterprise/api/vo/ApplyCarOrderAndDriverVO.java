package com.htstar.ovms.enterprise.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.htstar.ovms.enterprise.api.entity.ApplyCarOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author HanGuJi
 * @Description: 车辆和司机排班数据
 * @date 2020/7/1310:45
 */
@Data
public class ApplyCarOrderAndDriverVO implements Serializable {
    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "驾照类型")
    private Integer licenseType;

    @ApiModelProperty(value = "公司名称")
    private String etpName;
    @ApiModelProperty(value = "提示")
    private String remark;
    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "子品牌名称")
    private String carSubBrand;

    @ApiModelProperty(value = "车辆id")
    private Integer carId;

    @ApiModelProperty(value = "司机id")
    private Integer driverId;

    @ApiModelProperty(value = "车辆座位")
    private Integer seateNum;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "车辆级别")
    private Integer carLevel;

    @ApiModelProperty(value = "驾照类型")
    private Integer license_type;

    @ApiModelProperty(value = "车辆排班总数")
    private Integer totals;

    @ApiModelProperty(value = "拿证日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date getLicenseTime;

    @ApiModelProperty(value = "性别 0:女 1:男")
    private Integer sex;
    @ApiModelProperty(value = "一到星期天每个车不可排班的状态 1代表不可排班 0代表可以排班")
    private Integer statusNo;

    @ApiModelProperty(value = "排班结果集")
    private List<ApplyCarOrderDerverVO> carDriverSchedules;
}
