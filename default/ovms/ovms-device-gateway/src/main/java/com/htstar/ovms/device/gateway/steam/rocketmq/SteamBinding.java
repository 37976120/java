package com.htstar.ovms.device.gateway.steam.rocketmq;

import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * steam配置
 */
@EnableBinding({RocketSink.class})//数组
public class SteamBinding {

}
