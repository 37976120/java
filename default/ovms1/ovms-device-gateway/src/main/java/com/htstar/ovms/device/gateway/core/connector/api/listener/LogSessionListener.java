package com.htstar.ovms.device.gateway.core.connector.api.listener;

import cn.hutool.core.util.StrUtil;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.device.api.constant.DeviceOnlineConstant;
import com.htstar.ovms.device.api.feign.DeviceLastDataFeign;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: 日志监听（可选）-->做安全拦截
 * Author: flr
 * Company: 航通星空
 * Modified By:
 */
@Slf4j
public class LogSessionListener implements SessionListener {

    private DeviceLastDataFeign deviceLastDataFeign;

    public LogSessionListener(DeviceLastDataFeign deviceLastDataFeign){
        this.deviceLastDataFeign = deviceLastDataFeign;
    }

    @Override
    public void sessionCreated(SessionEvent se) {
        if(StrUtil.isNotBlank(se.getSession().getDeviceSn())){
            deviceLastDataFeign.heartBeatOnline(se.getSession().getDeviceSn(), DeviceOnlineConstant.ONLINE, SecurityConstants.FROM_IN);
        }
        log.info("设备[{}] session 已经被创建 ",se.getSession().getDeviceSn());
    }

    @Override
    public void sessionDestroyed(SessionEvent se) {
        if (StrUtil.isNotBlank(se.getSession().getDeviceSn())){
            deviceLastDataFeign.heartBeatOnline(se.getSession().getDeviceSn(), DeviceOnlineConstant.OFFLINE, SecurityConstants.FROM_IN);
        }

        log.info("设备[{}] session 已经被销毁 ",se.getSession().getDeviceSn());
    }
}
