package com.htstar.ovms.pay;

import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import com.htstar.ovms.common.swagger.annotation.EnableOvmsSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author ovms
 * @date 2019年05月27日17:25:38
 * <p>
 * 支付模块
 */
@EnableOvmsSwagger2
@SpringCloudApplication
@EnableOvmsFeignClients
@EnableOvmsResourceServer
public class OvmsPayPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(OvmsPayPlatformApplication.class, args);
	}
}
