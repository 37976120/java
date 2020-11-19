package com.htstar.ovms.device.protoco;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: OBD警情协议
 * Author: flr
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ObdAlarmProtoco {

    /**
     * 设备序列号
     */
    private String deviceSn;

    /**
     * 系统接收时间CST
     */
    protected LocalDateTime revTime;

    /**
     * 警情编号	U32	4	终端设备自定义的警情编号，终端可以根据此编号确定中心平台的响应指令
     */
    private Long alarmNo;

    /**
     * 统计数据包	STAT_DATA
     */
    private ObdStatDataTp obdStatDataTp;

    /**
     * GPS数据	GPS_DATA
     */
    private ObdGpsDataTp obdGpsDataTp;

    /**
     * 警情数	U8	1
     */
    private Integer alarmCount;

    /**
     * 警情信息 AlarmDataTp长度为6
     */
    private List<AlarmDataTp> alarmDataTpList;

}
