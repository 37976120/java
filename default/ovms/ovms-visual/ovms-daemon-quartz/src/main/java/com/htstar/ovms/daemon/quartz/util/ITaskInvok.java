package com.htstar.ovms.daemon.quartz.util;

import com.htstar.ovms.daemon.quartz.entity.SysJob;
import com.htstar.ovms.daemon.quartz.exception.TaskException;

/**
 * 定时任务反射实现接口类
 *
 * @author 郑健楠
 */
public interface ITaskInvok {

	/**
	 * 执行反射方法
	 *
	 * @param sysJob 配置类
	 * @throws TaskException
	 */
	void invokMethod(SysJob sysJob) throws TaskException;
}
