package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.ApplyCarProcess;
import com.htstar.ovms.enterprise.api.entity.ApplyVerifyNode;
import com.htstar.ovms.enterprise.api.req.CarProcessReq;
import com.htstar.ovms.enterprise.api.req.SetNodeVerifyUserReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCarProcessVO;
import com.htstar.ovms.enterprise.api.vo.NodeVerifyUserVO;

import java.util.List;

/**
 * 公车申请流程
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
public interface ApplyCarProcessService extends IService<ApplyCarProcess> {

    /**
     * 用车申请流程配置
     * @param req
     * @return
     */
    R configOfficeCarProcess(CarProcessReq req);

    /**
     * Description: 得到企业当前运行的公车申请流程配置
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    ApplyCarProcess getNowRunOfficeProcess(Integer etpId);

    /**
     * Description: 得到企业当前运行的私车公用申请流程配置
     * Author: flr
     * Date: 2020/7/4 17:34
     * Company: 航通星空
     * Modified By:
     */
    ApplyCarProcess getNowRunPrivateProcess(Integer etpId);

    /**
     * Description: 给企业添加一个默认公车申请配置
     * Author: flr
     * Date: 2020/7/1 14:43
     * Company: 航通星空
     * Modified By:
     */
    ApplyCarProcess createDefualtOfficeCarProcess(Integer etpId);

    /**
     * Description: 获取前一步的节点
     * Author: flr
     * Date: 2020/7/4 9:35
     * Company: 航通星空
     * Modified By:
     */
    ApplyVerifyNode getBeforeNode(Integer processId, Integer nodeType);
    /**
     * Description: 得到流程的节点列表（有序）
     * Author: flr
     * Date: 2020/7/3 11:05
     * Company: 航通星空
     * Modified By:
     */
    List<ApplyVerifyNode> getVerifyNodeList(Integer processId);

    /**
     * Description: 详情
     * Author: flr
     * Date: 2020/7/7 14:49
     * Company: 航通星空
     * Modified By:
     */
    R<ApplyCarProcessVO> getVoById(Integer id);

    /**
     * Description: 企业获取当前流程
     * Author: flr
     * Date: 2020/7/9 13:48
     * Company: 航通星空
     * Modified By:
     */
    R<ApplyCarProcessVO> getEtpNowProcess(Integer processType);

    /**
     * Description:获取节点的审批人员
     * Author: flr
     * Date: 2020/7/9 15:17
     * Company: 航通星空
     * Modified By:
     */
    R<NodeVerifyUserVO> getNodeVerifyUser(Integer nodeId);

    /**
     * Description: 设置节点的审批人员
     * Author: flr
     * Date: 2020/7/11 9:31
     * Company: 航通星空
     * Modified By:
     */
    R setNodeVerifyUser(SetNodeVerifyUserReq req);


    ApplyVerifyNode getNextNode(int applyType, int driveType, int processId, int nodeType);
}
