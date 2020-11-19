package com.htstar.ovms.device.handle.listener;

import com.htstar.ovms.common.core.util.ByteDataUtil;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import com.htstar.ovms.device.handle.event.DeviceUpOriDataEvent;
import com.htstar.ovms.device.handle.event.ObdGpsProcotoEvent;
import com.htstar.ovms.device.mongo.model.DeviceUpOriDataMG;
import com.htstar.ovms.device.mongo.repository.DeviceUpOriDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/23
 * Company: 航通星空
 * Modified By:
 */
@Component
@Slf4j
public class DeviceUpOriDataListener {

    @Autowired
    private DeviceUpOriDataRepository deviceUpOriDataRepository;

    @EventListener
    @Async
    public void onApplicationEvent(DeviceUpOriDataEvent deviceUpOriDataEvent) {
        ProtoTransferDTO dto = deviceUpOriDataEvent.getProtoTransferDTO();

        DeviceUpOriDataMG deviceUpOriData = new DeviceUpOriDataMG();
        BeanUtils.copyProperties(dto,deviceUpOriData);

        StringBuffer sb = new StringBuffer(ByteDataUtil.bytesToHex(dto.getHeader()));
        sb.append(ByteDataUtil.bytesToHex(dto.getData()));
        sb.append(ByteDataUtil.bytesToHex(dto.getTail()));

        deviceUpOriData.setDataHex(sb.toString());

        deviceUpOriDataRepository.save(deviceUpOriData);
    }
}
