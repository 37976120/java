package com.htstar.ovms.device.controller;

import com.htstar.ovms.common.security.annotation.Inner;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.service.DeviceWrapperService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/9
 * Company: 航通星空
 * Modified By:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/deviceMsg" )
@Api(value = "deviceMsg", tags = "设备消息处理")
public class DeviceMsgController {

    private final DeviceWrapperService deviceWrapperService;


    /**
     * Description: 处理网关传输的设备消息
     * Author: flr
     * Company: 航通星空
     */
    @PostMapping("/handlePackageWrapper")
    @Inner
    MessageWrapper handlePackageWrapper(@RequestBody ProtoTransferDTO protoTransferDTO){
        return deviceWrapperService.handlePackageWrapper(protoTransferDTO);
    }

}
