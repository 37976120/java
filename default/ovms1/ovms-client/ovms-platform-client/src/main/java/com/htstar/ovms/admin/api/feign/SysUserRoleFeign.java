package com.htstar.ovms.admin.api.feign;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * Description: 用户角色相关
 * Author: flr
 * Date: Created in 2020/6/29
 * Company: 航通星空
 *
 * Modified By:
 */
@FeignClient(contextId = "sysUserRoleFeign", value = ServiceNameConstants.PLATFORM_SERVICE)
public interface SysUserRoleFeign {

    /**
     * Description: 给用户赋予司机角色
     * @param userId 用户ID 必填
     * @param etpId 企业ID 必填
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    @GetMapping("/role/saveUserDriver/{userId}/{etpId}")
    R saveUserDriver(@PathVariable("userId") Integer userId, @PathVariable("etpId") Integer etpId, @RequestHeader(SecurityConstants.FROM) String from);


    /**
     * 删除用户的司机角色
     * @param userId
     * @param etpId
     * @param from
     * @return
     */
    @GetMapping("/role/removeUserDriver/{userId}/{etpId}")
    R removeUserDriver(@PathVariable("userId") Integer userId, @PathVariable("etpId") Integer etpId, @RequestHeader(SecurityConstants.FROM) String from);


    /**
     * Description: 获取企业当前所有有效的管理员的ID
     * @param etpId 企业ID 必填
     * Author: flr
     * Date: 2020/7/1 15:05
     * Company: 航通星空
     * Modified By:
     */
    @GetMapping("/role/queryAdminIdList/{etpId}")
    List<Integer> queryAdminIdList(@PathVariable("etpId")Integer etpId);

    @GetMapping("/role/queryAdminIdListInner/{etpId}")
    List<Integer> queryAdminIdListInner(@PathVariable("etpId")Integer etpId, @RequestHeader(SecurityConstants.FROM) String from);

}
