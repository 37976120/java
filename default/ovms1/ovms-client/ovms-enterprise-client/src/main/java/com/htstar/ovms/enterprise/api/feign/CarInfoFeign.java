package com.htstar.ovms.enterprise.api.feign;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author HanGuJi
 * @Description: 车辆信息
 * @date 2020/7/411:50
 */
@FeignClient(contextId = "carInfoFeign", value = ServiceNameConstants.ENTERPRISE_SERVICE)
public interface CarInfoFeign {

    /**
     * 根据deptId查询车辆信息
     * @param deptId
     * @return
     */
    @GetMapping("/carinfo/getCarInfoByDeptId/{deptId}")
    R getCarInfoByDeptId(@PathVariable("deptId")Integer deptId);

    /**
     * 获取车辆的最大ID
     * @param
     * @return
     */
    @GetMapping("/carinfo/selectMaxId")
     int selectMaxId();

    /**
     * 更新车辆状态
     *
     * @param carId
     * @return
     */
    @GetMapping("/carinfo/updateApplyStatus/{carId}")
     R updateApplyStatus(@PathVariable("carId") Integer carId);


//    /**
//     * 查询车辆是否是申请
//     * @param
//     * @return
//     */
//     @GetMapping("/getsetting/{licCode}")
//     int getsetting(@PathVariable("licCode") String licCode,@RequestHeader(SecurityConstants.FROM) String from);
}
