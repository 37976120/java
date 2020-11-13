package top.hfer.springmvc.init;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import top.hfer.springmvc.config.ApplicationConfig;
import top.hfer.springmvc.config.WebConfig;

public class SpringContainerInitailizer extends AbstractAnnotationConfigDispatcherServletInitializer
{
    //spring容器
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ApplicationConfig.class};
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
