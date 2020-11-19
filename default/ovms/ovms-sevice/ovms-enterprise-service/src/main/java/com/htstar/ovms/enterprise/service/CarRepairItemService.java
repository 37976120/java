package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CarRepairItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarRepairItemPageVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 公车维修表

 *
 * @author lw
 * @date 2020-06-08 13:48:46
 */
public interface CarRepairItemService extends IService<CarRepairItem> {
    /**
     * 维修信息分页
     * @param page
     * @param carItemPageReq
     * @return
     */
    IPage<CarRepairItemPageVO> queryPage(CarItemPageReq carItemPageReq);
    /**
     * 保存维修信息
     * @param carRepairItem
     * @return
     */
    R  saveInfo(CarRepairItem carRepairItem);


    /**
     * 根据id删除
     * @param id
     * @return
     */
    R removeByIds(String ids);

    /**
     * 修改
     * @param carRepairItem
     * @return
     */
    R update(CarRepairItem carRepairItem);

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
     * 退回
     * @param id
     * @param remark
     * @return
     */
    R withdraw(Integer id,String  remark);


    /**
     * 根据id获取
     * @return
     */
    CarRepairItemPageVO getItemById(Integer id);

    /**
     * 获取用户保存的信息
     * @return
     */
    CarRepairItemPageVO getItemByUser();
}
