package com.htstar.ovms.device.gateway.core.connector.tcp.server;

import com.htstar.ovms.device.gateway.core.connector.tcp.config.ServerTransportConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Data
public class TcpServer {

    private ServerTransportConfig serverConfig;
    private String serverName;
    private String ip;
    private int port;

    // 读写空闲时间，单位为秒
    private int rwIdleSeconds = 6*60;
    // 监听线程池大小
    private int listenThreadPoolSize;
    // 业务线程池大小
    private int workerThreadPoolSize;

    private ChannelFuture future;
    private ExecutorService executor;

    public TcpServer(String ip,
                          int port,
                          String serverName,
                          int rwIdleSeconds,
                          int listenThreadPoolSize,
                          int workerThreadPoolSize,
                     ServerTransportConfig serverTransportConfig) {
        this.ip = ip;
        this.port = port;
        this.serverName = serverName;

        this.rwIdleSeconds = rwIdleSeconds;

        this.serverConfig = serverTransportConfig;

        if(listenThreadPoolSize == 0){
            this.listenThreadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        }

        if(workerThreadPoolSize == 0){
            this.workerThreadPoolSize = 4;
        }

        this.executor = Executors.newFixedThreadPool(10);
    }

    public synchronized boolean  start() {
        if (future != null) {
            log.info("设备监听服务{} {}:{} 已经启动过了!", this.serverName, this.ip, this.port);
            return false;
        }

        executor.submit(() -> {
            //listen线程监听端口，worker线程负责数据读写
            EventLoopGroup listenGroup = new NioEventLoopGroup(listenThreadPoolSize);
            EventLoopGroup workerGroup = new NioEventLoopGroup(workerThreadPoolSize);

            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(listenGroup, workerGroup);
                bootstrap.channel(NioServerSocketChannel.class);

                //设置管道工厂
                bootstrap.childHandler(new ObdServerChannelInitializer(serverConfig));
                bootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1024, 65536));

                //1.链接缓冲池的大小（ServerSocketChannel的设置）
                bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
                //维持链接的活跃，清除死链接(SocketChannel的设置)
                bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
                //关闭延迟发送
                bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

                future = bootstrap.bind(ip, port).sync();
                log.info("-------------------------------------------------------------------------------------------------------------------------");
                log.info("*************************************************************************************************************************");
                log.info("{}服务正在监听端口 {}:{}，监听线程池大小{}，业务线程池大小{} ...", serverName, ip, port,listenThreadPoolSize,workerThreadPoolSize);
                log.info("*************************************************************************************************************************");
                log.info("-------------------------------------------------------------------------------------------------------------------------");
                future.channel().closeFuture().sync();

                log.info("{}服务监听端口 {}:{} 已关闭！", serverName, ip, port);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                log.info("{}服务退出，释放线程池资源 {}:{}", serverName, ip, port);
                listenGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });

        return true;
    }

    public void stop() {
        if (future != null) {
            future.channel().close();
            future = null;
        }
    }

}
