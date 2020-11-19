package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import com.htstar.ovms.device.api.entity.DeviceCommand;
import com.htstar.ovms.device.api.req.ObdSetGpsReq;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/17
 * Company: 航通星空
 * Modified By:
 */
public interface DeviceCommandService extends IService<DeviceCommand> {
    R setObdGps(ObdSetGpsReq req);

    Integer getGatewayStatusById(Long id);

    DeviceCommand getEnt(String deviceSn, int protocoSeq);

    boolean crackGpsSetReply(DeviceCommand deviceCommand, ProtoTransferDTO dto);
}
