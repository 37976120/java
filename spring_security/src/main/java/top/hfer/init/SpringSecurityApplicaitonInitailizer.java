package top.hfer.init;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import top.hfer.config.WebSecurityConfig;

public class SpringSecurityApplicaitonInitailizer extends AbstractSecurityWebApplicationInitializer {
    public SpringSecurityApplicaitonInitailizer() {
//        super(WebSecurityConfig.class);
    }
    /**若当前环境没有使用Spring或SpringMVC,则需要将WebSecurityConfig(SpringSecurity配置类)传入超类
     * 以确保配置生效，
     * 相反，我们已经在SpringContext中注册了SpringSecurityConfig(在那个数组中)，所以这个类什么都不干
     */
}
