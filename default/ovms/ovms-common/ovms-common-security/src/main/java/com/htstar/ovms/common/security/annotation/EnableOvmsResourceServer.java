package com.htstar.ovms.common.security.annotation;

import com.htstar.ovms.common.security.component.OvmsResourceServerAutoConfiguration;
import com.htstar.ovms.common.security.component.OvmsSecurityBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * 资源服务注解
 */
@Documented//标记这些注解是否包含在用户文档中。
@Inherited//标记这个注解是继承于哪个注解类(默认 注解并没有继承于任何子类)
@EnableResourceServer
@Target({ElementType.TYPE})//标记这个注解应该是哪种 Java 成员。 TYPE作用在class上
@Retention(RetentionPolicy.RUNTIME)//标识这个注解怎么保存，是只在代码中，还是编入class文件中，或者是在运行时可以通过反射访问。
@EnableGlobalMethodSecurity(prePostEnabled = true)
//使用表达式时间方法级别的安全性         4个注解可用
//@PreAuthorize 在方法调用之前,基于表达式的计算结果来限制对方法的访问
//@PostAuthorize 允许方法调用,但是如果表达式计算结果为false,将抛出一个安全性异常
//@PostFilter 允许方法调用,但必须按照表达式来过滤方法的结果
//@PreFilter 允许方法调用,但必须在进入方法之前过滤输入值
//https://www.jianshu.com/p/41b7c3fb00e0
@Import({OvmsResourceServerAutoConfiguration.class, OvmsSecurityBeanDefinitionRegistrar.class})
public @interface EnableOvmsResourceServer {

}
