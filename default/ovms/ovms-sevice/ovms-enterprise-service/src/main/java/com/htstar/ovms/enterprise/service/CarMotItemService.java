package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CarMotItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarMotItemPageVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 公车年检表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
public interface CarMotItemService extends IService<CarMotItem> {
    /**
     *
     * 年检信息分页
     * @param page
     * @param carItemPageReq
     * @return
     */
    IPage<CarMotItemPageVO> queryPage(CarItemPageReq carItemPageReq);

    /**
     * 保存
     * @param
     * @return
     */
    R saveInfo(CarMotItem carMotItem);

    /**
     * 根据ID删除
     * @param ids
     * @return
     */
    R removeByIds(String ids);

    R update(CarMotItem carMotItem);

    /**
     * 导出
     * @param ids
     * @param response
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
     * 企业年检过期车辆
     * @param etpId
     * @return
     */
    Integer expiredNumByEtp(Integer etpId, LocalDate date);

    /**
     * 根据id获取
     * @return
     */
    CarMotItemPageVO getItemById(Integer id);

    /**
     * 获取用户保存的信息
     * @return
     */
    CarMotItemPageVO getItemByUser();

    /**
     * 退回
     * @param id
     * @param remark
     * @return
     */
    R withdraw(Integer id,String  remark);
}
