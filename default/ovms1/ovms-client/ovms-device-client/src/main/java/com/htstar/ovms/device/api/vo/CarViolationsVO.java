package com.htstar.ovms.device.api.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
public class CarViolationsVO {


    @ApiModelProperty(value="id")
    private Integer id;
    /**
     * 车牌号
     */
    @ApiModelProperty(value="车牌号")
    private String licCode;
    /**
     * 设备号
     */
    @ApiModelProperty(value="设备号")
    private String deviceSn;
    /**
     * 开始时间
     */
    @ApiModelProperty(value="开始时间")
    private String staTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value="结束时间")
    private String endTime;
    /**
     * 开始地址
     */
    @ApiModelProperty(value="开始地址")
    private String staAddr;
    /**
     * 结束地址
     */
    @ApiModelProperty(value="结束地址")
    private String endAddr;

    /**
     * 行程开始时GPS点，格式：纬度,精度
     */
    @ApiModelProperty(value = "行程开始时GPS点，格式：纬度,精度")
    private String staLatlon;
    /**
     * 行程结束时GPS点，格式：纬度,精度
     */
    @ApiModelProperty(value = "行程结束时GPS点，格式：纬度,精度")
    private String endLatlon;




}
