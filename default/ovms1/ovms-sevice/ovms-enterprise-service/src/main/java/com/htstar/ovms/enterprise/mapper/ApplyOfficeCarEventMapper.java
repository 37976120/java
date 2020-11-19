package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.enterprise.api.entity.ApplyCarOrder;
import com.htstar.ovms.enterprise.api.req.PageApplyOrderReq;
import com.htstar.ovms.enterprise.api.vo.ApplyOrderPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 公车申请事件
 * @author flr
 * @date 2020-06-30 18:24:20
 */
@Mapper
public interface ApplyOfficeCarEventMapper extends BaseMapper<ApplyCarOrder> {

    /**
     * Description: 普通分页查询
     * Author: flr
     * Date: 2020/7/8 14:44
     * Company: 航通星空
     * Modified By:
     */
    Page<ApplyOrderPageVO> getPageTask(PageApplyOrderReq req);

    List<Integer> getApplyCarCount(@Param("date") Date date, @Param("etpId") Integer etpId);


    String getUserNinckName(@Param("driverId") Integer driverId);
}
