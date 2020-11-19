package com.htstar.ovms.admin.api.feign;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/28
 * Company: 航通星空
 * Modified By:
 */
@FeignClient(contextId = "smsFeign", value = ServiceNameConstants.PLATFORM_SERVICE)
public interface SmsFeign {
    /**
     * Description: 发送验证码
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    @GetMapping("/sms/sendCode/{mobile}")
    R sendSmsCode(@PathVariable("mobile") String mobile, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 校验短信验证码
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/sms/checkSmsCode/{phone}/{code}")
    boolean checkSmsCode(@PathVariable("phone")String phone, @PathVariable("code")String code, @RequestHeader(SecurityConstants.FROM) String from);
}
