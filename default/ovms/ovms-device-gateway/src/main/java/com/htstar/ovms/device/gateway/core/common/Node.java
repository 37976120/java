package com.htstar.ovms.device.gateway.core.common;

public interface Node {

    /**
     * @param isValid
     */
    void setValid(boolean isValid);

    boolean isValid();

    /**
     * @param creationTime
     */
    void setCreationTime(long creationTime);

    long getCreationTime();

    /**
     * @param lastAccessedTime
     */
    void setLastAccessedTime(long lastAccessedTime);

    long getLastAccessedTime();

    /**
     * @param maxInactiveInterval
     */
    void setMaxInactiveInterval(int maxInactiveInterval);

    int getMaxInactiveInterval();

    /**
     * @param name
     * @param value
     */
    void setAttribute(String name, Object value);

    /**
     * Return the object bound with the specified name in this session, or
     * <code>null</code> if no object is bound with that name.
     *
     * @param name Name of the attribute to be returned
     * @throws IllegalStateException if this method is called on an invalidated session
     *
     * 在此会话中返回用指定名称绑定的对象，如果没有用该名称绑定的对象，则返回空值。
     */
    Object getAttribute(String name);
}
