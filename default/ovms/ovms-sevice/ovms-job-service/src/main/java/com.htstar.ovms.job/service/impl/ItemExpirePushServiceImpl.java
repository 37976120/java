package com.htstar.ovms.job.service.impl;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.feign.CarInfoFeign;
import com.htstar.ovms.enterprise.api.feign.ItemExpirePushFeign;
import com.htstar.ovms.job.service.ItemExpirePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/20
 * Company: 航通星空
 * Modified By:
 */
@Service
public class ItemExpirePushServiceImpl implements ItemExpirePushService {
    @Autowired
    private ItemExpirePushFeign itemExpirePushFeign;
    @Autowired
    private CarInfoFeign carInfoFeign;
    @Override
    public R getMotItemExpirePush() {
        return itemExpirePushFeign.getMotItemExpirePush(SecurityConstants.FROM_IN);
    }
}
