package com.htstar.ovms.device.gateway.core.connector.tcp;

import com.htstar.ovms.device.gateway.core.connector.Session;
import com.htstar.ovms.device.gateway.core.connector.api.ExchangeSessionManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author flr
 */
public abstract class ExchangeTcpSessionManager extends ExchangeSessionManager {

    public abstract Session createSession(String deviceSn, ChannelHandlerContext ctx);

}
