package com.htstar.ovms.device.gateway.core.connector.tcp.server;

import com.htstar.ovms.device.gateway.core.connector.tcp.config.ServerTransportConfig;
import com.htstar.ovms.device.gateway.obd.coder.ObdLengthDecoder;
import com.htstar.ovms.device.gateway.obd.coder.ObdObjectDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/15
 * Company: 航通星空
 * Modified By:
 */
public class ObdServerChannelInitializer extends ChannelInitializer<SocketChannel>{
    private ServerTransportConfig serverConfig;

    public ObdServerChannelInitializer(ServerTransportConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("frameDecoder", new ObdLengthDecoder());//解码器，接收消息时候用
        pipeline.addLast("decoder",new ObdObjectDecoder());//解码器，接收消息时候用
        pipeline.addLast("handler", new TcpServerHandler(serverConfig));
    }
}
