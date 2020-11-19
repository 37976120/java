package com.htstar.ovms.enterprise.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.ApplyProcessRecord;
import com.htstar.ovms.enterprise.api.vo.DetailApplyCarRecordVO;
import com.htstar.ovms.enterprise.mapper.ApplyProcessRecordMapper;
import com.htstar.ovms.enterprise.service.ApplyProcessRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程记录
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
@Service
public class ApplyProcessRecordServiceImpl extends ServiceImpl<ApplyProcessRecordMapper, ApplyProcessRecord> implements ApplyProcessRecordService {

    /**
     * Description: 获取流程记录
     * Author: flr
     * Date: 2020/7/23 14:12
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R<List<DetailApplyCarRecordVO>> getRecord(Integer orderId) {
        List<DetailApplyCarRecordVO> list = baseMapper.queryDetailApplyCarRecordVOList(orderId);
        return R.ok(list);
    }

    @Override
    public ApplyProcessRecord getDistribution(Integer orderId) {
        return baseMapper.getDistribution(orderId);
    }

    @Override
    public String getByNodeType(int orderId,int nodeType) {
        return baseMapper.getByNodeType(orderId,nodeType);
    }

    @Override
    public String getNickNameByOPType(int orderId, int operationType) {
        return baseMapper.getNickNameByOPType(orderId,operationType);
    }

}
