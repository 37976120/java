package com.htstar.ovms.act;

import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import com.htstar.ovms.common.swagger.annotation.EnableOvmsSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author ovms
 * @date 2018/9/25
 * 工作流管理模块
 */
@EnableOvmsSwagger2
@EnableOvmsFeignClients
@EnableOvmsResourceServer
@SpringCloudApplication
public class OvmsActivitiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OvmsActivitiApplication.class, args);
	}

}
