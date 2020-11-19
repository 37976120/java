package com.htstar.ovms.device.handle.event;

import com.htstar.ovms.device.protoco.ObdConditionProcoto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * Description: OBD工况事件
 * Author: flr
 * Date: Created in 2020/6/22
 * Company: 航通星空
 * Modified By:
 */
public class ObdConditionProcotoEvent extends ApplicationEvent {

    @Getter
    @Setter
    private ObdConditionProcoto obdConditionProcoto;

    public ObdConditionProcotoEvent(Object source, ObdConditionProcoto obdConditionProcoto) {
        super(source);
        this.obdConditionProcoto = obdConditionProcoto;
    }
}
