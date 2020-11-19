package com.htstar.ovms.msg.api.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description: 指定用户的推送（单点）
 * Author: flr
 * Date: Created in 2020/8/12
 * Company: 航通星空
 * Modified By:
 */
@Data
public class SingleAppPushReq extends BaseAppPush implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "单点推送必须指定用户ID")
    private Integer userId;
}
