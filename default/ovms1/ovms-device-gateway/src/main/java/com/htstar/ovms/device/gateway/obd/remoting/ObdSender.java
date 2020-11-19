package com.htstar.ovms.device.gateway.obd.remoting;

import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.gateway.core.connector.tcp.TcpConnector;
import com.htstar.ovms.device.gateway.core.remoting.Sender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObdSender implements Sender {


    private TcpConnector tcpConnector;

    public ObdSender(TcpConnector tcpConnector){
        this.tcpConnector = tcpConnector;
    }

    @Override
    public void sendMessage(MessageWrapper messageWrapper) throws RuntimeException {
        try {
            tcpConnector.send(messageWrapper.getDeviceSn(), messageWrapper.getBody());
        } catch (Exception e) {
            log.error("ObdSender发送消息运行时异常!", e);
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public boolean existSession(MessageWrapper messageWrapper) throws RuntimeException {
        try {
            return tcpConnector.exist(messageWrapper.getDeviceSn());
        } catch (Exception e) {
            log.error("ObdSender发送消息运行时异常!", e);
            throw new RuntimeException(e.getCause());
        }
    }

}
