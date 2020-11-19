package com.htstar.ovms.daemon.quartz.config;

import com.htstar.ovms.daemon.quartz.constants.OvmsQuartzEnum;
import com.htstar.ovms.daemon.quartz.entity.SysJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 郑健楠
 *
 * <p>
 * 动态任务工厂
 */
@Slf4j
@DisallowConcurrentExecution
public class OvmsQuartzFactory implements Job {

	@Autowired
	private OvmsQuartzInvokeFactory ovmsQuartzInvokeFactory;


	@Override
	@SneakyThrows
	public void execute(JobExecutionContext jobExecutionContext) {
		SysJob sysJob = (SysJob) jobExecutionContext.getMergedJobDataMap().get(OvmsQuartzEnum.SCHEDULE_JOB_KEY.getType());
		ovmsQuartzInvokeFactory.init(sysJob, jobExecutionContext.getTrigger());
	}
}
