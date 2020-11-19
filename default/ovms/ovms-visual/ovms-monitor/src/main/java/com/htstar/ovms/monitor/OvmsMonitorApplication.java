package com.htstar.ovms.monitor;


import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author ovms
 * @date 2019年11月4日
 * 监控中心
 */
@EnableAdminServer
@SpringCloudApplication
public class OvmsMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OvmsMonitorApplication.class, args);
	}
}
