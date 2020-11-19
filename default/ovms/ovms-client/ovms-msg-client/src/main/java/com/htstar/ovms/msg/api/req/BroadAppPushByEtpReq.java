package com.htstar.ovms.msg.api.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description: 广播给企业消息
 * Author: flr
 * Date: Created in 2020/8/12
 * Company: 航通星空
 * Modified By:
 */
@Data
public class BroadAppPushByEtpReq extends BaseAppPush{
    @NotNull(message = "广播给企业消息需要etpId")
    private Integer etpId;
}
