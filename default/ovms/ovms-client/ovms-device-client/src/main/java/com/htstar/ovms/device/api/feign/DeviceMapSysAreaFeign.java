package com.htstar.ovms.device.api.feign;

import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.entity.DriverMapAreaCarInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/16
 * Company: 航通星空
 * Modified By:
 */
@FeignClient(contextId = "DeviceMapSysAreaFeign", value = ServiceNameConstants.DEVICE_SERVICE)
public interface DeviceMapSysAreaFeign {
    /**
     * 新增地图车辆关联信息
     * @param driverMapAreaCarInfo
     * @return
     */
     @PostMapping("/driverArea/baseMapAreaInfo")
     R saveBaseMapAreaInfo(@RequestBody DriverMapAreaCarInfo driverMapAreaCarInfo);

    /**
     * 根据车辆id修改地图车辆关联信息
     * @param driverMapAreaCarInfo
     * @return
     */
    @PostMapping("/driverArea/baseUpdateMapAreaInfo")
     R updateBaseMapAreaInfo(@RequestBody DriverMapAreaCarInfo driverMapAreaCarInfo);


    /**
     * 根据车辆id查询地图车辆关联信息  全部字段
     * @param mapCarInfoId
     * @return
     */
    @GetMapping("/driverArea/getByCarInfoId/{mapCarInfoId}")
    DriverMapAreaCarInfo getBymMapCarInfoId(@PathVariable("mapCarInfoId") Integer mapCarInfoId);

    /**
     * 根据车辆id查询所属地图标签  全部字段
     * @param mapCarInfoId
     * @return
     */
    @GetMapping("/driverArea/getByMapCarInfoId/{mapCarInfoId}")
    public R getByMapCarInfoId(@PathVariable("mapCarInfoId") Integer mapCarInfoId);

    /**
     * 根据车辆id删除当前车辆地图标签
     * @param mapCarInfoId
     * @return
     */
    @GetMapping("/driverArea/removeByMapCarInfoId/{mapCarInfoId}")
     boolean removeByMapCarInfoId(@PathVariable("mapCarInfoId") Integer mapCarInfoId);

}
