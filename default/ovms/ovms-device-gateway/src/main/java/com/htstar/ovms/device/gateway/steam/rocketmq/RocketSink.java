package com.htstar.ovms.device.gateway.steam.rocketmq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Description: 消息接收器
 * Author: flr
 * Company: 航通星空
 * Modified By:
 */
public interface RocketSink {
//        /**
//         * 这里的名称对应了spring.cloud.stream.rocketmq.bindings.<channelName>
//         * 集群单点消费消息
//         */
//        String OUTPUT_SP = "device-gateway-output-sp";
//
//        /**
//         * 广播消息
//         */
//        String OUTPUT_SB = "device-gateway-output-sb";


    /**
     * 监听设备服务广播的指令(对象消息)
     */
    String DEVICE_COMMAND_INPUT = "device-command-input";


    @Input(DEVICE_COMMAND_INPUT)
    SubscribableChannel deviceCommandInput();

}
