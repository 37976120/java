package com.htstar.ovms.gateway;


import com.htstar.ovms.common.gateway.annotation.EnableOvmsDynamicRoute;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author ovms
 * @date 2018年06月21日
 * 网关应用
 */
@EnableOvmsDynamicRoute
@SpringCloudApplication
public class OvmsApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(OvmsApiGatewayApplication.class, args);
	}
}
