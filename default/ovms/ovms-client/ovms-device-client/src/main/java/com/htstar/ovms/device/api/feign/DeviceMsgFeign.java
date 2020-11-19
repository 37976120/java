package com.htstar.ovms.device.api.feign;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = "deviceMsgHandFeign", value = ServiceNameConstants.DEVICE_SERVICE)
public interface DeviceMsgFeign {

    /**
     * Description: 处理网关装发数据
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    @PostMapping("/deviceMsg/handlePackageWrapper")
    MessageWrapper handlePackageWrapper(@RequestBody ProtoTransferDTO protoTransferDTO, @RequestHeader(SecurityConstants.FROM) String from);

}
