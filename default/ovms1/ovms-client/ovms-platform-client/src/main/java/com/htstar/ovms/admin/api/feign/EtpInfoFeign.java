package com.htstar.ovms.admin.api.feign;

import com.htstar.ovms.admin.api.vo.EtpInfoSVo;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(contextId = "EtpInfoFeign", value = ServiceNameConstants.PLATFORM_SERVICE)
public interface EtpInfoFeign {
    /**
     * 查询当前企业和子企业和子企业机构
     */

    @PostMapping("/etp/getEtpTrees1/{etpId}")
    List<EtpInfoSVo> getCurrentAndParents1(@PathVariable("etpId") Integer etpId);

    /**
     *
     */
    @GetMapping("/etp/etpTreeNoDept/{etpId}")
    public R getEtpTreeNoDeptHsl(@PathVariable("etpId") Integer etpId);
}
