package com.htstar.ovms.auth;


import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author ovms
 * @date 2020-02-08
 * 认证授权中心
 */
@SpringCloudApplication
@EnableOvmsFeignClients
public class OvmsAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OvmsAuthApplication.class, args);
	}
}
