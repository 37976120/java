package com.htstar.ovms.device;


import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import com.htstar.ovms.common.swagger.annotation.EnableOvmsSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author flr
 * <p>
 * 项目启动类
 */
@EnableOvmsSwagger2
@SpringCloudApplication
@EnableOvmsFeignClients
@EnableOvmsResourceServer
public class OvmsDeviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OvmsDeviceApplication.class, args);
    }
}
