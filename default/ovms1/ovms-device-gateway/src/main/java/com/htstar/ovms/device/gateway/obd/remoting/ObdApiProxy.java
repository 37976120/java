package com.htstar.ovms.device.gateway.obd.remoting;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import com.htstar.ovms.device.api.feign.DeviceMsgFeign;
import com.htstar.ovms.device.gateway.core.invoke.ApiProxy;
import com.htstar.ovms.device.gateway.core.message.SystemMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description: 数据上行调取后端微服务
 * Author: flr
 * Company: 航通星空
 * Modified By:
 */
@Slf4j
@Component
@AllArgsConstructor
public class ObdApiProxy implements ApiProxy {

    private final DeviceMsgFeign deviceMsgFeign;

    @Override
    public MessageWrapper invoke(SystemMessage sMsg, ProtoTransferDTO message) {
        return deviceMsgFeign.handlePackageWrapper(message,SecurityConstants.FROM_IN);
    }
}
