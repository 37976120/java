package com.htstar.ovms.enterprise.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CarInsItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarInsItemPageVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 公车保险表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
public interface CarInsItemService extends IService<CarInsItem> {
    /**
     * 保险信息分页
     * @param carItemPageReq
     * @return
     */
    IPage<CarInsItemPageVO> queryPage(CarItemPageReq carItemPageReq);

    /**
     * 保存
     * @param carInsItem
     * @return
     */
    R saveInfo(CarInsItem carInsItem);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    R removeByIds(String id);

    R update(CarInsItem carInsItem);
    /**
     * 导出excel
     * @param req
     */
    void exportExcel(ExportReq req);

    /**
     * 存档 过期
     * @param id
     * @param itemStatus
     * @return
     */
    R filing(Integer id,Integer itemStatus);

    /**
     * 企业保险过期车辆
     * @param etpId
     * @return
     */
    Integer expiredNumByEtp(Integer etpId, LocalDate date);

    /**
     * 根据id获取
     * @return
     */
    CarInsItemPageVO getItemById(Integer id);

    /**
     * 获取用户保存的信息
     * @return
     */
    CarInsItemPageVO getItemByUser();


    /**
     * 退回
     * @param id
     * @param remark
     * @return
     */
    R withdraw(Integer id,String  remark);

}
