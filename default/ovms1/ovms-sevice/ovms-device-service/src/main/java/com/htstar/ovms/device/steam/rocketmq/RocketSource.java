package com.htstar.ovms.device.steam.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

/**
 * Description: 消息发布者
 * Author: flr
 * Company: 航通星空
 */
public interface RocketSource {
//        /**
//         * 这里的名称对应了spring.cloud.stream.rocketmq.bindings.<channelName>
//         * 集群单点消费消息
//         */

    /**
     * 广播指令(对象消息)
     */
    String DEVICE_COMMAND_OUTPUT = "device-command-output";


    @Output(DEVICE_COMMAND_OUTPUT)
    SubscribableChannel deviceCommandOutput();

}
