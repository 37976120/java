package com.htstar.ovms.device.gateway.start;

import com.htstar.ovms.device.api.feign.DeviceLastDataFeign;
import com.htstar.ovms.device.gateway.core.connector.api.listener.LogSessionListener;
import com.htstar.ovms.device.gateway.core.connector.api.listener.SessionListener;
import com.htstar.ovms.device.gateway.core.connector.tcp.TcpConnector;
import com.htstar.ovms.device.gateway.core.connector.tcp.TcpSessionManager;
import com.htstar.ovms.device.gateway.core.connector.tcp.config.ServerTransportConfig;
import com.htstar.ovms.device.gateway.core.connector.tcp.server.TcpServer;
import com.htstar.ovms.device.gateway.core.notify.NotifyProxy;
import com.htstar.ovms.device.gateway.core.utils.NetUtils;
import com.htstar.ovms.device.gateway.obd.remoting.ObdApiProxy;
import com.htstar.ovms.device.gateway.obd.remoting.ObdSender;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: OBD启动
 * Author: flr
 * Date: Created in 2020/6/10
 * Company: 航通星空
 */
@Configuration
@Slf4j
public class ObdServerStart {

    /**
     * 设备名称
     */
    @Getter
    @Setter
    private String serverName = "OBD";

    /**
     * 设备连接IP
     */
    @Getter
    @Setter
    @Value("${device.OBD.expose.ip:127.0.0.1}")
    private String ip;


    /**
     * 设备连接端口
     */
    @Getter
    @Setter
    @Value("${device.OBD.expose.port:1001}")
    private int port;

    /**
     * 最大超时时间
     */
    @Getter
    @Setter
    @Value("${device.OBD.expose.maxInactiveInterval:300}")
    private int maxInactiveInterval;

    /**
     * 监听线程池大小
     */
    @Getter
    @Setter
    @Value("${device.OBD.expose.listenThreadPoolSize:0}")
    private int listenThreadPoolSize;

    /**
     * 业务线程池大小
     */
    @Getter
    @Setter
    @Value("${device.OBD.expose.workerThreadPoolSize:0}")
    private int workerThreadPoolSize;


    /**
     * 网关之后处理设备协议的代理类
     */
    @Autowired
    ObdApiProxy obdApiProxy;

    @Autowired
    DeviceLastDataFeign deviceLastDataFeign;


    /**
     * 普通指令下发
     */
    @Bean("obdSender")
    ObdSender obdSender(){
        return new ObdSender(tcpConnector());
    }


    @Bean
    ServerTransportConfig getServerTransportConfig(){

        ServerTransportConfig serverTransportConfig = new ServerTransportConfig(
                tcpConnector(),
                obdApiProxy,
                notifyProxy());
        return serverTransportConfig;
    }

    @Bean
    TcpSessionManager tcpSessionManager(){
        TcpSessionManager tcpSessionManager = new TcpSessionManager();
        //取消senssion日志监听
        List<SessionListener> sessionListenersList = new ArrayList<>();
        sessionListenersList.add(logSessionListener());
        tcpSessionManager.setSessionListeners(sessionListenersList);
        tcpSessionManager.setMaxInactiveInterval(this.maxInactiveInterval);
        return tcpSessionManager;
    }


    /**
     * 连接器
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "destroy")
    TcpConnector tcpConnector(){
        TcpConnector tcpConnector = new TcpConnector();
        tcpConnector.setTcpSessionManager(tcpSessionManager());
        tcpConnector.setDeviceLastDataFeign(deviceLastDataFeign);
        return tcpConnector;
    }

    /**
     * 会话日志监听（做安全处理）
     * @return
     */
    @Bean
    LogSessionListener logSessionListener(){
        return new LogSessionListener(deviceLastDataFeign);
    }

    /**
     *
     * @return
     */
    @Bean
    NotifyProxy notifyProxy(){
        NotifyProxy notifyProxy = new NotifyProxy(tcpConnector());
        return notifyProxy;
    }

    @Bean(initMethod = "start",destroyMethod = "stop")
    TcpServer obdTcpServer(){
        if (ip.equals("127.0.0.1")){
            this.ip = NetUtils.getLocalHost();
        }
        TcpServer tcpServer = new TcpServer(
                ip,
                port,
                serverName,
                maxInactiveInterval,
                listenThreadPoolSize,
                workerThreadPoolSize,
                getServerTransportConfig());
        return tcpServer;
    }

//    @Override
//    public void run(String... args) {
//        try{
//            if (ip.equals("127.0.0.1")){
//                this.ip = NetUtils.getLocalHost();
//            }
//            TcpServer tcpServer = new TcpServer(
//                    ip,
//                    port,
//                    serverName,
//                    maxInactiveInterval,
//                    listenThreadPoolSize,
//                    workerThreadPoolSize,
//                    getServerTransportConfig());
//            tcpServer.start();
//        }catch (Exception e){
//            log.error("{},启动失败：Exception：{}",e);
//        }
//    }
}
