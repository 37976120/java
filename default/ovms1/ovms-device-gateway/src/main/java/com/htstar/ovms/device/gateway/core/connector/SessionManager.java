package com.htstar.ovms.device.gateway.core.connector;

import java.util.Set;

public interface SessionManager {

    /**
     * 添加指定session
     *
     * @param session
     */
    void addSession(Session session);

    void updateSession(String deviceSn);

    /**
     * 删除指定session
     *
     * @param session
     */
    void removeSession(Session session);

    /**
     * 删除指定session
     *
     * @param deviceSn
     */
    void removeSession(String deviceSn);

    /**
     * 根据指定sessionId获取session
     *
     * @param deviceSn
     * @return
     */
    Session getSession(String deviceSn);

    /**
     * 获取所有的session
     *
     * @return
     */
    Session[] getSessions();

    /**
     * 获取所有的session的id集合
     *
     * @return
     */
    Set<String> getSessionKeys();

    /**
     * 获取所有的session数目
     *
     * @return
     */
    int getSessionCount();

    /**
     * Return the default maximum inactive interval (in seconds)
     * for Sessions created by this Manager.
     */
    int getMaxInactiveInterval();

    /**
     * Set the default maximum inactive interval (in seconds)
     * for Sessions created by this Manager.
     *
     * @param interval The new default value
     */
    void setMaxInactiveInterval(int interval);
}
