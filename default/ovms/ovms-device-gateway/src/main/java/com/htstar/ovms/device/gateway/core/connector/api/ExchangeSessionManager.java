package com.htstar.ovms.device.gateway.core.connector.api;

import com.htstar.ovms.device.gateway.core.connector.Session;
import com.htstar.ovms.device.gateway.core.connector.SessionManager;
import com.htstar.ovms.device.gateway.core.connector.api.listener.SessionListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class ExchangeSessionManager implements SessionManager {

    /**
     * 默认 timeout 5min
     */
    private int maxInactiveInterval = 5 * 60;

    protected List<SessionListener> sessionListeners = null;

    public void setSessionListeners(List<SessionListener> sessionListeners) {
        this.sessionListeners = sessionListeners;
    }

    /**
     * 此管理器的当前活动会话集，由会话标识符键入。
     */
    protected Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public synchronized void addSession(Session session) {
        if (null == session) {
            return;
        }
        sessions.put(session.getDeviceSn(), session);
        log.info("添加{}，到sessions集合ConcurrentHashMap，当前服务器设备会话数量：{}",session.getDeviceSn(),sessions.size());
    }

    @Override
    public synchronized void updateSession(String deviceSn) {
        Session session = sessions.get(deviceSn);
        session.setLastAccessedTime(System.currentTimeMillis());

        sessions.put(deviceSn, session);
    }

    /**
     * Remove this Session from the active Sessions for this Manager.
     */
    @Override
    public synchronized void removeSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("session is null!");
        }
        removeSession(session.getDeviceSn());
    }

    @Override
    public synchronized void removeSession(String deviceSn) {
        sessions.remove(deviceSn);
        log.debug("remove the session " + deviceSn + " from sessions!");
    }

    @Override
    public Session getSession(String deviceSn) {
        return sessions.get(deviceSn);
    }

    @Override
    public Session[] getSessions() {
        return sessions.values().toArray(new Session[0]);
    }

    @Override
    public Set<String> getSessionKeys() {
        return sessions.keySet();
    }

    @Override
    public int getSessionCount() {
        return sessions.size();
    }


    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

}
