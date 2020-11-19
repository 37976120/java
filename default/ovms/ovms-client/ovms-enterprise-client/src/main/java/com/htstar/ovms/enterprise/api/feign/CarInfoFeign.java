package com.htstar.ovms.enterprise.api.feign;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public int selectMaxId();
}
