package com.htstar.ovms.daemon.quartz.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.daemon.quartz.entity.SysJobLog;
import com.htstar.ovms.daemon.quartz.service.SysJobLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author frwcloud
 * <p>
 * 定时任务执行日志表
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-job-log")
@Api(value = "sys-job-log", tags = "定时任务日志")
public class SysJobLogController {
	private final SysJobLogService sysJobLogService;

	/**
	 * 分页查询
	 *
	 * @param page      分页对象
	 * @param sysJobLog 定时任务执行日志表
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation(value = "分页定时任务日志查询")
	public R getSysJobLogPage(Page page, SysJobLog sysJobLog) {
		return R.ok(sysJobLogService.page(page, Wrappers.query(sysJobLog)));
	}
}
