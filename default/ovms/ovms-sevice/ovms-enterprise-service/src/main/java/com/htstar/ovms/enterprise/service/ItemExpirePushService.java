package com.htstar.ovms.enterprise.service;

import com.htstar.ovms.common.core.util.R;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/20
 * Company: 航通星空
 * Modified By:
 */
public interface ItemExpirePushService {
    /**
     * 年检过期提醒
     * @return
     */
    R getMotItemExpirePush();

    /**
     * 保险过期推送
     * @return
     */
    R getInsItemExpirePush();


}
