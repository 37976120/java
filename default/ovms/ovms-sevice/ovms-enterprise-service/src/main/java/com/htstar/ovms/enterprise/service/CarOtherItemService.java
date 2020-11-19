package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CarOtherItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarOtherItemPageVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 公车其他项目表

 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
public interface CarOtherItemService extends IService<CarOtherItem> {

    IPage<CarOtherItemPageVO> queryPage(CarItemPageReq carItemPageReq);
    /**
     * 新增
     * @param carOtherItem
     * @return
     */
    R saveInfo(CarOtherItem carOtherItem);

    /**
     * 删除
     * @param ids
     * @return
     */
    R removeByIds(String ids);

    /**
     * 修改
     * @param carOtherItem
     * @return
     */
    R update(CarOtherItem carOtherItem);
    /**
     * 导出exc
     * @param
     *
     */
    void  exportExcel(ExportReq req);

    /**
     * 存档
     * @param id
     * @param itemStatus
     * @return
     */
    R filing(Integer id,Integer itemStatus);

    /**
     * 批量保存
     * @param carOtherItem
     * @return
     */
    R batchSave(CarOtherItem carOtherItem);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CarOtherItemPageVO getItemById(int id);

    /**
     * 退回
     * @param id
     * @param remark
     * @return
     */
    R withdraw(Integer id,String  remark);


    /**
     * 获取用户保存的
     * @param itemType
     * @return
     */
    CarOtherItemPageVO getItemByUser(Integer itemType);

    R temporarySave(CarOtherItemPageVO carOtherItem);

    R getTemporarySave();
}
