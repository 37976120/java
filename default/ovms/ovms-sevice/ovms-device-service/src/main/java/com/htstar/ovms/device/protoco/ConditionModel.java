package com.htstar.ovms.device.protoco;

import lombok.Data;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/22
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ConditionModel {

    //PID号	PID名称	数据类型	长度	单位

    /**
     * PID号 ox210c
     */
    private String pidKey;


    /**
     * PID名称
     */
    private String pidName;

    /**
     * 数据类型：
     * 1=U8[4];
     * 2=U16[1];
     * 3=U8[2];
     * 4=U8[1];
     * 5=S8[1]
     *
     */
    private int dataType;

    /**
     * 数据长度
     */
    private int byteLenght;


    /**
     * 数据值
     */
    private String conValue;
}

