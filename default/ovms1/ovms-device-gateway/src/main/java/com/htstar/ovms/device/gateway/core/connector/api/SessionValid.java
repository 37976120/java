package com.htstar.ovms.device.gateway.core.connector.api;

import com.htstar.ovms.device.gateway.core.connector.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SessionValid implements Session {

    /**
     * We are currently processing a session create, so bypass certain
     * IllegalStateException tests. NOTE: This value is not included in the
     * serialized version of this object.
     *
     * transient：将不需要序列化的属性前添加关键字transient，序列化对象的时候，这个属性就不会被序列化
     *我们当前正在处理会话创建，因此请绕过某些IllegalStateException测试。注意：此值不包含在此对象的序列化版本中。
     *
     */
    protected transient volatile boolean connecting = false;

    /**
     * We are currently processing a session expiration, so bypass certain
     * IllegalStateException tests. NOTE: This value is not included in the
     * serialized version of this object.
     *
     * 我们当前正在处理会话过期，因此请绕过某些非法状态异常测试。注意：此值不包含在此对象的序列化版本中。
     */
    protected transient volatile boolean closing = false;

    /**
     * The time this session was created, in milliseconds since midnight,
     * 创建此会话的时间，从午夜开始以毫秒为单位，
     * January 1, 1970 GMT.
     */
    protected long creationTime = 0L;

    /**
     * The last accessed time for this Session.
     * 上次访问此会话的时间。
     */
    protected volatile long lastAccessedTime = creationTime;

    /**
     * Flag indicating whether this session is valid or not.
     * 指示此会话是否有效的标志。
     */
    protected volatile boolean isValid = false;

    /**
     * The maximum time interval, in seconds, between client requests before the
     * container may invalidate this session. A negative time indicates that the
     * session should never time out.
     *
     * 容器可能使此会话无效之前，客户端请求之间的最大时间间隔（秒）。负时间表示会话不应该超时。
     */
    protected int maxInactiveInterval = 5 * 60;

    /**
     * The collection of user data attributes associated with this Session.
     */
    protected Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    @Override
    public boolean isValid() {
        if (closing) {
            return true;
        }
        return (isValid);
    }

    @Override
    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    @Override
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setAttribute(String name, Object value) {
        if (!isValid()) {
            throw new IllegalStateException("[setAttribute]Session already invalidated");
        }

        if (name == null)
            return;

        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        if (!isValid()) {
            throw new IllegalStateException("[getAttribute]Session already invalidated");
        }

        if (name == null)
            return null;

        return (attributes.get(name));
    }
}
