package com.htstar.ovms.device.api.feign;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.device.api.entity.DeviceCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "deviceCommandFeign", value = ServiceNameConstants.DEVICE_SERVICE)
public interface DeviceCommandFeign {

    /**
     * Description: 网关处理命令结果
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    @PostMapping("/deviceCommand/handleGatewayResult")
    void handleGatewayResult(@RequestBody DeviceCommand deviceCommand, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * Description: 获取命令的网关处理结果
     * @param seq
     * @param from
     * @return
     */
    @GetMapping("/deviceCommand/getGatewayStatus/{seq}")
    Integer getGatewayStatus(@PathVariable("seq")Long seq, @RequestHeader(SecurityConstants.FROM) String from);
}
