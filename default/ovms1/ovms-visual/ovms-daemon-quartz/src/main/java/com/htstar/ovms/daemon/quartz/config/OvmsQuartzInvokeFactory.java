package com.htstar.ovms.daemon.quartz.config;

import com.htstar.ovms.daemon.quartz.entity.SysJob;
import com.htstar.ovms.daemon.quartz.event.SysJobEvent;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.quartz.Trigger;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author 郑健楠
 */
@Aspect
@Slf4j
@AllArgsConstructor
public class OvmsQuartzInvokeFactory {

	private final ApplicationEventPublisher publisher;

	@SneakyThrows
	void init(SysJob sysJob, Trigger trigger) {
		publisher.publishEvent(new SysJobEvent(sysJob, trigger));
	}
}
