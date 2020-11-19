package com.htstar.ovms.device.controller;

import com.htstar.ovms.common.security.annotation.Inner;
import com.htstar.ovms.device.service.DeviceCommandService;
import com.htstar.ovms.device.service.GpsService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/2814:12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/feignGps")
@Api(value = "feignGps", tags = "获取GPS解析位置")
public class GPSController {

    private final GpsService gpsService;
    /**
     * @param lat 纬度
     * @param lng 经度
     * @return
     */
    @Inner
    @GetMapping("/getGpsAddr/{lat}/{lng}")
    String getGpsAddr(@PathVariable("lat")Double lat,@PathVariable("lng")Double lng){
        return gpsService.getGpsAddr(lat,lng);
    }
}
