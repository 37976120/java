package com.htstar.ovms.enterprise.handle.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Description: 异步线程池配置
 * Author: flr
 * Date: 2020/8/13 9:27
 * Company: 航通星空
 * Modified By:
 */
@Configuration
@EnableAsync
@Slf4j
public class TaskAsyncConfigurer implements AsyncConfigurer {

    @Value("${async.handle.corePoolSize:4}")
    private Integer corePoolSize;
    @Value("${async.handle.maxPoolSize:12}")
    private Integer maxPoolSize;
    @Value("${async.handle.queueCapacity:24}")
    private Integer queueCapacity;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setBeanName("async-task");
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.initialize();
        return taskExecutor;
     }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            //这个地方可以处理异步执行中的异常
            log.error("线程异步处理异常：",ex);
            for (Object param : params) {
                log.info("Parameter value - " + param);
            }
        };
    }
}
