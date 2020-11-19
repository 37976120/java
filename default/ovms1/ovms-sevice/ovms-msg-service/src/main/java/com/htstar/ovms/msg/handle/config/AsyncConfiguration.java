package com.htstar.ovms.msg.handle.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author JinZhu
 * @Description:
 * @date 2020/8/2711:03
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {
    @Value("${msg.handle.corePoolSize:4}")
    private Integer corePoolSize;
    @Value("${msg.handle.maxPoolSize:12}")
    private Integer maxPoolSize;
    @Value("${msg.handle.queueCapacity:24}")
    private Integer queueCapacity;

    @Bean("kingAsyncExecutor")
    public ThreadPoolTaskExecutor executor() { //线程配置
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        String threadNamePrefix = "kingDeeAsyncExecutor-";
//        executor.setThreadNamePrefix(threadNamePrefix);
//        executor.setWaitForTasksToCompleteOnShutdown(true);
//        int awaitTerminationSeconds = 5;
//        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        executor.initialize();
        return executor;
    }



    @Override
    public Executor getAsyncExecutor() {  //线程执行器
        return executor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() { //异步异常处理
        return (ex, method, params) -> log.error(String.format("执行异步任务'%s'", method), ex);
    }
}
