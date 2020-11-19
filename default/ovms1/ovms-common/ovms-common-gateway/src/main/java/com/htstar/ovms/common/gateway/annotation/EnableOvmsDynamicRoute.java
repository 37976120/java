package com.htstar.ovms.common.gateway.annotation;

import com.htstar.ovms.common.gateway.configuration.DynamicRouteAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ovms
 * @date 2018/11/5
 * <p>
 * 开启ovms 动态路由
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(DynamicRouteAutoConfiguration.class)
public @interface EnableOvmsDynamicRoute {
}
