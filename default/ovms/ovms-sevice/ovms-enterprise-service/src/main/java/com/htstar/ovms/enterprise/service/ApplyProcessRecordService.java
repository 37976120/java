package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.ApplyProcessRecord;
import com.htstar.ovms.enterprise.api.vo.DetailApplyCarRecordVO;

import java.util.List;

/**
 * 流程记录
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
public interface ApplyProcessRecordService extends IService<ApplyProcessRecord> {
    /**
     * Description: 获取流程记录
     * Author: flr
     * Date: 2020/7/23 14:11
     * Company: 航通星空
     * Modified By:
     */
    R<List<DetailApplyCarRecordVO>> getRecord(Integer orderId);
    
    
    /**
     * Description:
     * Author: flr
     * Date: 2020/8/10 10:19
     * Company: 航通星空
     * Modified By: 
     */
    ApplyProcessRecord getDistribution(Integer orderId);

    String getByNodeType(int orderId,int nodeType);

    String getNickNameByOPType(int orderId, int operationType);
}
