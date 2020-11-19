package com.htstar.ovms.device.gateway.core.connector.api;

import com.htstar.ovms.device.gateway.core.connector.Connection;
import com.htstar.ovms.device.gateway.core.connector.SessionManager;
import com.htstar.ovms.device.gateway.core.connector.api.listener.SessionEvent;
import com.htstar.ovms.device.gateway.core.connector.api.listener.SessionListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class ExchangeSession extends SessionValid {

    /**
     * The session identifier of this Session.
     * 此会话的会话标识符。
     */
    private String deviceSn = null;

    private transient List<SessionListener> listeners = new CopyOnWriteArrayList<SessionListener>();

    private transient Connection connection = null;

    /**
     * The Manager with which this Session is associated.
     * 与此会话关联的管理器。
     */
    private transient SessionManager sessionManager = null;

    @Override
    public void access() {
        // Check to see if access is in progress or has previously been called
        if (!isValid) {
            return;
        }
        lastAccessedTime = System.currentTimeMillis();
    }

    @Override
    public void connect() {
        // Check to see if tellNew is in progress or has previously been called
        if (connecting || !isValid) {
            log.debug("the session " + deviceSn + " is connecting or isValid = false!");
            return;
        }
        connecting = true;
        connection.connect();
        addSessionEvent();

        connecting = false;
        log.debug("the session " + deviceSn + " is ready!");
    }

    private void addSessionEvent() {
        SessionEvent event = new SessionEvent(this);
        for (SessionListener listener : listeners) {
            try {
                listener.sessionCreated(event);
                log.info("SessionListener " + listener + " .sessionCreated() is invoked successfully!");
            } catch (Exception e) {
                log.error("addSessionEvent error.", e);
            }
        }
    }

    /**
     * Perform the internal processing required to invalidate this session,
     * without triggering an exception if the session has already expired.
     *
     * 执行使此会话无效所需的内部处理，如果会话已过期，则不触发异常。
     */
    @Override
    public void close() {
        close(true);
    }

    /**
     * Perform the internal processing required to invalidate this session,
     * without triggering an exception if the session has already expired.
     *
     * @param notify Should we notify listeners about the demise of this session?
     */
    void close(boolean notify) {
        // Check to see if close is in progress or has previously been called
        if (closing || !isValid) {
            log.debug("the session " + deviceSn + " is closing or isValid = false!");
            return;
        }
        synchronized (this) {
            // Check again, now we are inside the sync so this code only runs
            // once
            // Double check locking - closing and isValid need to be volatile
            if (closing || !isValid) {
                log.debug("the session " + deviceSn + " is closing or isValid = false!");
                return;
            }
            // Mark this session as "being closed"
            closing = true;
            if (notify) {
                SessionEvent event = new SessionEvent(this);
                for (SessionListener listener : listeners) {
                    try {
                        listener.sessionDestroyed(event);
                        log.debug("SessionListener " + listener + " .sessionDestroyed() is invoked successfully!");
                    } catch (Exception e) {
                        log.error("sessionDestroyed error! " + e);
                    }
                }
            }
            setValid(false);

            connection.close();

            recycle();
            // We have completed close of this session
            closing = false;
            log.debug("the session " + deviceSn + " have been destroyed!");
        }
    }

    /**
     * Release all object references, and initialize instance variables, in
     * preparation for reuse of this object.
     */
    @Override
    public void recycle() {
        log.debug("the session " + deviceSn + " is recycled!");
        // Remove this session from our manager's active sessions
        sessionManager.removeSession(this);

        // Reset the instance variables associated with this Session
        listeners.clear();
        listeners = null;
        creationTime = 0L;
        connecting = false;
        closing = false;
        deviceSn = null;
        lastAccessedTime = 0L;
        maxInactiveInterval = -1;
        isValid = false;
        sessionManager = null;
    }

    @Override
    public boolean expire() {
        //A negative time indicates that the session should never time out.
        if (maxInactiveInterval < 0){
            return false;
        }


        long timeNow = System.currentTimeMillis();
        int timeIdle = (int) ((timeNow - lastAccessedTime) / 1000L);
        if (timeIdle >= maxInactiveInterval) {
            return true;
        }
        return false;
    }

    /**
     * Add a session event listener to this component.
     */
    @Override
    public void addSessionListener(SessionListener listener) {
        if (null == listener) {
            throw new IllegalArgumentException("addSessionListener listener");
        }
        listeners.add(listener);
    }

    /**
     * Remove a session event listener from this component.
     */
    @Override
    public void removeSessionListener(SessionListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("removeSessionListener listener");
        }
        listeners.remove(listener);
    }

    @Override
    public void setSessionId(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    @Override
    public String getDeviceSn() {
        return deviceSn;
    }

    @Override
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }
}
