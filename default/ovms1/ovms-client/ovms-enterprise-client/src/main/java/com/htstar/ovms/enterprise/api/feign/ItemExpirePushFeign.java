package com.htstar.ovms.enterprise.api.feign;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/20
 * Company: 航通星空
 * Modified By:
 */
@FeignClient(contextId = "itemExpirePushFeign", value = ServiceNameConstants.ENTERPRISE_SERVICE)
public interface ItemExpirePushFeign {

    /**
     * 年检到期推送
     * @return
     */
    @GetMapping("/itemPush/motItem" )
    R getMotItemExpirePush(@RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 保险到期推送
     * @return
     */
    @GetMapping("/itemPush/insItem" )
    R getInsItemExpirePush(@RequestHeader(SecurityConstants.FROM) String from);
}
