package com.htstar.ovms.device.gateway.core.invoke;

import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import com.htstar.ovms.device.gateway.core.message.SystemMessage;

/**
 * Description: 后端服务代理类
 * Author: flr
 * Company: 航通星空
 * Modified By:
 */
public interface ApiProxy {

    MessageWrapper invoke(SystemMessage sMsg, ProtoTransferDTO message);

}

