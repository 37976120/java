package com.htstar.ovms.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author LCN
 * @author ovms
 * tx-manager ，进行了代码逻辑和代码规范重构
 */
@SpringCloudApplication
public class OvmsTxManagerApplication {


	public static void main(String[] args) {
		SpringApplication.run(OvmsTxManagerApplication.class, args);
	}

}
