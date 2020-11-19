package com.htstar.ovms.enterprise.api.feign;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeWhereDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/20
 * Company: 航通星空
 * Modified By:
 */
@FeignClient(contextId = "carSchedulingTimeFeign", value = ServiceNameConstants.ENTERPRISE_SERVICE)
public interface CarSchedulingTimeFeign {

    /**
     * 更具日期时间判断是否可以使用车，排班
     * @return
     */
    @PostMapping("/carschedulingtime/rqweek" )
     int getCount(CarSchedulingTimeWhereDTO carSchedulingTimeWhereDTO,@RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 更具日期时间判断是否可以使用车，排班新车
     * @return
     */
    @GetMapping("/carschedulingtime/xcrqweek/{licCode}")
    int getlicCodeCount(@PathVariable("licCode") String licCode,@RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 查询车辆是否是申请
     * @param
     * @return
     */
    @GetMapping("/carschedulingtime/getsettings/{licCode}")
    int getsetting(@PathVariable("licCode") String licCode,@RequestHeader(SecurityConstants.FROM) String from);
}
