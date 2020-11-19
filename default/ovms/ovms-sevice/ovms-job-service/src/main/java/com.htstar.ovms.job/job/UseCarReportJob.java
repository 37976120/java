package com.htstar.ovms.job.job;

import cn.hutool.json.JSONUtil;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.job.api.req.UseCarReportReq;
import com.htstar.ovms.job.service.ReportUseCarService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.xxl.job.core.biz.model.ReturnT.FAIL;
import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * Description: 用车报表JOB
 * Author: flr
 * Date: Created in 2020/7/30
 * Company: 航通星空
 * Modified By:
 */
@Slf4j
@Component
public class UseCarReportJob {

    @Autowired
    private ReportUseCarService useCarService;

    /**
     * Description: 统计
     * Author: flr
     * Date: 2020/7/30 11:53
     * Company: 航通星空
     * Modified By: 
     */
    @XxlJob("UseCarReportHandler")
    public ReturnT<String> useCarReportHandler(String json) {
        ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
        log.info("用车报表JOB：ShardingVO{}", shardingVO);
        UseCarReportReq req = JSONUtil.toBean(json,UseCarReportReq.class);
        R r = useCarService.useCarReport(req);
        if (r.getCode() == CommonConstants.SUCCESS){
            return SUCCESS;
        }else {
            return FAIL;
        }
    }
}
