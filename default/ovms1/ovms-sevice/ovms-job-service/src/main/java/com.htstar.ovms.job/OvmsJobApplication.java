package com.htstar.ovms.job;

import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.job.annotation.EnableOvmsXxlJob;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import com.htstar.ovms.common.swagger.annotation.EnableOvmsSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * Description: 分布式定时任务服务
 * Author: flr
 * Date: Created in 2020/7/20
 * Company: 航通星空
 */
@EnableOvmsXxlJob
@EnableOvmsFeignClients
@EnableOvmsSwagger2
@SpringCloudApplication
@EnableOvmsResourceServer
public class OvmsJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(OvmsJobApplication.class, args);
    }
}
