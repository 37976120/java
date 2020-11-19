package com.htstar.ovms.device.handle.event;

import com.htstar.ovms.device.protoco.ObdGpsDataTp;
import com.htstar.ovms.device.protoco.ObdGpsProtoco;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * Description: OBDGPS协议上传事件
 * Author: flr
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
@Slf4j
public class ObdGpsProcotoEvent extends ApplicationEvent {

    @Getter
    @Setter
    private ObdGpsProtoco obdGpsProtoco;

    public ObdGpsProcotoEvent(Object source, ObdGpsProtoco obdGpsProtoco) {
        super(source);
        this.obdGpsProtoco = obdGpsProtoco;
    }
}
