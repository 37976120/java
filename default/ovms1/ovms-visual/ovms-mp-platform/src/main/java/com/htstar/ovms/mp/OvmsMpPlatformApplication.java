package com.htstar.ovms.mp;

import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import com.htstar.ovms.common.swagger.annotation.EnableOvmsSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author ovms
 * @date 2019/03/25
 * 微信公众号管理模块
 */
@EnableOvmsSwagger2
@EnableOvmsFeignClients
@SpringCloudApplication
@EnableOvmsResourceServer
public class OvmsMpPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(OvmsMpPlatformApplication.class, args);
	}
}
