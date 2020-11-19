package com.htstar.ovms.device.protoco;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: OBD工况协议
 * Author: flr
 * Date: Created in 2020/6/22
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ObdConditionProcoto {

    /**
     * 设备序列号
     */
    private String deviceSn;

    /**
     * 系统接收时间CST
     */
    protected LocalDateTime revTime;

    /**
     * 统计数据包	STAT_DATA
     */
    private ObdStatDataTp obdStatDataTp;

    /**
     * 采集间隔	U16	2	工况采集间隔
     * 范围2 ~ 600
     */
    private Integer collecteInterval;

    /**
     * 工况类型个数	U8	1	范围: 1~10个
     */
    private Integer conTypeCount;

    /**
     * 工况类型数组	U16[x]	2*x	x = con_type_count
     * 工况类型，详细定义参见附录9.2
     * 特别说明：
     * 当设置的采集工况类型终端不支持时，在上传工况数据中，终端不支持的工况类型不列入此工况类型数组中，
     * 后续的工况数据字段也无其数据
     */
    private List<String> conTypeList;

    /**
     * 工况数据的包数	U8	1	范围：1 ~ 30
     */
    private Integer conGroupCount;


    /**
     * 每包工况数据的长度	U8	1
     */
    private Integer conGroupSize;


    /**
     * 工况数据	x	x	x = con_group_count * con_group_size
     * 工况数据，详细定义参见附录9.2
     */
    private Integer conDat;


    /**
     * 非协议携带（转化值）
     */
    private List<ConditionModel> conditionModelList;

    /**
     * 最后工况
     */
    private ConditionModel lastCondition;

    private Integer speed;

    private Integer rpm;
}
