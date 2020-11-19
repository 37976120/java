package com.htstar.ovms.device.mongo.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/22
 * Company: 航通星空
 * Modified By:
 */
@Data
@Document("obdCondition")
public class ObdConditionMG {

    /**
     * 设备编号
     */
    private String deviceSn;

    /**
     * 接收时间，计算机本地时间
     */
    private LocalDateTime rcvTime;



    /**
     * PID号 ox210c
     */
    private String pidKey;



    /**
     * 数据值
     */
    private String conValue;

}
