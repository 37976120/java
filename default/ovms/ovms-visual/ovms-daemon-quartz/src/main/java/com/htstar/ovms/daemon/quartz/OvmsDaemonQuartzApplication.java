package com.htstar.ovms.daemon.quartz;

import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import com.htstar.ovms.common.swagger.annotation.EnableOvmsSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author frwcloud
 * @date 2019/01/23
 * 定时任务模块
 */
@EnableOvmsSwagger2
@EnableOvmsFeignClients
@SpringCloudApplication
@EnableOvmsResourceServer
public class OvmsDaemonQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(OvmsDaemonQuartzApplication.class, args);
	}
}
