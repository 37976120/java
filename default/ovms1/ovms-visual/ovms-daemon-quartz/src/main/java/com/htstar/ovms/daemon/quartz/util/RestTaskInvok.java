package com.htstar.ovms.daemon.quartz.util;

import cn.hutool.http.HttpUtil;
import com.htstar.ovms.daemon.quartz.entity.SysJob;
import com.htstar.ovms.daemon.quartz.exception.TaskException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 定时任务rest反射实现
 *
 * @author 郑健楠
 */
@Slf4j
@AllArgsConstructor
@Component("restTaskInvok")
public class RestTaskInvok implements ITaskInvok {

	@Override
	public void invokMethod(SysJob sysJob) throws TaskException {
		try {
			HttpUtil.createGet(sysJob.getExecutePath())
					.execute();
		} catch (Exception e) {
			log.error("定时任务restTaskInvok异常,执行任务：{}", sysJob.getExecutePath());
			throw new TaskException("定时任务restTaskInvok业务执行失败,任务：" + sysJob.getExecutePath());
		}
	}
}
