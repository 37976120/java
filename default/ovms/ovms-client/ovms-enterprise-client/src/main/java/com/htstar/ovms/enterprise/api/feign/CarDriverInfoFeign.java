package com.htstar.ovms.enterprise.api.feign;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CarDriverInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/29
 * Company: 航通星空
 * Modified By:
 * @author liuwei
 */
@FeignClient(contextId = "carDriverInfoFeign", value = ServiceNameConstants.ENTERPRISE_SERVICE)
public interface CarDriverInfoFeign {


    /**
     * 注册时同步司机
     * @param userId
     * @param etpId
     * @return
     */
    @GetMapping("/cardriverinfo/saveDriver/{userId}/{etpId}")
    R saveDriverByUserId(@PathVariable("userId") Integer userId, @PathVariable("etpId")Integer etpId);

    /**
     * 删除用户时同步删除司机
     * @param userId
     * @return
     */
    @GetMapping("/cardriverinfo/delDriver/{userId}")
    R delDriverByUserId(@PathVariable("userId") Integer userId);
}
