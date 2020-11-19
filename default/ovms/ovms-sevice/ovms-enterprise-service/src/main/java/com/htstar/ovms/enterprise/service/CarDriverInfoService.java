package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleDTO;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleNoPageDTO;
import com.htstar.ovms.enterprise.api.entity.CarDriverInfo;
import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderAndDriverVO;
import com.htstar.ovms.enterprise.api.vo.CarDriverPageVO;
import com.htstar.ovms.enterprise.api.vo.DriverVO;

import java.util.List;

/**
 * 司机
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
public interface CarDriverInfoService extends IService<CarDriverInfo> {
    /**
     * 新增
     * @param carDriverInfo
     * @return
     */
    R saveInfo(CarDriverInfo carDriverInfo);
    /**
     * 批量删除
     * @param ids
     * @return
     */
    R removeByIds(String ids);

    /**
     * 修改
     * @param carDriverInfo
     * @return
     */
    R updateDriverById(CarDriverInfo carDriverInfo);

    /**
     * 注册时候添加司机
     * @param userId
     * @param etpId
     * @return
     */
   Boolean saveDriverByUserId(Integer userId,Integer etpId);

    /**
     * 用户信息删除司机
     * @param userId
     * @return
     */
   Boolean delDriverByUserId(Integer userId);

    /**
     * 分页
     * @param carFileManageReq
     * @return
     */
    IPage<CarDriverPageVO> queryPage(CarFileManageReq carFileManageReq);

    /**
     * 导出
     * @param req
     */
    void exportExcel(CarFileManageReq req);

    /**
     * Description: 获取企业所有的司机
     * Author: flr
     * Date: 2020/7/4 14:24
     * Company: 航通星空
     * Modified By:
     */
    List<Integer> queryAllDriver(Integer etpId);

    /**
     * Description: 获取司机VO
     * Author: flr
     * Date: 2020/7/7 17:10
     * Company: 航通星空
     * Modified By:
     */
    DriverVO getDriverVO(Integer driveUserId);

    /**
     * 排班司机信息
     * @return
     */
    Page<ApplyCarOrderAndDriverVO> selectDriverInfoPage(CarDriverScheduleDTO carDriverScheduleDTO);

    /**
     * 不可排班司机
     * @param
     * @return
     */
     Page<ApplyCarOrderAndDriverVO> selectNoDriverInfoPage(CarDriverScheduleDTO carDriverScheduleDTO);
    /**
     * 不可排班司机,不分页
     * @param
     * @return
     */
     List<ApplyCarOrderAndDriverVO> selectNoDriverInfoList(CarDriverScheduleNoPageDTO carDriverScheduleNoPageDTO);
     String getNickName(Integer userId);
    /**
     * 不可排班司机 总数
     * @param
     * @return
     */
    Integer selectNoDriverInfoPageTotal(Integer etpId,String licCodeOrriverName, Integer scheduleStatus);
}
