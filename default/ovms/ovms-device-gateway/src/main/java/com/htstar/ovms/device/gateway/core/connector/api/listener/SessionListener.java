package com.htstar.ovms.device.gateway.core.connector.api.listener;

import java.util.EventListener;

/**
 * 此接口的实现会收到TSM应用程序中活动会话列表更改的通知。要接收通知事件，必须在TSM应用程序的部署描述符中配置实现类。
 */
public interface SessionListener extends EventListener {

    /**
     * 创建会话的通知。
     *
     * @param se the notification event
     */
    void sessionCreated(SessionEvent se);

    /**
     * 会话即将失效的通知。
     *
     * @param se the notification event
     */
    void sessionDestroyed(SessionEvent se);
}
