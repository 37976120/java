package com.htstar.ovms.device.api.feign;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author HanGuJi
 * @Description: 获取gps解析地址
 * @date 2020/6/2814:21
 */
@FeignClient(contextId = "deviceGpsFeign", value = ServiceNameConstants.DEVICE_SERVICE)
public interface DeviceGpsFeign {

    /**
     *  根据经纬度 解析地理位置
     * @param lat 纬度
     * @param lng 经度
     * @return
     */
    @GetMapping("/feignGps/getGpsAddr/{lat}/{lng}")
    String getGpsAddr(@PathVariable("lat")Double lat,@PathVariable("lng")Double lng,@RequestHeader(SecurityConstants.FROM) String from);
}
