package com.htstar.ovms.device.handle.event;

import com.htstar.ovms.device.protoco.ObdAlarmProtoco;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * Description: OBD警情协议上传监听
 * Author: flr
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
public class ObdAlarmProcotoEvent extends ApplicationEvent {

    @Getter
    @Setter
    private ObdAlarmProtoco obdAlarmProtoco;

    public ObdAlarmProcotoEvent(Object source, ObdAlarmProtoco obdAlarmProtoco) {
        super(source);
        this.obdAlarmProtoco = obdAlarmProtoco;
    }
}
