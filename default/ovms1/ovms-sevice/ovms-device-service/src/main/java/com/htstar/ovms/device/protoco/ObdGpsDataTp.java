package com.htstar.ovms.device.protoco;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description: GPS数据 GPS_DATA
 * Author: flr
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ObdGpsDataTp implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * GPS个数
     */
    private int gpsCount;

    /**
     * byte 数据指针位置
     */
    private int index;

    /**
     * GPS数组
     */
    private List<GpsItemTp> gpsItemTpList;

    /**
     * 最后的GPS
     */
    private GpsItemTp lastGpsItemTp;

}
