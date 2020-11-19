package com.htstar.ovms.device.api.feign;/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/10
 * Company: 航通星空
 * Modified By:
 */

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Description:
 * @Author: lw
 * @CreateDate: 2020/7/10 16:17  
 */
@FeignClient(contextId = "deviceFeign", value = ServiceNameConstants.DEVICE_SERVICE)
public interface DeviceFeign {
    /**
     * 检查设备是否可以绑定
     * @param deviceSn
     * @return
     */
    @GetMapping("/device/checkBinding/{deviceSn}")
    R checkBinding(@PathVariable("deviceSn") String deviceSn);
}