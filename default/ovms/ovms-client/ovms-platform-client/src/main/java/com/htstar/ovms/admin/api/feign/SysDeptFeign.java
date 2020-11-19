package com.htstar.ovms.admin.api.feign;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Description: 系统部门
 * Author: flr
 * Date: Created in 2020/6/16
 * Company: 航通星空
 * Modified By:
 */
@FeignClient(contextId = "sysDeptFeign", value = ServiceNameConstants.PLATFORM_SERVICE)
public interface SysDeptFeign {

    /**
     * Description: 通过部门ID获取部门名称
     * Author: flr
     * Company: 航通星空
     */
    @GetMapping(value = "/dept/getDeptNameById/{deptId}")
    R getDeptNameById(@PathVariable("deptId") Integer deptId, @RequestHeader(SecurityConstants.FROM) String from);
}
