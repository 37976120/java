package com.htstar.ovms.device.service;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
public interface GpsService {

    /**
     * 逆袭 GPS位置
     * @param lat
     * @param lng
     * @return
     */
    String getGpsAddr(Double lat, Double lng);
}
