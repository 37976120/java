package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.dto.AreaDTO;
import com.htstar.ovms.device.api.dto.CarSecurityAlarmDTO;
import com.htstar.ovms.device.api.dto.ExportMonitoringDTO;
import com.htstar.ovms.device.api.dto.MonitoringDTO;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.entity.DriverMapAreaCarInfo;
import com.htstar.ovms.device.api.vo.AreaVo;
import com.htstar.ovms.device.api.vo.CarSecurityAlarmVO;
import com.htstar.ovms.device.api.vo.DeviceAlarmVO;
import com.htstar.ovms.device.api.vo.MonitoringVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 设备警情
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Mapper
public interface DeviceMapAreaCarInfoMapper extends BaseMapper<DriverMapAreaCarInfo> {
    /**
     * 根据车辆id查询所属地图标签
     * @param mapCarInfoId
     * @return
     */
    String getByMapCarInfoId(@Param("mapCarInfoId") Integer mapCarInfoId);
}
