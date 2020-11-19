package com.htstar.ovms.device.protoco;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * Description:协议中的VSTATE用来描述车载终端的状态信息，U8[4]，字节分别为：S0-S1-S2-S3。初始值的每一位为0。每个字节的意义描述如下
 * Author: flr
 * Date: Created in 2020/6/22
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ObdVStateTp {
    /**
     * 第1个byte（暂时不解析）
     * Bit7	尾气超标告警
     * Bit6	停车未熄火告警
     * Bit5	急减速告警
     * Bit4	急加速告警
     * Bit3	水温过高告警
     * Bit2	超速告警
     * Bit1	拖吊告警
     * Bit0	低电压告警
     */
    private String s0;


    /**
     * 第2个byte（暂时不解析）
     * 7防拆告警
     * 6碰撞告警
     * 5紧急告警
     * 4疲劳驾驶
     * 3急转弯
     * 2急变道
     * 1上电（Power on）
     * 0转速过高告警
     */
    private String s1;


    /**
     * 8第3个byte
     * 7MIL故障告警
     * 6OBD剪线告警
     * 5断电告警
     * 4无GPS设备接入
     * 3隐私状态
     * 2点火状态
     * 1非法点火告警
     * 0非法进入告警
     */
    private String s2;

    /**
     * 由s2中解析的点火状态：0=未点火，1=点火
     */
    private short fireState;


    /**
     * 第4个byte（暂时不解析）
     * 7备用
     * 6备用
     * 5备用
     * 4备用
     * 3震动告警
     * 2危险驾驶报警
     * 1未刷卡报警
     * 0未锁车报警
     */
    private String s3;

    @JsonIgnore
    private int index;
}
