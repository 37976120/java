package com.htstar.ovms.device.gateway.core.remoting;

import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.gateway.core.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpSender implements Sender {

    private final static Logger logger = LoggerFactory.getLogger(TcpSender.class);

    private Connector tcpConnector;

    public TcpSender(Connector tcpConnector) {
        this.tcpConnector = tcpConnector;
    }

    @Override
    public void sendMessage(MessageWrapper wrapper) throws RuntimeException {
        try {
            tcpConnector.send(wrapper.getDeviceSn(), wrapper.getBody());
        } catch (Exception e) {
            logger.error("TcpSender sendMessage occur Exception!", e);
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public boolean existSession(MessageWrapper wrapper) throws RuntimeException {
        try {
            return tcpConnector.exist(wrapper.getDeviceSn());
        } catch (Exception e) {
            logger.error("TcpSender sendMessage occur Exception!", e);
            throw new RuntimeException(e.getCause());
        }
    }

}
