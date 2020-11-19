package com.htstar.ovms.device.gateway.core.connector;

import com.htstar.ovms.device.gateway.core.common.Endpoint;

public interface Session extends Endpoint {

    /**
     * Update the accessed time information for this session.  This method
     * should be called by the context when a request comes in for a particular
     * session, even if the application does not reference it.
     *
     * 更新此会话的访问时间信息。当请求进入特定会话时，上下文应该调用此方法，即使应用程序没有引用它。
     */
    void access();

    /**
     * Inform the listeners about the new session and open connection.
     *
     * 通知侦听器新session 并打开连接。
     */
    void connect();

    /**
     * Perform the internal processing required to invalidate this session,
     * without triggering an exception if the session has already expired.
     * then close the connection.
     * 执行使此会话无效所需的内部处理，如果会话已过期，则不触发异常。然后关闭连接。
     */
    void close();

    /**
     * Release all object references, and initialize instance variables, in
     * preparation for reuse of this object.
     * 释放所有对象引用并初始化实例变量，以准备重用此对象。
     */
    void recycle();

    boolean expire();

}
