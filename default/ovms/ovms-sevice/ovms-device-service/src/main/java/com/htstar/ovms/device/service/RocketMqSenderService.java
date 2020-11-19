package com.htstar.ovms.device.service;

import org.springframework.messaging.Message;

/**
 * Description: RocketMq
 * Author: flr
 * Date: Created in 2020/6/17
 * Company: 航通星空
 * Modified By:
 */
public interface RocketMqSenderService {
    <T> void sendWithTags(T message, String tag);
}
