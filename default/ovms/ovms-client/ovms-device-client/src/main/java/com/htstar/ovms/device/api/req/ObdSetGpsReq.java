package com.htstar.ovms.device.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: 设置OBD GPS请求模型
 * Author: flr
 * Date: Created in 2020/6/17
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ObdSetGpsReq implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="设备序列号")
    private String deviceSn;

    @ApiModelProperty(value="GPS开关：0=关闭；1=开启；")
    private Integer gpsSwitch;

    @ApiModelProperty(value="上传时间间隔 注意：不设置的时候不要传过来")
    private Integer upTimeInterval;

    @ApiModelProperty(value="上传包数 注意：不设置的时候不要传过来")
    private Integer upPackageNum;

}