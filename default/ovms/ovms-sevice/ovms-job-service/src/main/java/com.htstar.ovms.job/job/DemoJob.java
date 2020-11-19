package com.htstar.ovms.job.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * Description: demo定时任务
 * Author: flr
 * Date: 2020/7/20 16:00
 * Company: 航通星空
 * Modified By: 
 */
@Slf4j
@Component
public class DemoJob {

	@XxlJob("DemoJobHandler")
	public ReturnT<String> demoJobHandler(String s) {
		ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
		XxlJobLogger.log("demo 定时任务：" + shardingVO);
		return SUCCESS;
	}
}