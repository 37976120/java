package com.htstar.ovms.device.gateway.core.connector.tcp;

import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.gateway.core.connector.Session;
import com.htstar.ovms.device.gateway.core.connector.api.ExchangeConnector;
import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;

public abstract class ExchangeTcpConnector<T> extends ExchangeConnector<T> {

    @Setter
    protected TcpSessionManager tcpSessionManager = null;

    public abstract void connect(ChannelHandlerContext ctx, MessageWrapper wrapper);

    public abstract void close(MessageWrapper wrapper);

    /**
     * 会话心跳
     *
     * @param wrapper
     */
    public abstract void heartbeatClient(MessageWrapper wrapper);

    /**
     * 接收客户端消息通知响应
     *
     * @param wrapper
     */
    public abstract void responseSendMessage(MessageWrapper wrapper);

    @Override
    public void send(String deviceSn, T message) throws Exception {
        super.send(tcpSessionManager, deviceSn, message);
    }

    @Override
    public boolean exist(String deviceSn) throws Exception {
        Session session = tcpSessionManager.getSession(deviceSn);
        return session != null ? true : false;
    }

    public void setTcpSessionManager(TcpSessionManager tcpSessionManager) {
        this.tcpSessionManager = tcpSessionManager;
    }
}
