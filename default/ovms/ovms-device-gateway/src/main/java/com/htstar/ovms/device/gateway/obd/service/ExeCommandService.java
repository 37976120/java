package com.htstar.ovms.device.gateway.obd.service;

import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.gateway.obd.remoting.ObdSender;
import com.htstar.ovms.device.gateway.steam.rocketmq.RocketSink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Description: 执行指令
 * Author: flr
 * Company: 航通星空
 * Modified By:
 */
@Service
@Slf4j
public class ExeCommandService {

    /**
     * 普通下发（不关注结果）
     */
    @Autowired
    private ObdSender obdSender;

//    /**
//     * 指令序列化下发
//     */
//    @Autowired
//    private NotifyProxy notifyProxy;
//
//    @Autowired
//    private DeviceCommandFeign deviceCommandFeign;


    /**
     * 对象消息
     */
    @StreamListener(RocketSink.DEVICE_COMMAND_INPUT)
    public void commandInput(@Payload MessageWrapper message) {
        log.info("设备服网关监听[{}]MQ下发设备指令:-->指令序列号:{}",message.getDeviceSn(),message.getSeq());
        if (message == null){
            log.error("设备服网关监听MQ下发设备指令异常，消息内容为null");
            return;
        }
        try {
            if (obdSender.existSession(message)){
                obdSender.sendMessage(message);
                /*// 1.检查该指令是否需要下发 ::网关执行状态：0=未发送到网关；1=网关已收到；2=网关下发成功；3=网关执行失败
                Integer gatewayStatus = deviceCommandFeign.getGatewayStatus(message.getSeq(), SecurityConstants.FROM_IN);

                if (gatewayStatus == null || gatewayStatus.intValue() == 1 || gatewayStatus.intValue() == 2){
                    log.info("网关重复消息--gatewayStatus{}--->seq{}",gatewayStatus,message.getSeq());
                    return;
                }
                int result = notifyProxy.notify(message,30000);//毫秒
                switch (result){
                    case Constants
                            .NOTIFY_SUCCESS:
                        DeviceCommand success = new DeviceCommand();
                        success.setId(message.getSeq());
                        success.setDeviceSn(message.getDeviceSn());
                        success.setGatewayStatus(2);
                        success.setExecuteTime(OvmDateUtil.getCstNow());
                        success.setRemark(NetUtils.getLocalHost());
                        //修改平台执行指令的状态：平台已下发
                        deviceCommandFeign.handleGatewayResult(success, SecurityConstants.FROM_IN);
                        break;
                    case Constants
                            .NOTIFY_FAILURE:
                        DeviceCommand fail = new DeviceCommand();
                        fail.setId(message.getSeq());
                        fail.setGatewayStatus(3);
                        fail.setDeviceSn(message.getDeviceSn());
                        fail.setRemark(NetUtils.getLocalHost());
                        deviceCommandFeign.handleGatewayResult(fail, SecurityConstants.FROM_IN);
                        break;
                    case Constants
                            .NOTIFY_NO_SESSION:
                        log.debug("[{}]不在本服务器连接!",message.getDeviceSn());
                        break;
                    default:
                        log.debug("[{}]不在本服务器连接!",message.getDeviceSn());
                        break;
                }*/
            }else{
                /** no session on this machine **/
                log.debug("[{}]不在本服务器连接!",message.getDeviceSn());
                return;
            }
        } catch (Exception e) {
            log.error("下发命令遭遇异常：",e);
        }
    }

//    /**
//     * 字符串消息
//     */
//    @StreamListener(Sink.INPUT)
//    public void receiveInput(String receiveMsg) throws Exception {
//        log.info("input receive: " + receiveMsg);
//    }
}
