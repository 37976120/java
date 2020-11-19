package com.htstar.ovms.device.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
* @Description:    OBD协议业务模型（解包以后留下的业务数据）
* @Author:         范利瑞
* @CreateDate:     2020/3/20 15:40
*/
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObdPackageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    //协议方法名称
    protected String methodName;
    //内容数据
    protected byte[] data;
    //设备序列号
    protected String deviceSn;

//    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    protected Date revUtcTime;
}
