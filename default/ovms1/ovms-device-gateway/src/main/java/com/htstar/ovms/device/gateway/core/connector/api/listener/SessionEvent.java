package com.htstar.ovms.device.gateway.core.connector.api.listener;

import com.htstar.ovms.device.gateway.core.connector.Session;

import java.util.EventObject;

/**
 * TSM应用程序中Session更改的事件通知的类。
 */
public class SessionEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public SessionEvent(Object source) {
        super(source);
    }

    /**
     * Return the session that changed.
     */
    public Session getSession() {
        return (Session) super.getSource();
    }
}
