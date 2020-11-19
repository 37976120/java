package com.htstar.ovms.device.gateway;

import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 设备服务网关
 */
@SpringCloudApplication
@EnableOvmsFeignClients
@EnableOvmsResourceServer
public class OvmsDeviceGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OvmsDeviceGatewayApplication.class, args);
    }
}
