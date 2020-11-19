package com.htstar.ovms.device.handle.event;

import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import com.htstar.ovms.device.mongo.model.DeviceUpOriDataMG;
import com.htstar.ovms.device.protoco.ObdAlarmProtoco;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * Description: 原始上传数据
 * Author: flr
 * Date: Created in 2020/6/23
 * Company: 航通星空
 * Modified By:
 */
public class DeviceUpOriDataEvent extends ApplicationEvent {

    @Getter
    @Setter
    private ProtoTransferDTO protoTransferDTO;

    public DeviceUpOriDataEvent(Object source, ProtoTransferDTO protoTransferDTO) {
        super(source);
        this.protoTransferDTO = protoTransferDTO;
    }
}