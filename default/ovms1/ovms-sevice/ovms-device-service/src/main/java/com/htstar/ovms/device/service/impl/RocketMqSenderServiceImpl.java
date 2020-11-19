package com.htstar.ovms.device.service.impl;

import com.htstar.ovms.device.steam.rocketmq.RocketSource;
import com.htstar.ovms.device.service.RocketMqSenderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

/**
 * Description: rocketMQ 发布消息
 * Author: flr
 * Date: Created in 2020/6/17
 * Company: 航通星空
 * Modified By:
 */
@Service
@Slf4j
public class RocketMqSenderServiceImpl implements RocketMqSenderService {

    @Autowired
    private RocketSource source;

    /**
     * 发送带tag的对象消息
     */
    @Override
    public <T> void sendWithTags(T msg, String tag) {
        Message message = MessageBuilder.withPayload(msg)
                .setHeader(MessageConst.PROPERTY_TAGS, tag)
//                .setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, 1000 * 60 * 1)//延时消息
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();

        log.info("MQ广播指令信息：{}",message);
        source.deviceCommandOutput().send(message);
    }
}
