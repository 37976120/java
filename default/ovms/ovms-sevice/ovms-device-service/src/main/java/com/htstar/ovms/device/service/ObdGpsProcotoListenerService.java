package com.htstar.ovms.device.service;

import com.htstar.ovms.device.protoco.GpsItemTp;
import com.htstar.ovms.device.protoco.ObdGpsProtoco;
import com.htstar.ovms.device.protoco.ObdStatDataTp;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/8/1310:49
 */
public interface ObdGpsProcotoListenerService {

    /**
     * 检测驶入驶出进行推送
     */
    void ObdGpsProcotoListenerMsgPush(String deviceSn,
                                      GpsItemTp lastGps,
                                      ObdGpsProtoco obdGpsProtoco,
                                      int tripId,
                                      ObdStatDataTp obdStatDataTp,
                                      int userId);
}
