package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.htstar.ovms.enterprise.api.constant.ProcessTypeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 车-设备 中间表
 *
 * @author lw
 * @date 2020-06-28 16:48:51
 */
@Data
@TableName("car_device")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车-设备 中间表")
public class CarDevice extends Model<CarDevice> {
private static final long serialVersionUID = 1L;

    /**
     * 车Id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="车Id")
    private Integer carId;
    /**
     * 设备号
     */
    @ApiModelProperty(value="设备号")
//    @TableId(value = "device_sn")
    private String deviceSn;
    /**
     * 绑定时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="绑定时间")
    private LocalDateTime createTime;


}
