package com.htstar.ovms.codegen;

import com.htstar.ovms.common.datasource.annotation.EnableDynamicDataSource;
import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import com.htstar.ovms.common.swagger.annotation.EnableOvmsSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author ovms
 * @date 2018/07/29
 * 代码生成模块
 */
@EnableDynamicDataSource
@EnableOvmsSwagger2
@EnableOvmsFeignClients
@SpringCloudApplication
@EnableOvmsResourceServer
public class OvmsCodeGenApplication {


	public static void main(String[] args) {
		SpringApplication.run(OvmsCodeGenApplication.class, args);
	}
}
