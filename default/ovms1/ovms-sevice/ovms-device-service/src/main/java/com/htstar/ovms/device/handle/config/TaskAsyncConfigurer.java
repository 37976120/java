package com.htstar.ovms.device.handle.config;

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
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
@Configuration
@EnableAsync
@Slf4j
public class TaskAsyncConfigurer implements AsyncConfigurer {

    @Value("${device.handle.corePoolSize:4}")
    private Integer corePoolSize;
    @Value("${device.handle.maxPoolSize:12}")
    private Integer maxPoolSize;
    @Value("${device.handle.queueCapacity:24}")
    private Integer queueCapacity;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setBeanName("cast-task");
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.initialize();
        return taskExecutor;

        // 自定义拒绝策略
//        /*executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
//            @Override
//            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//                //........
//            }
//        });*/
//
//        //一些参数的用法，这里是xml中配置的参数的用法，我们用的是代码的操作，调用对应方法的意义也是一样的。
//        //id：当配置多个executor时，被@Async(“id”)指定使用；也被作为线程名的前缀。
//        //pool-size：
//        //core size：最小的线程数，缺省：1
//        //max size：最大的线程数，缺省：Integer.MAX_VALUE
//        //queue-capacity：当最小的线程数已经被占用满后，新的任务会被放进queue里面，当这个queue的capacity也被占满之后，pool里面会创建新线程处理这个任务，直到总线程数达到了max size，这时系统会拒绝这个任务并抛出TaskRejectedException异常（缺省配置的情况下，可以通过rejection-policy来决定如何处理这种情况）。缺省值为：Integer.MAX_VALUE
//        //keep-alive：超过core size的那些线程，任务完成后，再经过这个时长（秒）会被结束掉
//        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
//        //ABORT（缺省）：抛出TaskRejectedException异常，然后不执行
//        //DISCARD：不执行，也不抛出异常
//        //DISCARD_OLDEST：丢弃queue中最旧的那个任务
//        //CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
     }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {

            //这个地方可以处理异步执行中的异常
            log.error("线程处理设备协议异常：",ex);
            for (Object param : params) {
                log.info("Parameter value - " + param);
            }
        };
    }
}
