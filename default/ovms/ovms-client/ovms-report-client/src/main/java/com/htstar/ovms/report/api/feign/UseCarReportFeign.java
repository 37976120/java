package com.htstar.ovms.report.api.feign;

import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/30
 * Company: 航通星空
 * Modified By:
 */
@FeignClient(contextId = "useCarReportFeign", value = ServiceNameConstants.REPORT_SERVICE)
public interface UseCarReportFeign {

}
