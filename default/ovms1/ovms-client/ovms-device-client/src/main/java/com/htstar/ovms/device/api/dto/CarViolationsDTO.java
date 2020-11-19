package com.htstar.ovms.device.api.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 18:39:11
 */
@Data
@TableName("car_violations")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class CarViolationsDTO extends Page {
private static final long serialVersionUID = 1L;



    /**
     * 设备号
     */
    @ApiModelProperty(value="设备号")
    private String deviceSn;

    /**
     * 车牌号
     */
    @ApiModelProperty(value="车牌号")
    private String licCode;
    /**
     * 开始时间
     */
    @ApiModelProperty(value="开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value="结束时间")
    private String endTime;

    private Integer etpId;
    @ApiModelProperty(value="排班id")
    private String id;

    @ApiModelProperty(value="设备号承载")
    private String nuMber;


}
