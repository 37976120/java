package com.htstar.ovms.device.gateway.core.connector.tcp.config;

import com.htstar.ovms.device.gateway.core.connector.tcp.TcpConnector;
import com.htstar.ovms.device.gateway.core.invoke.ApiProxy;
import com.htstar.ovms.device.gateway.core.notify.NotifyProxy;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.Getter;

public class ServerTransportConfig {

    // handler
    @Getter
    private TcpConnector tcpConnector;
    // invoke
    @Getter
    private ApiProxy proxy;

    @Getter
    private NotifyProxy notify;

    public ServerTransportConfig(TcpConnector tcpConnector, ApiProxy proxy, NotifyProxy notify) {
        this.tcpConnector = tcpConnector;
        this.proxy = proxy;
        this.notify = notify;
    }

}
