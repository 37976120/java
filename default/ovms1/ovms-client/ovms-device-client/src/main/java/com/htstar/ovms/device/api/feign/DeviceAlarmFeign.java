package com.htstar.ovms.device.api.feign;

import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/16
 * Company: 航通星空
 * Modified By:
 */
@FeignClient(contextId = "deviceAlarmFeien", value = ServiceNameConstants.DEVICE_SERVICE)
public interface DeviceAlarmFeign {


    @GetMapping("/devicealarm/getAlarmData/{etpId}/{date}")
    R getAlarmData(@PathVariable("etpId")Integer etpId ,@PathVariable("date")  String date);
}
