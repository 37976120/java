package com.htstar.ovms.device.mongo.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

@Data
@Document("deviceUpOriData")
public class DeviceUpOriDataMG {
    //协议方法名称
    private String methodName;
    //内容数据
    private String dataHex;
    //设备序列号
    private String deviceSn;

    private LocalDateTime revCstTime;
}
