package com.htstar.ovms.msg.api.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Description: 批量推送
 * Author: flr
 * Date: Created in 2020/8/12
 * Company: 航通星空
 * Modified By:
 */
@Data
public class BatchPushByUserIdsReq extends BaseAppPush{

    @NotNull
    private List<Integer> userIds;
}
