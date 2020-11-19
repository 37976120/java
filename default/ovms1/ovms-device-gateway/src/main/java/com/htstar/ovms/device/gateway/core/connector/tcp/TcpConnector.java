package com.htstar.ovms.device.gateway.core.connector.tcp;

import com.htstar.ovms.device.api.feign.DeviceLastDataFeign;
import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.gateway.core.connector.Session;
import com.htstar.ovms.device.gateway.core.connector.tcp.listener.TcpHeartbeatListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpConnector extends ExchangeTcpConnector {

    private TcpHeartbeatListener tcpHeartbeatListener = null;

    @Setter
    private DeviceLastDataFeign deviceLastDataFeign;
    public void init(){
        /**
         * 心跳检测
         */
        tcpHeartbeatListener = new TcpHeartbeatListener(tcpSessionManager,deviceLastDataFeign);

        Thread heartbeatThread = new Thread(tcpHeartbeatListener, "tcpHeartbeatListener");
        heartbeatThread.setDaemon(true);
        heartbeatThread.start();
    }


    public void destroy() {
        tcpHeartbeatListener.stop();
        for (Session session : tcpSessionManager.getSessions()) {
            session.close();
        }
        tcpSessionManager = null;
    }

    @Override
    public void connect(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        try {
            Session session = tcpSessionManager.createSession(wrapper.getDeviceSn(), ctx);
            session.addSessionListener(tcpHeartbeatListener);
            session.connect();

            tcpSessionManager.addSession(session);
            /** send **/
            session.getConnection().send(wrapper.getBody());
        } catch (Exception e) {
            log.error("TcpConnector connect occur Exception.", e);
        }
    }

    @Override
    public void close(MessageWrapper wrapper) {
        Session session = tcpSessionManager.getSession(wrapper.getDeviceSn());
        if (null == session){
            return;
        }
        session.getConnection().send(wrapper.getBody());
        session.close();
    }

    @Override
    public void heartbeatClient(MessageWrapper wrapper) {
        try {
            tcpSessionManager.updateSession(wrapper.getDeviceSn());
            Session session = tcpSessionManager.getSession(wrapper.getDeviceSn());
            session.getConnection().send(wrapper.getBody());
        } catch (Exception e) {
            log.error("心跳包处理异常", e);
        }
    }

    @Override
    public void responseSendMessage(MessageWrapper wrapper) {
        try {
            Session session = tcpSessionManager.getSession(wrapper.getDeviceSn());
            session.getConnection().send(wrapper.getBody());
        } catch (Exception e) {
            log.error("下发消息异常.", e);
        }
    }

}
