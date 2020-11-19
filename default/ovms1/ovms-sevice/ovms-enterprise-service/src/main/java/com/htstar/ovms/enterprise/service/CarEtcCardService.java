package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CarEtcCard;
import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.req.RechargeReq;
import com.htstar.ovms.enterprise.api.vo.EtcCardPageVO;

/**
 * etc卡
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
public interface CarEtcCardService extends IService<CarEtcCard> {
    /**
     * 新增
     * @param carEtcCard
     * @return
     */
    R saveInfo(CarEtcCard carEtcCard);
    /**
     * 批量删除
     * @param ids
     * @return
     */
    R removeByIds(String ids);

    /**
     * 修改
     * @param carEtcCard
     * @return
     */
    R updateEtcById(CarEtcCard carEtcCard);


    /**
     * 分页
     * @param carFileManageReq
     * @return
     */
    IPage<EtcCardPageVO> queryPage(CarFileManageReq carFileManageReq);



    /**
     *
     * 导出
     * @param req
     */
    void exportExcel(CarFileManageReq req);

    /**
     *
     * etc卡扣费
     * @param cardId etc卡
     * @param cost 扣费金额
     */
    Integer etcDeduction(Integer cardId,Integer cost);

    /**
     * 检查是否能绑定
     * @param cardId 卡id
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
