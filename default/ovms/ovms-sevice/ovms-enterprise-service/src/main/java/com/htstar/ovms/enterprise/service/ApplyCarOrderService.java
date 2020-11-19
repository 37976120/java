package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.ApplyCarOrder;
import com.htstar.ovms.enterprise.api.req.*;
import com.htstar.ovms.enterprise.api.vo.ApplyCarDataVo;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderVO;
import com.htstar.ovms.enterprise.api.vo.ApplyOrderPageVO;

import java.util.Date;

/**
 * 公车申请事件
 *
 * @author flr
 * @date 2020-06-30 18:24:20
 */
public interface ApplyCarOrderService extends IService<ApplyCarOrder> {

    /**
     * Description:提交公车申请
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    R commitOfficeCarApply(ApplyCarOrderReq req);

    /**
     * Description: 处理公车申请
     * Author: flr
     * Date: 2020/7/2 16:05
     * Company: 航通星空
     * Modified By:
     */
    R handleOfficeCarApply(HandleOfficeCarApplyReq req);

    R<ApplyCarOrderVO> getVOById(Integer id);

    /**
     * Description: 分配车辆
     * Author: flr
     * Date: 2020/7/8 11:00
     * Company: 航通星空
     * Modified By:
     */
    R<ApplyCarOrderVO> distributionCar(DistributionCarReq req);

    /**
     * Description: 分配司机
     * Author: flr
     * Date: 2020/7/8 12:15
     * Company: 航通星空
     * Modified By:
     */
    R distributionDriver(DistributionDriverReq req);

    /**
     * Description: 提车上报
     * Author: flr
     * Date: 2020/7/8 12:21
     * Company: 航通星空
     * Modified By:
     */
    R saveGiveCarData(SaveGiveCarDataReq req);

    /**
     * Description: 还车上报
     * Author: flr
     * Date: 2020/7/8 14:26
     * Company: 航通星空
     * Modified By:
     */
    R saveReturnCarData(SaveReturnCarDataReq req);

    /**
     * Description: 普通分页查询
     * Author: flr
     * Date: 2020/7/8 14:44
     * Company: 航通星空
     * Modified By:
     */
    R<Page<ApplyOrderPageVO>> getPage(PageApplyOrderReq req);

    /**
     *
     * 用车数据总览
     * @param date
     * @param etpId
     * @return
     */
    ApplyCarDataVo applyCarData(Date date, Integer etpId);


}
