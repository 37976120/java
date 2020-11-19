package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.enterprise.api.entity.ApplyCarProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 公车申请流程
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
@Mapper
public interface ApplyOfficeCarProcessMapper extends BaseMapper<ApplyCarProcess> {

    /**
     * Description: 得到企业当前运行的流程配置
     * Author: flr
     * Date: 2020/7/1 14:32
     * Company: 航通星空
     * Modified By:
     */
    ApplyCarProcess getNowRunProcess(@Param("etpId") Integer etpId, @Param("processType") int processType);

}
