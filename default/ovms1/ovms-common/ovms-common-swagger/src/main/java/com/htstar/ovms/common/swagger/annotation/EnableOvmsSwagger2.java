package com.htstar.ovms.common.swagger.annotation;

import com.htstar.ovms.common.swagger.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ovms
 * @date 2018/7/21
 * 开启ovms swagger
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnableOvmsSwagger2 {
}
