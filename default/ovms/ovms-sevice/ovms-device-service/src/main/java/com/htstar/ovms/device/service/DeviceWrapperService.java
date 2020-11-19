package com.htstar.ovms.device.service;

import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/11
 * Company: 航通星空
 * Modified By:
 */
public interface DeviceWrapperService {

    MessageWrapper handlePackageWrapper(ProtoTransferDTO protoTransferDTO);
}
