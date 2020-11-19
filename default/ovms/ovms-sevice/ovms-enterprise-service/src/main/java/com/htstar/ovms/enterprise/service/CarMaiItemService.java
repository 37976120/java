package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.entity.CarMaiItem;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarMaiItemPageVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 公车保养表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
public interface CarMaiItemService extends IService<CarMaiItem> {
    /**
     *
     * 保养信息分页
     * @param page
     * @param carItemPageReq
     * @return
     */
    IPage<CarMaiItemPageVO> queryPage(CarItemPageReq carItemPageReq);


    /**
     * 保存保养信息
     * @param carMaiItem
     * @return
     */
   R saveInfo(CarMaiItem carMaiItem);

    /**
     * 根据id删除保养信息
     * @param ids
     * @return
     */
   R removeByIds(String ids);

   R update(CarMaiItem carMaiItem);
    /**
     * 导出
     * @param req
     */
   void exportExcel(ExportReq req);
    /**
     * 存档
     * @param id
     * @param itemStatus
     * @return
     */
    R filing(Integer id,Integer itemStatus);

    /**
     * 车辆保养过期数
     * @param etpId
     * @return
     */
    Integer expiredNumByEtp(Integer etpId, LocalDate date);

    /**
     * 根据id获取
     * @return
     */
    CarMaiItemPageVO getItemById(Integer id);

    /**
     * 获取用户保存的信息
     * @return
     */
    CarMaiItemPageVO getItemByUser();


    /**
     * 退回
     * @param id
     * @param remark
     * @return
     */
    R withdraw(Integer id,String  remark);
}
