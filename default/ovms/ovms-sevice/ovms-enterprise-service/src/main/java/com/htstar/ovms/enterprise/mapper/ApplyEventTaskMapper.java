package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.enterprise.api.entity.ApplyOrderTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审批任务
 *
 * @author flr
 * @date 2020-07-01 09:25:22
 */
@Mapper
public interface ApplyEventTaskMapper extends BaseMapper<ApplyOrderTask> {

    /**
     * Description: 清除所有任务
     * Author: flr
     * Date: 2020/8/3 14:07
     * Company: 航通星空
     * Modified By:
     */
    void cleanAllTask(Integer orderId);

    //修改我的为已处理
    void updateByParams(@Param("userId") Integer userId, @Param("orderId") Integer orderId, @Param("processStatus") Integer processStatus,@Param("taskType")int taskType);

    //删除其他人的审批任务
    void delByParams(@Param("userId") Integer userId, @Param("orderId") Integer orderId, @Param("processStatus") Integer processStatus);

    List<Integer> getAdminRoleId(@Param("etpId") Integer etpId);

    void overTask(@Param("orderId")Integer orderId);

    List<Integer> getVerifyIdList(@Param("orderId") Integer orderId);
}
