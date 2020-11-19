package com.htstar.ovms.daemon.quartz.event;

import com.htstar.ovms.daemon.quartz.entity.SysJobLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author frwcloud
 * 定时任务日志多线程事件
 */
@Getter
@AllArgsConstructor
public class SysJobLogEvent {
	private final SysJobLog sysJobLog;
}
