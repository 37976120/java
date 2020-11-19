package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.dto.DeviceDTO;
import com.htstar.ovms.device.api.dto.DeviceExportDTO;
import com.htstar.ovms.device.api.entity.Device;
import com.htstar.ovms.device.api.vo.DeviceDataVO;

import java.util.List;

/**
 * 设备
 *
 * @author flr
 * @date 2020-06-09 11:25:24
 */
public interface DeviceService extends IService<Device> {


    /**
     * 查询设备分页列表信息
     *
     * @param deviceDTO
     * @return
     */
    Page<DeviceDataVO> selectDevicePage(DeviceDTO deviceDTO);
    /**
     * 根据设备找车辆最终找到所属车辆的用户
     */
     int getuserId(String deviceSn);
    /**
     * 获取企业ID
     *
     * @param
     */
    int getEtpIds(String deviceSn);
    /**
     * 根据设备找车辆
     */
    String getLicCode(String deviceSn);

    /**
     * 根据设备找车辆Id
     */
    Integer getCarId(String deviceSn);
    /**
     * 根据设备编号 删除设备
     *
     * @param deviceSns
     */
    R removeByDeviceSns(String deviceSns);

    /**
     * 根据设备编号查询设备详细信息
     *
     * @param deviceSn
     * @return
     */
     DeviceDataVO selectDeviceDataVOByDeviceSn(String deviceSn);

    /**
     * 通过deviceSn解除设备绑定
     *
     * @param deviceSn
     * @return
     */
    boolean removeBindingByDeviceSns(String deviceSn, Integer isClearAll);

    /**
     * Description: 检测是否是允许连接的设备
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    Integer getAllowStatus(String deviceSn);

    /**
     * 设备导出excel
     *
     * @param deviceDTO
     * @return
     */
    List<DeviceDataVO> exportDeviceInfo(DeviceExportDTO deviceDTO);

    /**
     * 根据设备编号获取设备信息
     *
     * @param deviceSns
     * @return
     */
    List<DeviceDataVO> getDeviceVOByDeviceSns(String deviceSns);

    /**
     * 添加设备信息
     *
     * @param device
     * @return
     */
    R saveDevice(Device device);

    /**
     * 修改设备信息
     *
     * @param device
     * @return
     */
    R updateDevice(Device device);

    /**
     * 检查设备是否可以绑定
     * @param deviceSn 设备号
     * @return
     */
    R checkDeviceIsBinding(String deviceSn);
}
