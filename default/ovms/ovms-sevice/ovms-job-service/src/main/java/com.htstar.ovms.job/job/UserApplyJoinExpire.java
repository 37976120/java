package com.htstar.ovms.job.job;

import com.htstar.ovms.job.service.UserApplyJoinExpireService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * Description: 用户申请加入（3天未处理过期）
 * Author: flr
 * Date: Created in 2020/9/4
 * Company: 航通星空
 * Modified By:
 */
@Slf4j
@Component
public class UserApplyJoinExpire {

    @Autowired
    private UserApplyJoinExpireService userApplyJoinExpireService;

    /**
     * 用户申请加入（3天未处理过期）
     * @param s
     * @return
     */
    @XxlJob("ExpireUserApplyJoin")
    public ReturnT<String> expireUserApplyJoin(String s) {
        try {
            ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
            log.info("用户申请加入（3天未处理过期）：ShardingVO{}", shardingVO);
            userApplyJoinExpireService.expireUserApplyJoin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
}
