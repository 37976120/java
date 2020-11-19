package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.dto.ApplyCostProcessDTO;
import com.htstar.ovms.enterprise.api.entity.CarEtcItem;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarEtcItemPageVO;
import com.htstar.ovms.report.api.entity.ReportDriverExpense;
import com.htstar.ovms.report.api.entity.ReportExpense;

import javax.servlet.http.HttpServletResponse;


/**
 * etc通行记录

 *
 * @author lw
 * @date 2020-06-22 10:11:23
 */
public interface CarEtcItemService extends IService<CarEtcItem> {
    /**
     * 新增
     * @param carEtcItem
     * @return
     */
    R saveInfo(CarEtcItem carEtcItem);
    /**
     * 批量删除
     * @param ids
     * @return
     */
    R removeByIds(String ids);

    /**
     * 修改
     * @param carEtcItem
     * @return
     */
    R updateEtcById(CarEtcItem carEtcItem);

    /**
     * 分页查询
     * @param carItemPageReq
     * @return
     */
    IPage<CarEtcItemPageVO> queryPage(CarItemPageReq carItemPageReq);

    /**
     * 导出exc
     * @param
     * @param req
     *
     */
    void  exportExcel(ExportReq req);

    /**
     * 存档
     * @param id
     * @param itemStatus
     * @return
     */
    R filing(Integer id, Integer itemStatus);


    /**
     * 车辆费用到报表
     * @param reportExpense
     */
    void saveReportExpense(ReportExpense reportExpense);

    /**
     * 保存司机费用到报表
     * @param reportDriverExpense
     */
    void saveReportDriverExpense(ReportDriverExpense reportDriverExpense);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    CarEtcItemPageVO getCarEtcItemById(Integer id);

    /**
     * 获取用户保存的信息，只有一条
     * @return
     */
    CarEtcItemPageVO getCarEtcItemByUser();

    /**
     * 退回
     * @param id
     * @param remark
     * @return
     */
    R withdraw(Integer id,String  remark);

    /**
     * 新增审批
     * @param applyCostProcessDTO
     */
   void addOrUpdateCostProcessRecord(ApplyCostProcessDTO applyCostProcessDTO);
}
