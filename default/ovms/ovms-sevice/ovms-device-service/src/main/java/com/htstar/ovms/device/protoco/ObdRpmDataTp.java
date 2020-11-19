package com.htstar.ovms.device.protoco;

import lombok.Data;

import java.util.List;

/**
 * Description: OBD RPM数据
 * Author: flr
 * Date: Created in 2020/6/15
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ObdRpmDataTp {

    /**
     * byte 数据指针位置
     */
    private int index;

    /**
     * RPM个数
     */
    private int rpmCount;

    /**
     * 发动机转速数组
     */
    private List<Integer> rpmList;

    /**
     * 发动机转速
     */
    private Integer rpm;
}
