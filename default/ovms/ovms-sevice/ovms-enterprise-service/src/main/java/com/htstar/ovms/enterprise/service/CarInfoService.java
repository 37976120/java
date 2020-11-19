package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.admin.api.vo.CarInfoVO;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.dto.*;
import com.htstar.ovms.enterprise.api.entity.CarInfo;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderAndDriverVO;
import com.htstar.ovms.enterprise.api.vo.CarRealTimeDrivingVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公车表

 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
public interface CarInfoService extends IService<CarInfo> {
    /**
     * 保存车辆基本信息 行驶证
     * @param carInfo
     * @return
     */
    R saveBaseInfo(CarInfoMapAreaDTO carInfo);

    /**
     * 添加行驶证信息
     * @param carInfo
     * @return
     */
    R saveDrivingLicenseInfo(CarInfo carInfo);



    /**
     * 绑定设备
     * @param carId
     * @param deviceSn
     * @return
     */
    R bindingDeviceSn(Integer carId,String deviceSn);

    /**
     * 修改基本信息
     * @param carInfo
     * @return
     */
    R updateBaseInfo(CarInfoMapAreaDTO carInfo);

    /**
     * 修改驾驶证信息
     * @param carInfo
     * @return
     */
    R updateDrivingLicenseInfo(CarInfo carInfo);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    R removeById(Integer id);

    /**
     * 修改车辆信息
     * @param carInfo
     * @return
     */
    R updateCarInfo(CarInfo carInfo);

    /**
     * 车辆列表分页
     * @param carInfoDTO
     * @return
     */
   Page<CarInfo> queryPage (CarInfoDTO carInfoDTO);

    void  export(CarInfoDTO carInfoDTO);



    /**
     * 后台车辆信息分页查询
     * @param carInfoDTO
     * @return
     */
    Page<CarInfo> selectCarInfoPage(CarInfoDTO carInfoDTO);




    /**
     * 根据部门id获取部门名称
     * @param deptId
     * @return
     */
    String getCarDeptNameById(Integer deptId);




    /**
     * 车辆信息导出excel统一接口
     * @param rows
     * @param
     * @param
     */
    void carExportUtil(List<Map<String,Object>> rows, String fileName) ;

    /**
     * 根据车辆id查询
     * @param id
     * @return
     */
    CarInfoMapAreaDTO selectCarInfoById(Integer id);

    /**
     *
     * @param carInfoDTO
     * @return
     */
    List<CarInfo> exportCarInfo(CarInfoExportDTO carInfoDTO);

    /**
     * 根据ids集合逻辑删除车辆信息
     * @param ids
     * @return
     */
    R updateByIds(String ids);

    /**
     * 分页车辆定位展示列表
     * @return
     */
    R selectCarLocationPage(CarLocationNoPageDTO carLocationNoPageDTO);
    /**
     * 车辆定位纬度集合
     * @return
     */
    public R selectCarLocations(CarLocationNoPageDTO carLocationDTO);

    /**
     * 用户车辆
     * 实时驾驶数据
     * @param deviceSn
     * @return
     */
    CarRealTimeDrivingVO selectCarDriving(String deviceSn);



    /**
     * 司机用户名下车辆
     * @param userId
     * @return
     */
    List<String> getLicCodeByUser(Integer userId);


    /**
     * 根据deptId查询车辆信息
     * @param deptId
     * @return
     */
    List<CarInfoVO> getCarInfoByDeptId(Integer deptId);


    /**
     * 根据角色获取分页参数
     * @param carItemPageReq
     * @return
     */
    CarItemPageReq getPageReqByRole(CarItemPageReq carItemPageReq);


    /**
     * 排班车辆信息
     * @return
     */
    Page<ApplyCarOrderAndDriverVO> selectCarPage(CarDriverScheduleDTO carDriverScheduleDTO);
    /**
     * 排班车辆信息不分页
     * @return
     */
    List<ApplyCarOrderAndDriverVO>selectCarList(CarDriverScheduleNoPageDTO carDriverScheduleNoPageDTO);
    /**
     * 排班信息总数
     * @return
     */
    Integer selectCarPageTotal(Integer etpId,String licCodeOrriverName,Integer scheduleStatus);
    /**
     * 保存车辆照片
     * @param id
     * @param addr
     * @return
     */
    R saveCarPhoto(Integer id,String addr);

    /**
     * 获取企业的车辆数量
     * @param etpId
     * @return
     */
    Integer getCarCount(Integer etpId);

    /**
     * app端分页 解绑
     * @param carInfoDTO
     * @return
     */
    R<Page<CarInfo>> appQueryPage (CarInfoDTO carInfoDTO);


    /**
     * 解绑司机,根据司机id
     * @param driverId
     * @return
     */
    R untieCarByDriverId(Integer driverId);




}
