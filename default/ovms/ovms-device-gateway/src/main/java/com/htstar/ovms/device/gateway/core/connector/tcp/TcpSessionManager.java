package com.htstar.ovms.device.gateway.core.connector.tcp;

import com.htstar.ovms.device.gateway.core.connector.Connection;
import com.htstar.ovms.device.gateway.core.connector.Session;
import com.htstar.ovms.device.gateway.core.connector.api.ExchangeSession;
import com.htstar.ovms.device.gateway.core.connector.api.listener.SessionListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpSessionManager extends ExchangeTcpSessionManager {

    @Override
    public synchronized Session createSession(String deviceSn, ChannelHandlerContext ctx) {
        Session session = sessions.get(deviceSn);
        if (session != null) {
            log.info("设备[{}] Session已经存在！",deviceSn);
            /**
             * 如果在已经建立Connection(1)的Channel上，再建立Connection(2)
             * session.close会将ctx关闭， Connection(2)和Connection(1)的Channel都将会关闭
             * 断线之后再建立连接Connection(3)，由于Session是有一点延迟
             * Connection(3)和Connection(1/2)的Channel不是同一个
             * **/
            // 如果session已经存在则销毁session
            session.close();
            log.info("设备[{}] session 已经被销毁 ",deviceSn);
        }
        log.info("设备[{}]创建新Session,ctx ->{}",deviceSn , ctx.toString());

        session = new ExchangeSession();
        session.setSessionId(deviceSn);
        session.setValid(true);
        session.setMaxInactiveInterval(this.getMaxInactiveInterval());
        session.setCreationTime(System.currentTimeMillis());
        session.setLastAccessedTime(System.currentTimeMillis());
        session.setSessionManager(this);
        session.setConnection(createTcpConnection(session, ctx));
        log.info("设备[{}]创建Session成功！",deviceSn);

        if (null != sessionListeners && !sessionListeners.isEmpty()){
            for (SessionListener listener : sessionListeners) {
                session.addSessionListener(listener);
            }
            log.debug("为设备[{}]添加监听器->{}成功！" , deviceSn ,sessionListeners);
        }
        return session;
    }

    protected Connection createTcpConnection(Session session, ChannelHandlerContext ctx) {
        Connection conn = new TcpConnection(ctx);
        conn.setConnectionId(session.getDeviceSn());
        conn.setSession(session);
        return conn;
    }

}
