package com.htstar.ovms.job.job;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.enterprise.api.feign.ItemExpirePushFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/20
 * Company: 航通星空
 * Modified By:
 */
@Slf4j
@Component
public class ItemExpirePushJob {
   @Autowired
   private ItemExpirePushFeign itemExpirePushFeign;

    /**
     * 保养到期定时任务
     * @param s
     * @return
     */
    @XxlJob("MotPushJobHandler")
    public ReturnT<String> motPushJobHandler(String s) {
        try {
            ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
            log.info("车辆年检：ShardingVO{}", shardingVO);
            itemExpirePushFeign.getMotItemExpirePush(SecurityConstants.FROM_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }


    /**
     * 保险到期定时任务
     * @param s
     * @return
     */
    @XxlJob("InsPushJobHandler")
    public ReturnT<String> insPushJobHandler(String s) {
        try {
            ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
            log.info("保险：ShardingVO{}", shardingVO);
            itemExpirePushFeign.getInsItemExpirePush(SecurityConstants.FROM_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
}
