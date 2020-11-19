package com.htstar.ovms.device.steam.rocketmq;

import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * 配置消息生产者
 */
@EnableBinding({RocketSource.class})
public class SteamBinding {

}
