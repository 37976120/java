package com.htstar.ovms.device.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/2911:57
 */
@Data
@ApiModel(value = "设备管理导出")
public class DeviceExportDTO implements Serializable {

    @ApiModelProperty(value = "设备序列号")
    private String deviceSn;

    @ApiModelProperty(value = "绑定状态 已绑定：1，未绑定：0")
    private Integer bindStatus = null;

    /**
     * 企业ID
     */
    @ApiModelProperty(value="企业ID")
    private Integer etpId = null;

    @ApiModelProperty(value = "企业名称")
    private String etpName;

    @ApiModelProperty(value = "车牌号/车架号")
    private String licOrFrameCode;

    @ApiModelProperty(value = "绑定账号")
    private String username;

    @ApiModelProperty(value = "绑定时间")
    private String createTime;

    @ApiModelProperty(value = "设备序列号 列表选中")
    private String deviceSns;

    @ApiModelProperty(value = "卡号")
    private String sim;
}
