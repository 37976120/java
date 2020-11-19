package com.htstar.ovms.enterprise.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.enterprise.api.entity.CarAlarmRemind;
import com.htstar.ovms.enterprise.api.entity.CarDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 车-设备 中间表
 *
 * @author lw
 * @date 2020-06-28 16:48:51
 */
@Mapper
public interface CarDeviceMapper extends BaseMapper<CarDevice> {

    /**
     * 获取企业已经绑定的设备数
     * @param etpId
     * @return
     */
    Integer getDeviceSnByEtp(Integer etpId);

    Integer getOnline(Integer etpId);

    /**
     * 设置警情
     * @param carAlarmRemind
     */
    void setAlarmRemind(@Param("req") CarAlarmRemind carAlarmRemind);

    CarAlarmRemind getAlarmRemind(Integer carId);
}
