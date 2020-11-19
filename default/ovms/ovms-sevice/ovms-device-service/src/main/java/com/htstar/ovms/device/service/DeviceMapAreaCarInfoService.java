package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.entity.DriverMapAreaCarInfo;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 地图车辆关联信息
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
public interface DeviceMapAreaCarInfoService extends IService<DriverMapAreaCarInfo> {

    /**
     * 新增地图车辆关联信息
     * @param driverMapAreaCarInfo
     * @return
     */
    R saveBaseMapAreaInfo(DriverMapAreaCarInfo driverMapAreaCarInfo);

    /**
     * 根据车辆id查询所属地图标签
     * @param mapCarInfoId
     * @return
     */
    String getByMapCarInfoId( Integer mapCarInfoId);
}
