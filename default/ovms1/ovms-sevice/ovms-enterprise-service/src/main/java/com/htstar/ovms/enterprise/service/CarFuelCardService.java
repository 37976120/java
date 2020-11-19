package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CarFuelCard;
import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.req.RechargeReq;
import com.htstar.ovms.enterprise.api.vo.FuelCardPageVO;

/**
 * 油卡
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
public interface CarFuelCardService extends IService<CarFuelCard> {
    /**
     * 新增
     * @param carFuelCard
     * @return
     */
    R saveInfo(CarFuelCard carFuelCard);
    /**
     * 批量删除
     * @param ids
     * @return
     */
    R removeByIds(String ids);

    /**
     * 修改
     * @param carFuelCard
     * @return
     */
    R updateFuelById(CarFuelCard carFuelCard);

    /**
     * 分页
     * @param carFileManageReq
     * @return
     */
    IPage<FuelCardPageVO> queryPage(CarFileManageReq carFileManageReq);






    /**
     * 导出
     * @param req
     */
    void exportExcel(CarFileManageReq req);

    /**
     *
     * 油卡扣费
     * @param cardId 油卡编号
     * @param cost 扣费金额
     */
    Integer fuelDeduction(Integer cardId,Integer cost);

    /**
     * 检查是否能绑定
     * @param cardId 卡号
     * @param cost  待扣费金额
     * @return
     */
    boolean checkIsBinding(Integer cardId,Integer cost);

    /**
     * 费用充值
     * @param req
     * @return
     */
    R recharge(RechargeReq req);

}
