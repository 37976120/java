package com.htstar.ovms.admin.api.feign;

import com.htstar.ovms.admin.api.entity.SysDictItem;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Description: 字典
 * Author: JinZhu
 * Date: Created in 2020/6/16
 * Company: 航通星空
 * Modified By:
 */
@FeignClient(contextId = "SysDictFeign", value = ServiceNameConstants.PLATFORM_SERVICE)
public interface SysDictFeign {

    /**
     * 通过字典类型和字典警情查找警情字典类型
     *
     * @param  类型
     * @return 同类型字典
     */
    @GetMapping("/dict/types/{value}")
    SysDictItem getDictByTypeValue( @PathVariable("value") Integer value);
}
