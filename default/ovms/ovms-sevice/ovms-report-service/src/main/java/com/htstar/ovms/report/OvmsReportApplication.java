package com.htstar.ovms.report;

import com.htstar.ovms.common.feign.annotation.EnableOvmsFeignClients;
import com.htstar.ovms.common.security.annotation.EnableOvmsResourceServer;
import com.htstar.ovms.common.swagger.annotation.EnableOvmsSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * Description: 报表服务
 * Author: flr
 * Date: Created in 2020/7/20
 * Company: 航通星空
 */
@EnableOvmsSwagger2
@SpringCloudApplication
@EnableOvmsFeignClients
@EnableOvmsResourceServer
public class OvmsReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(OvmsReportApplication.class, args);
    }
}
