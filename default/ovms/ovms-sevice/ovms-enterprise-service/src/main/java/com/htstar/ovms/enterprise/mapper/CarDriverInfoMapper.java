package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleDTO;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleNoPageDTO;
import com.htstar.ovms.enterprise.api.entity.CarDriverInfo;
import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderAndDriverVO;
import com.htstar.ovms.enterprise.api.vo.CarDriverPageVO;
import com.htstar.ovms.enterprise.api.vo.DriverVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 司机
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@Mapper
public interface CarDriverInfoMapper extends BaseMapper<CarDriverInfo> {

    /**
     * 司机分页
     * @param carFileManageReq
     * @return
     */
    IPage<CarDriverPageVO> queryPage(@Param("carFileManageReq")CarFileManageReq carFileManageReq);

    /**
     * 获取所有id
     * @param etpId
     * @return
     */
    List<Integer> getIdList(int etpId);

    /**
     * 获取正在使用的司机
     * @param etpId
     * @return
     */
    List<Integer> getUsingDriverUserIds(Integer etpId);

    /**
     * 导出
     * @param
     * @return
     */
    List<CarDriverPageVO> exportExcel(@Param("req")CarFileManageReq req);

    /**
     * Description: 获取司机VO
     * Author: flr
     * Date: 2020/7/7 17:10
     * Company: 航通星空
     * Modified By:
     */
    DriverVO getDriverVO(@Param("userId") Integer userId);


    /**
     * 司机信息
     * @return
     */
    @SqlParser(filter = true)
    Page<ApplyCarOrderAndDriverVO> selectDriverInfoPage(@Param("query") CarDriverScheduleDTO carDriverScheduleDTO);

    /**
     * 查询不可排班司机信息
     * @param carDriverScheduleDTO
     * @return
     */
    @SqlParser(filter = true)
    Page<ApplyCarOrderAndDriverVO> selectNoDriverInfoPage(@Param("query") CarDriverScheduleDTO carDriverScheduleDTO);

    String getNickName(@Param("userId") Integer userId);

    /**
     * 查询不可排班司机信息总数
     * @param etpId
     * @return
     */
    Integer selectNoDriverInfoPageTotal(@Param("etpId")Integer etpId,
                                        @Param("licCodeOrriverName")String licCodeOrriverName,
                                        @Param("scheduleStatus") Integer scheduleStatus);
    /**
     * 查询不可排班司机信息不分页
     * @param
     * @return
     */
    List<ApplyCarOrderAndDriverVO> selectNoDriverInfoList(@Param("query")CarDriverScheduleNoPageDTO carDriverScheduleNoPageDTO);
}
