package top.hfer.springmvc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

//spring容器的扫描排除掉controller的扫描！
@Configuration
@ComponentScan(basePackages = "top.hfer.springmvc", excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)})
public class ApplicationConfig {
    //配置除了Controller的其它东西，数据库连接池，事务管理器，bean等，

}
