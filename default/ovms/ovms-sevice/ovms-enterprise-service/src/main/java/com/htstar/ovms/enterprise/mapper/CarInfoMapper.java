package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.vo.CarInfoVO;
import com.htstar.ovms.enterprise.api.dto.*;
import com.htstar.ovms.enterprise.api.entity.CarInfo;
import com.htstar.ovms.enterprise.api.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公车表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Mapper
public interface CarInfoMapper extends BaseMapper<CarInfo> {

    /**
     * 后台车辆信息分页查询
     *
     * @param carInfoDTO
     * @return
     *
     */
    @SqlParser(filter = true)
    Page<CarInfo> selectCarInfoPage(@Param("query") CarInfoDTO carInfoDTO);

    /**
     * 根据车辆id查询
     *
     * @param id
     * @return
     */
    @SqlParser(filter = true)
    CarInfoMapAreaDTO selectCarInfoById(@Param("id") Integer id);

    /**
     * 后台 导出车辆信息
     * @param carInfoDTO
     * @return
     */
    List<CarInfo> exportCarInfo(@Param("query") CarInfoExportDTO carInfoDTO, @Param("list")List<Integer> list);

    /**
     * 分页车辆定位展示列表
     * @return
     */
    Page<CarLocationVO> selectCarLocationPage(@Param("query") CarLocationDTO carLocationDTO);

    /**
     * 查询所有车辆经纬度集合
     * @return
     */
    List<CarLocationsLatLngVO> selectCarLocations(@Param("licCode")String licCode, @Param("etpId") Integer etpId);

    /**
     * 实时驾驶数据
     * @param deviceSn
     * @return
     */
    CarRealTimeDrivingVO selectCarDriving(@Param("deviceSn") String deviceSn);

    /**
     * 企业端车辆分页
     * @param carInfoDTO
     * @return
     */
    @SqlParser(filter = true)
    Page<CarInfo> queryPage(@Param("query")  CarInfoDTO carInfoDTO);

    /**
     * 获取正在使用的车辆
     * @param etpId
     * @return
     */
    List<Integer> getUsingCarIds(Integer etpId);



    /**
     * 获取所有id
     * @param etpId
     * @return
     */
    List<Integer> getIdList(Integer etpId);

    /**
     * 导出
     * @param idList
     * @param carInfoDTO
     * @return
     */
    List<CarInfo> exportExcel(  @Param("query") CarInfoDTO carInfoDTO);


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
    List<CarInfoVO> getCarInfoByDeptId(@Param("deptId") Integer deptId);


    /**
     * 排班车辆信息
     * @return
     */
    @SqlParser(filter = true)
    Page<ApplyCarOrderAndDriverVO> selectCarPage(@Param("query") CarDriverScheduleDTO carDriverScheduleDTO);

    /**
     * 根据司机id解绑车辆
     * @param driverId
     */
    void untieCarByDriverId(Integer driverId);
    /**
     * 排班车辆信息  总数
     * @return
     */

    Integer selectCarPageTotal(@Param("etpId")Integer etpId,
                               @Param("licCodeOrriverName")String licCodeOrriverName,
                               @Param("scheduleStatus") Integer scheduleStatus);
    /**
     * 排班车辆信息不分页
     * @return
     */
    @SqlParser(filter = true)
    List<ApplyCarOrderAndDriverVO> selectCarList(@Param("query") CarDriverScheduleNoPageDTO carDriverScheduleNoPageDTO);
}
