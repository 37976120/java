package com.htstar.ovms.common.job.annotation;

import com.htstar.ovms.common.job.XxlJobAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ovms
 * @date 2019-09-18
 * <p>
 * 开启支持XXL
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({XxlJobAutoConfiguration.class})
public @interface EnableOvmsXxlJob {
}
