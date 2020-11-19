package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.enterprise.api.entity.ApplyProcessRecord;
import com.htstar.ovms.enterprise.api.vo.DetailApplyCarRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程记录
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
@Mapper
public interface ApplyProcessRecordMapper extends BaseMapper<ApplyProcessRecord> {

    List<DetailApplyCarRecordVO> queryDetailApplyCarRecordVOList(@Param("orderId") Integer orderId);

    ApplyProcessRecord getDistribution(@Param("orderId") Integer orderId);

    String getByNodeType(@Param("orderId")int orderId ,@Param("nodeType") int nodeType);

    String getNickNameByOPType(@Param("orderId") int orderId, @Param("operationType") int operationType);
}
