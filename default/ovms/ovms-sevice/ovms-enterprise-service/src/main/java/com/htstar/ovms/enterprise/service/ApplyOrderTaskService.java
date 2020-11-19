package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.enterprise.api.entity.ApplyCarOrder;
import com.htstar.ovms.enterprise.api.entity.ApplyOrderTask;

import java.util.List;

/**
 * 审批任务
 *
 * @author flr
 * @date 2020-07-01 09:25:22
 */
public interface ApplyOrderTaskService extends IService<ApplyOrderTask> {

    /**
     * Description: 清除所有任务
     * Author: flr
     * Date: 2020/8/3 14:07
     * Company: 航通星空
     * Modified By:
     */
    void cleanAllTask(Integer orderId);

    void createTask(ApplyCarOrder order);

    void createCCTask(Integer orderId, String ccUserList);

    ApplyOrderTask getTask(Integer userId, Integer orderId, int taskType);

    /**
     * Description: 节点有人已处理
     * Author: flr
     * Date: 2020/8/3 15:11
     * Company: 航通星空
     * Modified By:
     */
    void dealHaveDo(ApplyCarOrder order, Integer userId);

    void overTask(Integer orderId);

    List<Integer> getVerifyIdList(Integer orderId);
}
