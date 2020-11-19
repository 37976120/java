package com.htstar.ovms.device.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/2912:27
 */
@Data
public class DeviceSimExportDTO implements Serializable {
    @ApiModelProperty(value = "企业id")
    private Integer etpId = null;
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

    @ApiModelProperty(value = "多个id")
    private String ids;
}
