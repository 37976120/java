package com.htstar.ovms.admin;


import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import com.htstar.ovms.common.swagger.annotation.EnableOvmsSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author ovms
 * @date 2018年06月21日
 * <p>
 * 用户统一管理系统
 */
@EnableOvmsSwagger2
@SpringCloudApplication
@EnableOvmsFeignClients
@EnableOvmsResourceServer
public class OvmsPlatformApplication {
	public static void main(String[] args) {
		SpringApplication.run(OvmsPlatformApplication.class, args);
	}

}
