package com.htstar.ovms.device.api.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.device.api.entity.DeviceSim;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/1610:28
 */
@Data
@ApiModel(value = "设备SIM卡传输对象")
public class DeviceSimDTO extends Page {

    @ApiModelProperty(value = "企业id")
    private Integer etpId = null;

    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id")
    private Integer id;

    /**
     * sim卡号
     */
    @ApiModelProperty(value = "sim卡号")
    private String sim;

    @ApiModelProperty(value = "设备序列号")
    private String deviceSn;

    /**
     * 启用时间
     */
    @ApiModelProperty(value = "启用时间")
    private LocalDateTime startTime;
    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    private LocalDateTime endTime;
    /**
     * 导入/创建时间
     */
    @ApiModelProperty(value = "导入/创建时间")
    private LocalDateTime createTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

}
