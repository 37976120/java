package com.htstar.ovms.device.api.feign;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Description: 设备最新状态
 * Author: flr
 * Date: Created in 2020/6/20
 * Company: 航通星空
 * Modified By:
 */
@FeignClient(contextId = "deviceLastDataFeign", value = ServiceNameConstants.DEVICE_SERVICE)
public interface DeviceLastDataFeign {

    /**
     * Description: 心跳到期监听更新设备在线状态
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    @GetMapping("/devicelastdata/heartBeatOnline/{deviceSn}/{online}")
    void heartBeatOnline(@PathVariable("deviceSn") String deviceSn, @PathVariable("online") Integer online, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 通过sn查询设备最新状态
     */
    @ApiModelProperty(value = "通过carId查询", notes = "通过carId查询")
    @GetMapping("/devicelastdata/byCarId/{carId}")
    public R byCarId(@PathVariable("carId") String carId);
}
