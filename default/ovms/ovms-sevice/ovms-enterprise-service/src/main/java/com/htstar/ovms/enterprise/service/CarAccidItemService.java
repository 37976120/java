package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CarAccidItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarAccidItemPageVO;

import javax.servlet.http.HttpServletResponse;


/**
 * 车辆事故信息
 *
 * @author lw
 * @date 2020-06-22 10:11:23
 */
public interface CarAccidItemService extends IService<CarAccidItem> {
    /**
     * 新增
     * @param carAccidItem
     * @return
     */
    R saveInfo(CarAccidItem carAccidItem);
    /**
     * 批量删除
     * @param ids
     * @return
     */
    R removeByIds(String ids);

    /**
     * 修改
     * @param carAccidItem
     * @return
     */
    R updateAccidById(CarAccidItem carAccidItem);

    /**
     * 分页查询
     * @param carItemPageReq
     * @return
     */
    IPage<CarAccidItemPageVO> queryPage(CarItemPageReq carItemPageReq);

    /**
     * 导出exc
     * @param ids
     * @param response
     *
     */
    void  exportExcel(ExportReq req);

    /**记录
     * 存档
     * @param id
     * @param itemStatus
     * @return
     */
    R filing(Integer id,Integer itemStatus);


    /**
     * 根据id获取
     * @return
     */
    CarAccidItemPageVO getItemById(Integer id);


    /**
     * 获取用户保存的信息
     * @return
     */
    CarAccidItemPageVO getItemByUser();

    /**
     * 退回
     * @param id
     * @param remark
     * @return
     */
    R withdraw(Integer id,String  remark);

}
