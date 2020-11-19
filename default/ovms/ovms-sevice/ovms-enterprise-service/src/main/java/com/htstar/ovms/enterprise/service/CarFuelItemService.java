package com.htstar.ovms.enterprise.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CarFuelItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarFuelItemPageVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 公车加油
 *
 * @author lw
 * @date 2020-06-08 13:48:44
 */
public interface CarFuelItemService extends IService<CarFuelItem> {
    /**
     * 新增加油信息
     * @param carFuelItem
     * @return
     */
    R saveInfo(CarFuelItem carFuelItem);

    /**
     * 根据Id删除
     * @param ids
     * @return
     */
    R removeByIds(String ids);

    /**
     * 修改
     * @param carFuelItem
     * @return
     */
    R updateFuelById(CarFuelItem carFuelItem);


    /**
     * 加油信息分页
     * @param carItemPageReq
     * @return
     */
    IPage<CarFuelItemPageVO> queryPage(CarItemPageReq carItemPageReq);

    /**
     * 导出excel
     * @param ids
     * @param response
     * @throws IOException
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
     * 根据id查询加油信息
     * @param id
     * @return
     */
    CarFuelItemPageVO getCarFuelItemById(Integer id);


    /**
     * 获取用户保存的信息
     * @return
     */
    CarFuelItemPageVO getItemByUser();


    /**
     * 退回
     * @param id
     * @param remark
     * @return
     */
    R withdraw(Integer id,String  remark);
}
