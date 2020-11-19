package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.device.api.dto.DeviceDTO;
import com.htstar.ovms.device.api.dto.DeviceExportDTO;
import com.htstar.ovms.device.api.entity.Device;
import com.htstar.ovms.device.api.vo.DeviceDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备
 *
 * @author flr
 * @date 2020-06-09 11:25:24
 */
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
    /**
     * 分页查询设备信息 后台
     *
     * @param userDTO
     * @return
     */
    @SqlParser(filter = true)
    Page<DeviceDataVO> selectDevicePage(@Param("query") DeviceDTO userDTO);

    /**
     * 根据设备编号 判断是否绑定车辆
     *
     * @param deviceSn
     * @return
     */
    boolean IsBindByDeviceSn(@Param("deviceSn") String deviceSn);

    /**
     * 根据设备编号查询设备详细信息
     *
     * @param deviceSn
     * @return
     */
    DeviceDataVO selectDeviceDataVOByDeviceSn(@Param("deviceSn") String deviceSn);

    /**
     * 删除绑定关系
     *
     * @param deviceSn
     * @return
     */
    boolean removeBindingByDeviceSns(List<String> deviceSn);

    /**
     * Description: 检测是否是允许连接的设备,放回设备ID
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    Integer getAllowStatus(@Param("deviceSn") String deviceSn);

    /**
     * 设备信息导出
     *
     * @param deviceDTO
     * @return
     */
    List<DeviceDataVO> exportDeviceInfo(@Param("query") DeviceExportDTO deviceDTO,@Param("list")List<String> list);

    /**
     * 根据设备编号获取设备信息
     *
     * @param deviceSn
     * @return
     */
    DeviceDataVO getDeviceVOByDeviceSns(@Param("deviceSn")String deviceSn);
    /**
     * 根据设备找车辆最终找到所属车辆的用户
     */
    int getuserId(@Param("deviceSn")String deviceSn);

    /**
     * 根据设备找车辆
     */
    String getLicCode(@Param("deviceSn") String deviceSn);

    /**
     * 根据设备找车辆Id
     */
    Integer getCarId(String deviceSn);
}

