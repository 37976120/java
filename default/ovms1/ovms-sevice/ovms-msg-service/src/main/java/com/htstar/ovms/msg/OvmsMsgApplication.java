package com.htstar.ovms.msg;

import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import com.htstar.ovms.common.swagger.annotation.EnableOvmsSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * Description: 消息服务
 * Author: flr
 * Date: Created in 2020/7/20
 * Company: 航通星空
 */
@EnableOvmsSwagger2
@SpringCloudApplication
@EnableOvmsFeignClients
@EnableOvmsResourceServer
public class OvmsMsgApplication {
    public static void main(String[] args) {
        SpringApplication.run(OvmsMsgApplication.class, args);
    }
}
