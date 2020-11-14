package top.hfer.init;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import top.hfer.config.ApplicationConfig;
import top.hfer.config.WebConfig;
import top.hfer.config.WebSecurityConfig;

//初始化spring容，通过实现WebApplicationInitailizer
public class SpringContainerInitailizer extends AbstractAnnotationConfigDispatcherServletInitializer {
    //spring容器
    @Override
    protected Class<?>[] getRootConfigClasses() {
        //需要放入容器的类
        return new Class[]{ApplicationConfig.class, WebSecurityConfig.class};
    }

    //servletContext
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    //url-Mapping
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
