package com.htstar.ovms.enterprise.handle.event;

import com.htstar.ovms.enterprise.api.entity.ApplyCarOrder;
import com.htstar.ovms.enterprise.api.entity.ApplyOrderTask;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * Description: 用车申请消息事件
 * Author: flr
 * Date: Created in 2020/8/13
 * Company: 航通星空
 * Modified By:
 */
public class ApplyCarOrderPushMsgEvent extends ApplicationEvent {

    @Getter
    @Setter
    private ApplyCarOrder order;

    @Getter
    @Setter
    List<ApplyOrderTask> orderTaskList;

    public ApplyCarOrderPushMsgEvent(Object source, ApplyCarOrder order,List<ApplyOrderTask> orderTaskList) {
        super(source);
        this.order = order;
        this.orderTaskList = orderTaskList;
    }
}