package com.htstar.ovms.device.gateway.core.connector.tcp.server;

import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import com.htstar.ovms.device.gateway.core.connector.tcp.TcpConnector;
import com.htstar.ovms.device.gateway.core.connector.tcp.config.ServerTransportConfig;
import com.htstar.ovms.device.gateway.core.constant.Constants;
import com.htstar.ovms.device.gateway.core.invoke.ApiProxy;
import com.htstar.ovms.device.gateway.core.message.SystemMessage;
import com.htstar.ovms.device.gateway.core.notify.NotifyProxy;
import com.htstar.ovms.device.gateway.core.utils.NetUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@ChannelHandler.Sharable
@Slf4j
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    private TcpConnector tcpConnector;
    private ApiProxy proxy;

    //指令序号序列化下发
    private NotifyProxy notify;

    public TcpServerHandler(ServerTransportConfig config) {
        this.tcpConnector = config.getTcpConnector();
        this.proxy = config.getProxy();
        this.notify = config.getNotify();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) {
        try {
            if (o instanceof ProtoTransferDTO) {
                ProtoTransferDTO protoTransferDTO = (ProtoTransferDTO) o;
                SystemMessage sMsg = generateSystemMessage(ctx);
                // 发送数据到后端服务处理
                MessageWrapper wrapper = proxy.invoke(sMsg,protoTransferDTO);
                if (wrapper != null){
                    //  下发指令回包 （后端服务处理分辨出该指令是回包的，因为我们把指令包已经加了序号到数据库，
                    //  这一步可以通过后端服务区处理指令的处理结果）
                    if (wrapper.isReply()){
                        notify.reply(wrapper);
                    }else {
                        // inbound  普通交互回传
                        this.receive(ctx, wrapper);
                    }
                }

            } else {
                log.warn("channelRead信道处理设备消息接收到非期待的poro数据");
            }
        } catch (Exception e) {
            log.error("channelRead信道处理设备消息异常.", e);
//            throw e;
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        log.debug("收到连接来自 {" +
                NetUtils.channelToString(ctx.channel().remoteAddress(), ctx.channel().localAddress()) + "}");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        log.debug("收到退出连接来自 {" +
                NetUtils.channelToString(ctx.channel().remoteAddress(), ctx.channel().localAddress()) + "}");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("TcpServerHandler channelActive from (" + getRemoteAddress(ctx) + ")");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("TcpServerHandler channelInactive from (" + getRemoteAddress(ctx) + ")");
        String deviceSn0 = getChannelSessionHook(ctx);
        if (StringUtils.isNotBlank(deviceSn0)) {
            tcpConnector.close(new MessageWrapper(MessageWrapper.MessageType.CLOSE, deviceSn0, null));
            log.warn("TcpServerHandler channelInactive, close channel deviceSn0 -> " + deviceSn0 + ", ctx -> " + ctx.toString());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx,cause);
        log.debug("TcpServerHandler (" + getRemoteAddress(ctx) + ") -> Unexpected exception from downstream." + cause);
        String deviceSn0 = getChannelSessionHook(ctx);
        if (StringUtils.isNotBlank(deviceSn0)) {
            tcpConnector.close(new MessageWrapper(MessageWrapper.MessageType.CLOSE, deviceSn0, null));
            log.debug("TcpServerHandler channelInactive, close channel deviceSn0 -> " + deviceSn0 + ", ctx -> " + ctx.toString());
        }
    }

    private String getChannelSessionHook(ChannelHandlerContext ctx) {
        return ctx.channel().attr(Constants.SERVER_SESSION_HOOK).get();
    }

    private void setChannelSessionHook(ChannelHandlerContext ctx, String deviceSn) {
        ctx.channel().attr(Constants.SERVER_SESSION_HOOK).set(deviceSn);
    }

    /**
     * @param ctx
     * @param wrapper
     */
    private void receive(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        if (wrapper.isConnect()) {
            if (wrapper.getSeq() != null && wrapper.getSeq().longValue() == -1){
                tcpConnector.close(wrapper);
                //session 可能并未建立所以需要最后手动检测一下停掉ctx
                ctx.close();
                log.info("设备[{}]连接被平台拒绝连接",wrapper.getDeviceSn());
                return;
            }
            //首次连接，创建Session
            isConnect0(ctx, wrapper);
        } else if (wrapper.isClose()) {
            //主动退出，销毁Session
            tcpConnector.close(wrapper);
        } else if (wrapper.isHeartbeat()) {
            //心跳，更新Session
            tcpConnector.heartbeatClient(wrapper);
        } else if (wrapper.isSend()) {
            //后端处理有回包，下发到设备
            tcpConnector.responseSendMessage(wrapper);
        }
    }

    private void isConnect0(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        String deviceSn = wrapper.getDeviceSn();
        String deviceSn0 = getChannelSessionHook(ctx);
        if (deviceSn.equals(deviceSn0)) {
            log.info("设备[{}]TCP连接器重新连接,ctx ->{}",deviceSn,ctx);
            tcpConnector.responseSendMessage(wrapper);
        } else {
            log.info("设备[{}]TCP连接器新建连接,ctx ->{}",deviceSn,ctx);
            tcpConnector.connect(ctx, wrapper);
            setChannelSessionHook(ctx, deviceSn);
            log.info("设备[{}]创建通道attr成功, ctx ->{}",deviceSn,ctx.toString());
        }
    }

    private SystemMessage generateSystemMessage(ChannelHandlerContext ctx) {
        SystemMessage systemMessage = new SystemMessage();
        systemMessage.setRemoteAddress(getRemoteAddress(ctx));
        systemMessage.setLocalAddress(getLocalAddress(ctx));

        return systemMessage;
    }

    private String getRemoteAddress(ChannelHandlerContext ctx) {
        SocketAddress remote1 = ctx.channel().remoteAddress();
        InetSocketAddress remote = (InetSocketAddress) remote1;
        return NetUtils.toAddressString(remote);
    }

    private String getLocalAddress(ChannelHandlerContext ctx) {
        SocketAddress local1 = ctx.channel().localAddress();
        InetSocketAddress local = (InetSocketAddress) local1;
        return NetUtils.toAddressString(local);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }
}
