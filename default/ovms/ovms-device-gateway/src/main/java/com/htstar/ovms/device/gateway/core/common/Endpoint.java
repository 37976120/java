package com.htstar.ovms.device.gateway.core.common;
import com.htstar.ovms.device.gateway.core.connector.Connection;
import com.htstar.ovms.device.gateway.core.connector.SessionManager;
import com.htstar.ovms.device.gateway.core.connector.api.listener.SessionListener;

public interface Endpoint extends Node {

    /**
     * @param connection
     */
    void setConnection(Connection connection);

    Connection getConnection();

    /**
     * @param deviceSn
     */
    void setSessionId(String deviceSn);

    String getDeviceSn();

    /**
     * @param sessionManager
     */
    void setSessionManager(SessionManager sessionManager);

    SessionManager getSessionManager();

    /**
     * Add a session event listener to this component.
     */
    void addSessionListener(SessionListener listener);

    /**
     * Remove a session event listener from this component.
     */
    void removeSessionListener(SessionListener listener);
}
