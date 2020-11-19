package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.*;
import com.htstar.ovms.enterprise.api.entity.*;
import com.htstar.ovms.enterprise.api.req.*;
import com.htstar.ovms.enterprise.api.vo.*;
import com.htstar.ovms.enterprise.mapper.ApplyOfficeCarEventMapper;
import com.htstar.ovms.enterprise.msg.template.UseCarTemlateEnum;
import com.htstar.ovms.enterprise.service.*;
import com.htstar.ovms.msg.api.constant.MsgTypeConstant;
import com.htstar.ovms.msg.api.feign.MsgAppPushFeign;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;
import com.htstar.ovms.report.api.entity.ReportExpense;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * 公车申请事件
 *
 * @author flr
 * @date 2020-06-30 18:24:20
 */
@Slf4j
@Service
public class ApplyCarOrderServiceImpl extends ServiceImpl<ApplyOfficeCarEventMapper, ApplyCarOrder> implements ApplyCarOrderService {
    @Autowired
    private ApplyCarOrderDetailService detailService;
    @Autowired
    private ApplyCarProcessService processService;
    @Autowired
    private ApplyProcessRecordService recordService;
    @Autowired
    private ApplyOrderTaskService taskService;
    @Autowired
    private ApplyVerifyNodeService nodeService;
    @Autowired
    private CarDriverInfoService driverInfoService;
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private CarEtcItemService itemService;

    @Autowired
    private MsgAppPushFeign msgAppPushFeign;


    /**
     * Description: 提交公车申请
     * Author: flr
     * Company: 航通星空
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R commitOfficeCarApply(ApplyCarOrderReq req) {
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        Integer applyType = req.getApplyType();
        if (null == applyType) {
            return R.failed("请选择申请类型！！");
        }
        //1.得到企业当前运行的流程配置(如果没有配置，则新增一个默认配置)
        ApplyCarProcess process;
        switch (applyType.intValue()) {
            //申请类型:0=公车申请；1=私车公用；2=直接派车；
            case ProcessTypeConstant.PUBLIC_APPLY:
                process = processService.getNowRunOfficeProcess(etpId);
                break;
            case ProcessTypeConstant.PRIVATE_APPLY:
                process = processService.getNowRunPrivateProcess(etpId);
                break;
            case ProcessTypeConstant.DIRECT_PASS:
                if (!SecurityUtils.getRoleCode(user).contains(CommonConstants.ROLE_ADMIN)) {
                    R.failed("只有企业管理员可以直接派车！");
                }
                //直接派车，不存在申请，这里只记录
                ApplyCarOrder order = new ApplyCarOrder();
                BeanUtil.copyProperties(req, order);
                order.setEtpId(etpId);
                order.setApplyUserId(user.getId());
                order.setProcessStatus(ProcessStatusEnum.FINISH.getCode());
                order.setNowNodeType(ProcessNodeTypeConstant.FINISH);
                order.setNextNodeType(ProcessNodeTypeConstant.FINISH);
                this.save(order);
                ApplyCarOrderDetail detail = new ApplyCarOrderDetail();
                BeanUtil.copyProperties(req, detail);
                //转换一些前端传入的信息为JSON text
                /**
                 * 外部乘车人列表[{name:'姓名(必填)',mobile:'手机号'}]
                 */
                List<PassengerVO> outPassengerList = req.getOutPassengerList();
                detail.setOutPassengerList(JSONUtil.toJsonStr(outPassengerList));
                /**
                 * 照片列表[{photoUrl:'图片路径'}]
                 */
                List<PhotoVO> photoList = req.getPhotoList();
                detail.setPhotoList(JSONUtil.toJsonStr(photoList));
                if (order.getOrderId() == null) {
                    throw new RuntimeException("插入节点时未获取到自增主键");
                }
                detail.setOrderId(order.getOrderId());
                detailService.save(detail);
                //根据当前节点类型，生成对应的操作记录
                ApplyProcessRecord record = new ApplyProcessRecord();
                record.setOrderId(order.getOrderId());
                record.setOperationUserId(user.getId());
                record.setOperationType(ProcessOperationTypeConstant.AGREE);
                record.setRemark("直接派车");
                recordService.save(record);

                ApplyCarOrder updateOrder = new ApplyCarOrder();
                updateOrder.setOrderId(order.getOrderId());
                updateOrder.setLastRecordId(record.getId());
                this.updateById(updateOrder);


                ApplyOrderTask task = new ApplyOrderTask();
                task.setReadFalg(1);
                task.setTaskType(ProcessTaskTypeConstant.I_START);
                task.setVerifyUserId(user.getId());
                task.setOrderId(order.getOrderId());
                task.setProcessStatus(0);
                taskService.save(task);
                return R.ok();
            default:
                return R.failed("不支持的申请类型：" + applyType);
        }

        if (process == null) {
            return R.failed("系统错误：未找到对应流程配置，请联系管理员！");
        }
        ApplyCarOrder order = new ApplyCarOrder();
        BeanUtil.copyProperties(req, order);
        order.setProcessId(process.getId());
        order.setEtpId(etpId);
        order.setApplyUserId(user.getId());
        order.setNowNodeType(ProcessNodeTypeConstant.APPLY);
        //拿到当前需要审批的人员ID(并处理通知)--->首次审批的人员可以指定的
        ApplyVerifyNode nextNode = processService.getNextNode(
                order.getApplyType(),
                order.getDriveType(),
                process.getId(),
                ProcessNodeTypeConstant.APPLY
        );
        order.setNextNodeType(nextNode.getNodeType());
        this.save(order);
        ApplyCarOrderDetail detail = new ApplyCarOrderDetail();
        BeanUtil.copyProperties(req, detail);
        //转换一些前端传入的信息为JSON text
        /**
         * 外部乘车人列表[{name:'姓名(必填)',mobile:'手机号'}]
         */
        List<PassengerVO> outPassengerList = req.getOutPassengerList();
        detail.setOutPassengerList(JSONUtil.toJsonStr(outPassengerList));
        /**
         * 照片列表[{photoUrl:'图片路径'}]
         */
        List<PhotoVO> photoList = req.getPhotoList();
        detail.setPhotoList(JSONUtil.toJsonStr(photoList));
        if (order.getOrderId() == null) {
            throw new RuntimeException("插入节点时未获取到自增主键");
        }
        detail.setOrderId(order.getOrderId());
        detailService.save(detail);
        //根据当前节点类型，生成对应的操作记录
        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setOrderId(order.getOrderId());
        record.setOperationUserId(user.getId());
        record.setOperationType(ProcessOperationTypeConstant.AGREE);
        record.setNodeId(nodeService.getNodeId(order.getProcessId(), ProcessNodeTypeConstant.APPLY));
        record.setRemark(user.getUsername() + "提交公车申请");
        recordService.save(record);
        //5.更新最新的操作记录
        ApplyCarOrder updateOrder = new ApplyCarOrder();
        Integer processStatus = null;
        //生成流程状态
        switch (nextNode.getNodeType()){
            case ProcessNodeTypeConstant.VERIFY:
                processStatus = ProcessStatusEnum.WAIT_VERIFY.getCode();
                break;
            case ProcessNodeTypeConstant.GIVE_CAR:
                processStatus = ProcessStatusEnum.WAIT_DISTRIBUTION.getCode();
                break;
            case ProcessNodeTypeConstant.START_USE:
                processStatus = ProcessStatusEnum.WAIT_STA.getCode();
                break;
            case ProcessNodeTypeConstant.GET_CAR:
                processStatus = ProcessStatusEnum.WAIT_GETCAR.getCode();
                break;
            case ProcessNodeTypeConstant.RETURN_CAR:
                processStatus = ProcessStatusEnum.WAIT_RETURNCAR.getCode();
                break;
            case ProcessNodeTypeConstant.FINISH:
                processStatus = ProcessStatusEnum.WAIT_RECIVE.getCode();
                break;

        }
        if (null != processStatus){
            updateOrder.setProcessStatus(processStatus);
        }

        updateOrder.setOrderId(order.getOrderId());
        updateOrder.setLastRecordId(record.getId());
        this.updateById(updateOrder);

        ApplyCarOrder taskOrder = new ApplyCarOrder();
        taskOrder.setProcessId(order.getProcessId());
        taskOrder.setProcessStatus(updateOrder.getProcessStatus());
        taskOrder.setDriveType(order.getDriveType());
        taskOrder.setNextNodeType(order.getNextNodeType());
        taskOrder.setVerifyUserList(order.getVerifyUserList());
        taskOrder.setEtpId(order.getEtpId());
        taskOrder.setOrderId(order.getOrderId());
        taskOrder.setApplyUserId(order.getApplyUserId());
        taskService.createTask(taskOrder);
        ApplyOrderTask task = new ApplyOrderTask();
        task.setReadFalg(1);
        task.setTaskType(ProcessTaskTypeConstant.I_START);
        task.setVerifyUserId(user.getId());
        task.setOrderId(order.getOrderId());
        task.setProcessStatus(0);
        taskService.save(task);
        return R.ok();
    }

    /**
     * Description: 处理公车申请
     * Author: flr
     * Date: 2020/7/2 16:05
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R handleOfficeCarApply(HandleOfficeCarApplyReq req) {
        //TODO 分布式锁
        //操作类型：1=同意；2=拒绝；3=退回；4=修改；5=作废；6=撤回
        Integer operationType = req.getOperationType();
        if (null == operationType || operationType.intValue() == 0) {
            return R.failed("请选择操作类型！");
        }
        switch (operationType) {
            case ProcessOperationTypeConstant.AGREE:
                return agreeOfficeCarApply(req);
            case ProcessOperationTypeConstant.REFUSE:
                return refuseOfficeCarApply(req);
            case ProcessOperationTypeConstant.BACK:
                return backOfficeCarApply(req);
            case ProcessOperationTypeConstant.UPDATE:
                return updateOfficeCarApply(req);
            case ProcessOperationTypeConstant.ABAND:
                return abandOfficeCarApply(req);
            case ProcessOperationTypeConstant.GO_START:
                return goStartOfficeCarApply(req);
        }
        return R.ok();
    }

    /**
     * Description:撤回--》撤回(申请人有权限，刚申请的时候，这一单的状态挂起，等待重新提交)
     * Author: flr
     * Date: 2020/7/9 18:56
     * Company: 航通星空
     * Modified By:
     */
    private R goStartOfficeCarApply(HandleOfficeCarApplyReq req) {
        OvmsUser user = SecurityUtils.getUser();
        ApplyCarOrder order = this.getById(req.getOrderId());
        ApplyCarOrderDetail detail = detailService.getById(req.getOrderId());
        if (null == detail) {
            return R.failed("申请不存在，eventId=" + req.getOrderId());
        }

        if (user.getId().intValue() != order.getApplyUserId().intValue()){
            return R.failed("只有申请人才可以撤回！");
        }

        //具备了审批的权利，coming
        ApplyCarOrder updateOrder = new ApplyCarOrder();
        updateOrder.setOrderId(order.getOrderId());

        //2.生成操作记录
        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setRemark(req.getRemark());
        record.setOrderId(order.getOrderId());
        record.setOperationUserId(user.getId());
        record.setOperationType(ProcessOperationTypeConstant.GO_START);
        recordService.save(record);
        updateOrder.setLastRecordId(record.getId());
        updateOrder.setNowNodeType(ProcessNodeTypeConstant.APPLY);
        ApplyVerifyNode afterNode = processService.getNextNode(order.getApplyType(),
                order.getDriveType(),order.getProcessId(), ProcessNodeTypeConstant.APPLY);
        updateOrder.setNextNodeType(afterNode.getNodeType());
        updateOrder.setProcessStatus(ProcessStatusEnum.GO_START.getCode());
        this.updateById(updateOrder);
        //3.删除所有的任务
        taskService.cleanAllTask(order.getOrderId());
        return R.ok();
    }


    /**
     * Description: 分页
     * Author: flr
     * Date: 2020/7/8 14:44
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R<Page<ApplyOrderPageVO>> getPage(PageApplyOrderReq req) {
        OvmsUser user = SecurityUtils.getUser();
        req.setEtpId(user.getEtpId());
        if (StrUtil.isBlank(req.getTaskType())){
            req.setTaskType(null);
        }
        Page<ApplyOrderPageVO> page;
        //类型：1=待我审批；2=已审批；3=抄送我的；4=我发起的；
        if (null == req.getUserId()){
            req.setUserId(user.getId());
        }
        page = baseMapper.getPageTask(req);
        List<ApplyOrderPageVO> records = page.getRecords();
        if (records != null && !records.isEmpty()) {
            for (ApplyOrderPageVO vo : records) {
                int processStatus = vo.getProcessStatus();
                switch (processStatus){
                    case ProcessStatusConstant.WAIT_VERIFY:
                        vo.setAgreeIf(1);
                        vo.setRefuseIf(1);
                        vo.setUpdateIf(1);
                        if (vo.getApplyUserId().intValue() == user.getId().intValue()){
                            vo.setGoStartIf(1);
                        }
                        break;
                    case ProcessStatusConstant.VERIFY_REFUSE:
                        break;
                    case ProcessStatusConstant.WAIT_DISTRIBUTION:
                        vo.setAgreeIf(1);
                        break;
                    case ProcessStatusConstant.WAIT_GIVECAR:
                        vo.setAgreeIf(1);
                        break;
                    case ProcessStatusConstant.GIVECAR_REFUSE:
                        break;
                    case ProcessStatusConstant.WAIT_GETCAR:
                        if(vo.getDriveType() == ProcessOrderDriveTypeEnum.DRIVER.getCode()){//司机
                            ApplyCarProcess process = processService.getById(vo.getProcessId());

//                            //提车还车时需录入里程:0=否；1=是；")
//                            int mileageStatus;
//                            if (null != vo.getMileageStatus()){
//                                mileageStatus= vo.getMileageStatus();
//                            }


                            if (process.getDriverGetCarStatus() == 1 && vo.getDriverId().intValue() == user.getId().intValue()){
                                vo.setGetCarIf(1);
                            }else if (process.getDriverGetCarStatus() == 0 && vo.getDriverId().intValue() == user.getId().intValue()){
                                vo.setGetCarIf(1);
                            }else if (process.getDriverGetCarStatus() == 0 && vo.getApplyUserId().intValue() == user.getId().intValue()){
                                vo.setGetCarIf(1);
                            }
                        }else {
                            if(user.getId().intValue() == vo.getApplyUserId().intValue()){
                                vo.setGetCarIf(1);
                            }
                        }
                        break;
                    case ProcessStatusConstant.GETCAR_REFUSE:
                        break;
                    case ProcessStatusConstant.WAIT_RETURNCAR:
                        vo.setRetuenCarIf(1);
                        vo.setAgreeIf(1);
                        break;
                    case ProcessStatusConstant.WAIT_STA:
                        vo.setAgreeIf(1);
                        break;
                    case ProcessStatusConstant.WAIT_END:
                        vo.setEndCarIf(1);
                        vo.setAgreeIf(1);
                        break;
                    case ProcessStatusConstant.WAIT_RECIVE:
                        vo.setBackIf(1);
                        vo.setAgreeIf(1);
                        break;
                    case ProcessStatusConstant.GO_START:
                        vo.setGoStartIf(0);
                        break;
                    case ProcessStatusConstant.RECIVE_BACK:
                        if (vo.getTaskType().intValue() == 1){
                            if (vo.getApplyType().intValue() == 1 ){
                                //提车还车时需录入里程:0=否；1=是；
                                if (vo.getMileageStatus().intValue() == 0){
                                    vo.setEndCarIf(1);
                                }else if (vo.getMileageStatus().intValue() == 1 && vo.getRetuenCarStatus().intValue() == 1){
                                    vo.setEndCarIf(1);
                                }
                            }else if (vo.getTaskType().intValue() == 0){
                                //提车还车时需录入里程:0=否；1=是；
                                if (vo.getMileageStatus().intValue() == 0){
                                    vo.setRetuenCarIf(1);
                                }else if (vo.getMileageStatus().intValue() == 1 && vo.getRetuenCarStatus().intValue() == 1){
                                    vo.setRetuenCarIf(1);
                                }
                            }

                        }
                        vo.setAgreeIf(1);
                        break;
                    case ProcessStatusConstant.FINISH:
                        break;
                }
                if (vo.getDriverId() > 0){
                    vo.setDriverName(baseMapper.getUserNinckName(vo.getDriverId()));
                }
            }
        }
        if (page == null) {
            page = new Page<>();
        }
        return R.ok(page);
    }

    /**
     * Description: 详情
     * Author: flr
     * Date: 2020/7/7 16:26
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R<ApplyCarOrderVO> getVOById(Integer id) {
        ApplyCarOrderVO vo = new ApplyCarOrderVO();
        ApplyCarOrder order = this.getById(id);
        ApplyCarOrderDetail detail = detailService.getById(id);
        BeanUtil.copyProperties(order, vo);
        BeanUtil.copyProperties(detail, vo);
        OvmsUser user = SecurityUtils.getUser();
        //处理已读状态
        ApplyOrderTask task = taskService.getTask(user.getId(), id, ProcessTaskTypeConstant.CC_ME);
        if (null != task && task.getReadFalg().intValue() == 0) {
            task.setReadFalg(1);
            task.setReadTime(OvmDateUtil.getCstNow());
            taskService.updateById(task);
        }

        //填充一些详情数据----
        //申请人姓名
        DriverVO applyUser = driverInfoService.getDriverVO(order.getApplyUserId());
        vo.setApplyUserNickName(applyUser.getNickName());
        vo.setApplyUserPhone(applyUser.getUsername());

        // * 驾车人
        Integer driveUserId = detail.getDriveUserId();
        if (driveUserId != null && driveUserId.intValue() > 0) {
            DriverVO driverVO = driverInfoService.getDriverVO(driveUserId);
            vo.setDriverVO(driverVO);
        }
        // * [审批人员ID列表(有序)]
        if (StrUtil.isNotBlank(order.getVerifyUserList())) {
            List<VerifyUserVO> verifyUserVOS = nodeService.queryVerifyUserVOByStr(order.getVerifyUserList());
            vo.setVerifyUserVOS(verifyUserVOS);
        }
        // * [抄送人员ID列表(有序)]
        if (StrUtil.isNotBlank(detail.getCcUserList())) {
            List<VerifyUserVO> verifyUserVOS = nodeService.queryVerifyUserVOByStr(detail.getCcUserList());
            vo.setCcUserListVOS(verifyUserVOS);
        }

        //[内部乘车人员ID列表]
        if (StrUtil.isNotBlank(detail.getInnerPassengerList())) {
            List<VerifyUserVO> verifyUserVOS = nodeService.queryVerifyUserVOByStr(detail.getInnerPassengerList());
            vo.setInnerPassengerListVOS(verifyUserVOS);
        }

        // * 车辆id（分配后有大于0的记录，未分配默认0）
        Integer carId = order.getCarId();
        if (carId != null && carId.intValue() > 0) {
            CarInfo carInfo = carInfoService.getById(carId);
            vo.setCarInfo(carInfo);
        }

        // * 司机id（分配后有大于0的记录，未分配默认0）
        Integer driverId = order.getDriverId();
        if (driverId != null && driverId.intValue() > 0) {
            DriverVO driver = driverInfoService.getDriverVO(driverId);
            vo.setDriver(driver);
        }



        //用车审批人姓名")
        String useVerifyName = recordService.getByNodeType(id,ProcessNodeTypeConstant.VERIFY);
        if (StrUtil.isNotBlank(useVerifyName)){
            vo.setUseVerifyName(useVerifyName);
        }

        //回车审批人姓名")
        String receiveVerrifyName = recordService.getByNodeType(id,ProcessNodeTypeConstant.FINISH);
        if (StrUtil.isNotBlank(receiveVerrifyName)){
            vo.setReceiveVerrifyName(receiveVerrifyName);
        }

        //回车审批人姓名")
        String giveCarUserName = recordService.getNickNameByOPType(id,ProcessOperationTypeConstant.DISTRIBUTION_CAR);
        if (StrUtil.isNotBlank(giveCarUserName)){
            vo.setGiveCarUserName(giveCarUserName);
        }


        if (order.getLastRecordId() != null){
            ApplyProcessRecord record = recordService.getById(order.getLastRecordId());
            vo.setRecord(record);
        }

        //分配时间
        ApplyProcessRecord record = recordService.getDistribution(order.getOrderId());
        if (record != null){
            vo.setDistributionTime(record.getCreateTime());
        }
        return R.ok(vo);
    }

    /**
     * Description: 分配车辆
     * Author: flr
     * Date: 2020/7/8 11:00
     * Company: 航通星空
     * Modified By:
     */
    @Override
    @Transactional
    public R distributionCar(DistributionCarReq req) {
        OvmsUser user = SecurityUtils.getUser();
        ApplyCarOrder order = this.getById(req.getOrderId());
        if (null == order) {
            return R.failed("申请不存在");
        }
        ApplyVerifyNode node = nodeService.getNode(order.getProcessId(), order.getNextNodeType());
        //查找交车节点的审批人
        List<Integer> verifyUserIdList = nodeService.queryVerifyUserId(order);
        if (!verifyUserIdList.contains(user.getId())) {
            return R.failed("无权限操作！");
        }
        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setOperationType(ProcessOperationTypeConstant.DISTRIBUTION_CAR);
        record.setRemark(user.getUsername() + "分配车辆");
        record.setOperationUserId(user.getId());
        record.setOrderId(req.getOrderId());
        record.setNodeId(node.getId());
        recordService.save(record);
        ApplyCarOrder upEnt = new ApplyCarOrder();
        upEnt.setOrderId(req.getOrderId());
        upEnt.setCarId(req.getCarId());
        upEnt.setLastRecordId(record.getId());
        this.updateById(upEnt);
        return R.ok();
    }

    /**
     * Description: 分配司机
     * Author: flr
     * Date: 2020/7/8 12:15
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R distributionDriver(DistributionDriverReq req) {
        OvmsUser user = SecurityUtils.getUser();
        ApplyCarOrder order = this.getById(req.getOrderId());
        if (null == order) {
            return R.failed("申请不存在");
        }
        ApplyVerifyNode node = nodeService.getNode(order.getProcessId(), order.getNextNodeType());
        //查找交车节点的审批人
        List<Integer> verifyUserIdList = nodeService.queryVerifyUserId(order);
        if (!verifyUserIdList.contains(user.getId())) {
            return R.failed("无权限操作！");
        }
        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setOperationType(ProcessOperationTypeConstant.DISTRIBUTION_DRIVER);
        record.setRemark(user.getUsername() + "分配司机");
        record.setOperationUserId(user.getId());
        record.setOrderId(req.getOrderId());
        record.setNodeId(node.getId());
        recordService.save(record);
        ApplyCarOrder upEnt = new ApplyCarOrder();
        upEnt.setOrderId(req.getOrderId());
        upEnt.setDriverId(req.getDriverId());
        upEnt.setLastRecordId(record.getId());
        this.updateById(upEnt);
        return R.ok();
    }

    /**
     * Description: 提车上报
     * Author: flr
     * Date: 2020/7/8 12:22
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R saveGiveCarData(SaveGiveCarDataReq req) {
        OvmsUser user = SecurityUtils.getUser();
        ApplyCarOrder order = this.getById(req.getOrderId());
        if (null == order) {
            return R.failed("申请不存在");
        }
        ApplyVerifyNode node = nodeService.getNode(order.getProcessId(), order.getNextNodeType());
        //查找交车节点的审批人
        List<Integer> verifyUserList = nodeService.queryVerifyUserId(order);
        if (!verifyUserList.contains(user.getId())) {
            return R.failed("无权限操作！");
        }

        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setOperationType(ProcessOperationTypeConstant.GIVE_CAR_DATA);
        record.setRemark(user.getUsername() + "提车上报");
        record.setOperationUserId(user.getId());
        record.setOrderId(req.getOrderId());
        record.setNodeId(node.getId());
        recordService.save(record);
        ApplyCarOrder upEnt = new ApplyCarOrder();
        upEnt.setOrderId(req.getOrderId());
        upEnt.setLastRecordId(record.getId());

        ApplyCarOrderDetail detail = new ApplyCarOrderDetail();
        detail.setOrderId(req.getOrderId());
        BeanUtil.copyProperties(req, detail);

        detail.setGiveCarStatus(1);
        detailService.updateById(detail);
        this.updateById(upEnt);
        return R.ok();
    }

    /**
     * Description: 还车上报
     * Author: flr
     * Date: 2020/7/8 14:27
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R saveReturnCarData(SaveReturnCarDataReq req) {
        OvmsUser user = SecurityUtils.getUser();
        ApplyCarOrder order = this.getById(req.getOrderId());
        if (null == order) {
            return R.failed("申请不存在");
        }
        ApplyVerifyNode node = nodeService.getNode(order.getProcessId(), order.getNextNodeType());
        //查找交车节点的审批人
        if (order.getProcessStatus().intValue() != ProcessStatusEnum.RECIVE_BACK.getCode()){
            List<Integer> verifyUserIdList = nodeService.queryVerifyUserId(order);
            if (!verifyUserIdList.contains(user.getId())) {
                return R.failed("无权限操作！");
            }
        }

        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setOperationType(ProcessOperationTypeConstant.RETURN_CAR_DATA);
        record.setRemark(user.getUsername() + "还车上报");
        record.setOperationUserId(user.getId());
        record.setOrderId(req.getOrderId());
        record.setNodeId(node.getId());
        recordService.save(record);
        ApplyCarOrder upEnt = new ApplyCarOrder();
        upEnt.setOrderId(req.getOrderId());
        upEnt.setLastRecordId(record.getId());

        if (order.getProcessStatus().intValue() == ProcessStatusEnum.RECIVE_BACK.getCode()){
            //相当于重新提交
//            upEnt.setProcessStatus(ProcessStatusEnum.WAIT_RECIVE.getCode());
            if (order.getApplyType() == ProcessTypeConstant.PUBLIC_APPLY){
                upEnt.setNowNodeType(ProcessNodeTypeConstant.RETURN_CAR);
            }else if (order.getApplyType() == ProcessTypeConstant.PRIVATE_APPLY){
                upEnt.setNowNodeType(ProcessNodeTypeConstant.END_USE);
            }
//            upEnt.setNextNodeType(ProcessNodeTypeConstant.FINISH);
        }


        ApplyCarOrderDetail detail = new ApplyCarOrderDetail();
        detail.setOrderId(req.getOrderId());
        BeanUtil.copyProperties(req, detail);
        detail.setRetuenCarStatus(1);
        detailService.updateById(detail);
        this.updateById(upEnt);
        return R.ok();
    }

    /**
     * Description: 作废公车申请
     * Author: flr
     * Date: 2020/7/4 15:14
     * Company: 航通星空
     * Modified By:
     */
    private R abandOfficeCarApply(HandleOfficeCarApplyReq req) {
        ApplyCarOrderReq upmodel = req.getUpdateModel();
        if (null == upmodel) {
            return R.failed("请求参数错误！");
        }
        OvmsUser user = SecurityUtils.getUser();
        ApplyCarOrder order = this.getById(req.getOrderId());
        ApplyCarOrderDetail detail = detailService.getById(req.getOrderId());
        if (null == detail) {
            return R.failed("申请不存在，eventId=" + req.getOrderId());
        }
        //2.查看是否指定审批用户（不管怎样，指定还是什么的都在里面的）
        List<Integer> verifyUserList = nodeService.queryVerifyUserId(order);
        if (!verifyUserList.contains(user.getId())) {
            return R.failed("该申请的审批人不是你");
        }

        //判断该节点是否指定了可以撤回==（作废）
        ApplyVerifyNode nextNode = processService.getNextNode(order.getApplyType(),order.getDriveType(),order.getProcessId(),order.getNextNodeType());
        if (null == nextNode) {
            return R.failed("无法操作该(已完成)申请！");
        }
        if (nextNode.getInvalidStatus() == 0) {
            return R.failed("该流程的节点配置了不可作废！");
        }

        //具备了审批的权利，coming
        ApplyCarOrder updateOrder = new ApplyCarOrder();
        updateOrder.setOrderId(order.getOrderId());
        //2.生成操作记录
        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setRemark(req.getRemark());
        record.setOrderId(order.getOrderId());
        record.setOperationUserId(user.getId());
        record.setOperationType(ProcessOperationTypeConstant.ABAND);
        recordService.save(record);
        updateOrder.setLastRecordId(record.getId());
        this.updateById(updateOrder);
        //3.删除所有的任务
        taskService.cleanAllTask(order.getOrderId());
        //TODO 通知申请人
        return R.ok();
    }

    /**
     * Description: 修改公车申请
     * Author: flr
     * Date: 2020/7/4 14:52
     * Company: 航通星空
     * Modified By:
     */
    private R updateOfficeCarApply(HandleOfficeCarApplyReq req) {
        ApplyCarOrderReq upmodel = req.getUpdateModel();
        if (null == upmodel) {
            return R.failed("请求参数错误！");
        }
        OvmsUser user = SecurityUtils.getUser();
        ApplyCarOrder order = this.getById(req.getOrderId());
        ApplyCarOrderDetail detail = detailService.getById(req.getOrderId());
        if (null == detail) {
            return R.failed("申请不存在，eventId=" + req.getOrderId());
        }
        boolean gostart = false;
        if (order.getProcessStatus().intValue() == ProcessStatusConstant.GO_START
                && user.getId().intValue()  == order.getApplyUserId().intValue()){
            gostart = true;
        }
        //2.查看是否指定审批用户（不管怎样，指定还是什么的都在里面的）
        if (!gostart){
            List<Integer> verifyUserList = nodeService.queryVerifyUserId(order);
            if (null == verifyUserList || verifyUserList.isEmpty()) {
                return R.failed("该申请已被审批");
            }
            if (!verifyUserList.contains(user.getId())) {
                return R.failed("该申请的审批人不是你");
            }
        }

        //具备了审批的权利，coming
        ApplyCarOrder updateOrder = new ApplyCarOrder();
        updateOrder.setOrderId(order.getOrderId());
        BeanUtil.copyProperties(req.getUpdateModel(),updateOrder);
        //* 用车级别（公用字典）：0=A级(紧凑型车)；1=B级（中型车）；2=C级（中大型车）；3=D级（大型车）；
        Integer carLevel = upmodel.getCarLevel();
        if (null != carLevel) {
            updateOrder.setCarLevel(carLevel);
        }
        // * 驾车方式:1=司机；2=自驾；
        Integer driveType = upmodel.getDriveType();
        if (null != driveType) {
            updateOrder.setDriveType(driveType);
        }
        // * 同行人数
        Integer passengers = upmodel.getPassengers();
        if (null != passengers) {
            updateOrder.setPassengers(passengers);
        }
        // * 用车开始时间
        LocalDateTime staTime = upmodel.getStaTime();
        if (null != staTime) {
            updateOrder.setStaTime(staTime);
        }
        // * 用车结束时间
        LocalDateTime endTime = upmodel.getEndTime();
        if (null != endTime) {
            updateOrder.setEndTime(endTime);
        }

        // * 用车原因（50字以内）
        String applyReason = upmodel.getApplyReason();
        if (StrUtil.isNotBlank(applyReason)) {
            updateOrder.setApplyReason(applyReason);
        }
        ApplyCarOrderDetail updetail = new ApplyCarOrderDetail();
        updetail.setOrderId(order.getOrderId());

        BeanUtil.copyProperties(req.getUpdateModel(),updetail);

        // * 出发地址（50字内）
        String staAddr = upmodel.getStaAddr();
        if (StrUtil.isNotBlank(staAddr)) {
            updetail.setStaAddr(staAddr);
        }
        // * 目的地址(列表','隔开)
        String endAddrList = upmodel.getEndAddrList();
        if (StrUtil.isNotBlank(endAddrList)) {
            updetail.setEndAddrList(endAddrList);
        }
        // * [内部乘车人员ID列表]
        String innerPassengerList = upmodel.getInnerPassengerList();
        if (StrUtil.isNotBlank(innerPassengerList)) {
            updetail.setInnerPassengerList(innerPassengerList);
        }
        // * 外部乘车人列表[{name:'姓名(必填)',mobile:'手机号'}]
        List<PassengerVO> outPassengerList = upmodel.getOutPassengerList();

        if (outPassengerList != null) {
            updetail.setOutPassengerList(JSONUtil.toJsonStr(outPassengerList));
        }
        // * 照片列表[{photoUrl:'图片路径'}]
        List<PhotoVO> photoList = upmodel.getPhotoList();
        if (photoList != null) {
            updetail.setPhotoList(JSONUtil.toJsonStr(photoList));
        }
        //2.生成操作记录
        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setRemark(req.getRemark());
        record.setOrderId(order.getOrderId());
        record.setOperationUserId(user.getId());
        record.setOperationType(ProcessOperationTypeConstant.UPDATE);
        recordService.save(record);
        if (gostart){
            ApplyVerifyNode nextNode = processService.getNextNode(order.getApplyType(),
                    order.getDriveType(),order.getProcessId(), ProcessNodeTypeConstant.APPLY);
            Integer processStatus = null;
            switch (nextNode.getNodeType()){
                case ProcessNodeTypeConstant.VERIFY:
                    processStatus = ProcessStatusEnum.WAIT_VERIFY.getCode();
                    break;
                case ProcessNodeTypeConstant.START_USE:
                    processStatus = ProcessStatusEnum.WAIT_STA.getCode();
                    break;
                case ProcessNodeTypeConstant.GIVE_CAR:
                    processStatus = ProcessStatusEnum.WAIT_DISTRIBUTION.getCode();
                    break;
                case ProcessNodeTypeConstant.GET_CAR:
                    processStatus = ProcessStatusEnum.WAIT_GETCAR.getCode();
                    break;
                case ProcessNodeTypeConstant.RETURN_CAR:
                    processStatus = ProcessStatusEnum.WAIT_RETURNCAR.getCode();
                    break;
                case ProcessNodeTypeConstant.FINISH:
                    processStatus = ProcessStatusEnum.WAIT_RECIVE.getCode();
                    break;

            }
            if (null != processStatus){
                updateOrder.setProcessStatus(processStatus);
            }

            ApplyCarOrder taskOrder = new ApplyCarOrder();
            taskOrder.setProcessId(order.getProcessId());
            taskOrder.setProcessStatus(updateOrder.getProcessStatus());
            if (updateOrder.getDriveType() != null){
                taskOrder.setDriveType(updateOrder.getDriveType());
            }else {
                taskOrder.setDriveType(order.getDriveType());
            }
            taskOrder.setNextNodeType(order.getNextNodeType());

            if (updateOrder.getVerifyUserList() != null){
                taskOrder.setVerifyUserList(updateOrder.getVerifyUserList());
            }else {
                taskOrder.setVerifyUserList(order.getVerifyUserList());
            }
            taskOrder.setEtpId(order.getEtpId());
            taskOrder.setOrderId(order.getOrderId());
            taskOrder.setApplyUserId(order.getApplyUserId());
            taskService.createTask(taskOrder);
        }
        updateOrder.setLastRecordId(record.getId());
        this.updateById(updateOrder);
        detailService.updateById(updetail);
        return R.ok();
    }

    /**
     * Description: 退回公车申请
     * 退回 退回到上一步，当前节点的操作人有权限，默认有
     * Author: flr
     * Date: 2020/7/3 16:12
     * Company: 航通星空
     * Modified By:
     */
    private R backOfficeCarApply(HandleOfficeCarApplyReq req) {
        OvmsUser user = SecurityUtils.getUser();
        ApplyCarOrder order = this.getById(req.getOrderId());
        ApplyCarOrderDetail detail = detailService.getById(req.getOrderId());
        if (null == detail) {
            return R.failed("申请不存在，eventId=" + req.getOrderId());
        }
        //2.查看是否指定审批用户（不管怎样，指定还是什么的都在里面的）
        List<Integer> verifyUserList = nodeService.queryVerifyUserId(order);
        if (null == verifyUserList || verifyUserList.isEmpty()) {
            return R.failed("该申请已被审批");
        }
        if (!verifyUserList.contains(user.getId())) {
            return R.failed("该申请的审批人不是你");
        }
        //现在处在状态
        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setRemark(req.getRemark());
        record.setOrderId(order.getOrderId());
        record.setOperationUserId(user.getId());


        ApplyVerifyNode nextNode = processService.getNextNode(
                order.getApplyType(),
                order.getDriveType(),
                order.getProcessId(),order.getNowNodeType());
        if (nextNode == null || nextNode.getNodeType().intValue() != ProcessNodeTypeConstant.FINISH){
            return R.failed("只有收车环节允许退回！");
        }


        ApplyVerifyNode node = processService.getBeforeNode(order.getProcessId(), order.getNowNodeType());
        if (node == null) {
            node = nodeService.getNode(order.getProcessId(), order.getNowNodeType());
        }


        //3.修改相关状态
        ApplyCarOrder upEnvent = new ApplyCarOrder();
        upEnvent.setOrderId(order.getOrderId());
        upEnvent.setNowNodeType(node.getNodeType());
        upEnvent.setNextNodeType(order.getNowNodeType());
        //4.生成操作记录
        record.setOperationType(ProcessOperationTypeConstant.BACK);
        record.setNodeId(node.getId());//BUG
        record.setOrderId(order.getOrderId());
        recordService.save(record);
        upEnvent.setLastRecordId(record.getId());
        upEnvent.setProcessStatus(ProcessStatusEnum.RECIVE_BACK.getCode());
        this.updateById(upEnvent);
        //1.已处理
        taskService.dealHaveDo(order,user.getId());
        //2.重新生成相关的任务

        ApplyCarOrder taskOrder = new ApplyCarOrder();
        taskOrder.setProcessId(order.getProcessId());
        taskOrder.setProcessStatus(upEnvent.getProcessStatus());
        if (upEnvent.getDriveType() != null){
            taskOrder.setDriveType(upEnvent.getDriveType());
        }else {
            taskOrder.setDriveType(order.getDriveType());
        }
        taskOrder.setNextNodeType(upEnvent.getNextNodeType());
        taskOrder.setDriverId(order.getDriverId());

        if (upEnvent.getVerifyUserList() != null){
            taskOrder.setVerifyUserList(upEnvent.getVerifyUserList());
        }else {
            taskOrder.setVerifyUserList(order.getVerifyUserList());
        }
        taskOrder.setEtpId(order.getEtpId());
        taskOrder.setOrderId(order.getOrderId());
        taskOrder.setApplyUserId(order.getApplyUserId());
        taskOrder.setNowNodeType(upEnvent.getNowNodeType());
        taskService.createTask(taskOrder);
        return R.ok();
    }

    /**
     * Description: 拒绝公车申请
     * Author: flr
     * Date: 2020/7/3 16:19
     * Company: 航通星空
     * Modified By:
     */
    @Transactional
    R refuseOfficeCarApply(HandleOfficeCarApplyReq req) {
        OvmsUser user = SecurityUtils.getUser();
        ApplyCarOrder order = this.getById(req.getOrderId());
        //2.查看是否指定审批用户（不管怎样，指定还是什么的都在里面的）
        List<Integer> verifyUserList = nodeService.queryVerifyUserId(order);
        if (null == verifyUserList || verifyUserList.isEmpty()) {
            return R.failed("该申请已被审批");
        }
        if (!verifyUserList.contains(user.getId())) {
            return R.failed("该申请的审批人不是你");
        }
        ApplyVerifyNode nextNode = processService.getNextNode(
                order.getApplyType(),
                order.getDriveType(),
                order.getProcessId(), order.getNextNodeType());
        if (nextNode == null) {
            return R.failed("错误的审批配置！");
        }

        ApplyCarOrder updateOrder = new ApplyCarOrder();
        updateOrder.setOrderId(order.getOrderId());
        Integer processStatus = null;

        //生成流程状态
        switch (nextNode.getNodeType()){
            case ProcessNodeTypeConstant.VERIFY:
                processStatus = ProcessStatusEnum.VERIFY_REFUSE.getCode();
                break;
            case ProcessNodeTypeConstant.START_USE:
                return R.failed("当前节点不允许拒绝!");
            case ProcessNodeTypeConstant.END_USE:
                return R.failed("当前节点不允许拒绝");
            case ProcessNodeTypeConstant.GIVE_CAR:
                processStatus = ProcessStatusEnum.GIVECAR_REFUSE.getCode();
                break;
            case ProcessNodeTypeConstant.GET_CAR:
                processStatus = ProcessStatusEnum.GETCAR_REFUSE.getCode();
                break;
            case ProcessNodeTypeConstant.RETURN_CAR:
                return R.failed("还车节点不允许拒绝！");
            case ProcessNodeTypeConstant.FINISH:
                return R.failed("当前节点不允许拒绝");

        }
        if (null != processStatus){
            updateOrder.setProcessStatus(processStatus);
        }

        //具备了审批的权利，coming
        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setOperationType(ProcessOperationTypeConstant.REFUSE);
        record.setRemark(req.getRemark());
        record.setOrderId(order.getOrderId());
        record.setOperationUserId(user.getId());
        //生成一个统一审批的记录
        record.setNodeId(nextNode.getId());
        recordService.save(record);
        updateOrder.setLastRecordId(record.getId());
        updateOrder.setNowNodeType(nextNode.getNodeType());
        this.updateById(updateOrder);
        //3.已处理
        taskService.dealHaveDo(order,user.getId());
        SingleAppPushReq msgReq = new SingleAppPushReq();
        msgReq.setMsgType(MsgTypeConstant.VEHICEL_NOTIFICATION);
        msgReq.setUserId(order.getApplyUserId());
        msgReq.setTitle(UseCarTemlateEnum.REFUSE.getTitle());
        msgReq.setContent(UseCarTemlateEnum.REFUSE.getContent());
        msgReq.setAppCarId(req.getOrderId());
        msgAppPushFeign.appSinglePush(msgReq, SecurityConstants.FROM_IN);
        return R.ok();
    }

    /**
     * Description: 同意公车申请
     * Author: flr
     * Date: 2020/7/2 16:19
     * Company: 航通星空
     * Modified By:
     */
    @Transactional
    R agreeOfficeCarApply(HandleOfficeCarApplyReq req) {
        OvmsUser user = SecurityUtils.getUser();
        ApplyCarOrder order = this.getById(req.getOrderId());
        ApplyCarOrderDetail detail = detailService.getById(req.getOrderId());
        if (null == detail) {
            return R.failed("申请不存在，eventId=" + req.getOrderId());
        }

        //2.查看是否指定审批用户（不管怎样，指定还是什么的都在里面的）
        List<Integer> verifyUserList = nodeService.queryVerifyUserId(order);
        if (null == verifyUserList || verifyUserList.isEmpty()) {
            return R.failed("该申请已被审批");
        }
        if (!verifyUserList.contains(user.getId())) {
            return R.failed("该申请的审批人不是你");
        }
        //具备了审批的权利，coming
        ApplyCarOrder updateOrder = new ApplyCarOrder();
        updateOrder.setOrderId(order.getOrderId());
        ApplyProcessRecord record = new ApplyProcessRecord();
        record.setOperationType(ProcessOperationTypeConstant.AGREE);
        record.setRemark(req.getRemark());
        record.setOrderId(order.getOrderId());
        record.setOperationUserId(user.getId());
        //1.在流程中找到下一个节点
        ApplyVerifyNode nextNode = processService.getNextNode(order.getApplyType(),
                order.getDriveType(),order.getProcessId(), order.getNowNodeType());
        if (nextNode == null) {
            return R.failed("错误的操作``");
        }


        if (order.getDriveType().intValue() == ProcessOrderDriveTypeEnum.DRIVER.getCode()
                && order.getProcessStatus().intValue() == ProcessStatusEnum.WAIT_DISTRIBUTION.getCode()
                && order.getApplyType() == ProcessTypeConstant.PRIVATE_APPLY
                && order.getDriverId().intValue() == 0){
            return R.failed("私车-司机需要分配司机");
        }

        if (nextNode.getNodeType() == ProcessNodeTypeConstant.GIVE_CAR
                && order.getApplyType() == ProcessTypeConstant.PUBLIC_APPLY) {
            if (order.getCarId() == null
                    || order.getCarId() <= 0) {
                return R.failed("请先分配车辆！");
            }
        }

        ApplyCarProcess process = processService.getById(order.getProcessId());

        if (nextNode.getNodeType() == ProcessNodeTypeConstant.GET_CAR) {
            if (null == detail.getGiveCarMileage() || detail.getGiveCarMileage().intValue() == 0) {
                if (process.getMileageStatus().intValue() == 1){
                    return R.failed("提车前要录入里程数据！");
                }
            }
        }
        if (nextNode.getNodeType() == ProcessNodeTypeConstant.RETURN_CAR) {
            if (null == detail.getGiveCarMileage() || detail.getGiveCarMileage().intValue() == 0){
                if (process.getMileageStatus().intValue() == 1){
                    return R.failed("还车前要录入里程数据！");
                }
            }
        }
        //2.生成一个统一审批的记录
        record.setNodeId(nextNode.getId());
        //5.修改该事件
        updateOrder.setNowNodeType(order.getNextNodeType());
        updateOrder.setLastRecordId(record.getId());

        //修改我的审批状态
        taskService.dealHaveDo(order,user.getId());
        switch (order.getProcessStatus().intValue()){
            case ProcessStatusConstant.WAIT_VERIFY:
                dealNextStep(order,updateOrder,process);
                break;
            case ProcessStatusConstant.WAIT_DISTRIBUTION:
                dealNextStep(order,updateOrder,process);
                break;
            case ProcessStatusConstant.WAIT_GIVECAR:
                dealNextStep(order,updateOrder,process);
                break;
            case ProcessStatusConstant.WAIT_GETCAR:
                dealNextStep(order,updateOrder,process);
                break;
            case ProcessStatusConstant.WAIT_RETURNCAR:
                dealNextStep(order,updateOrder,process);
                break;
            case ProcessStatusConstant.WAIT_STA:
                dealNextStep(order,updateOrder,process);
                break;
            case ProcessStatusConstant.WAIT_END:
                dealNextStep(order,updateOrder,process);
                break;
            case ProcessStatusConstant.WAIT_RECIVE:
                updateOrder.setProcessStatus(ProcessStatusEnum.FINISH.getCode());
                updateOrder.setNextNodeType(ProcessNodeTypeConstant.FINISH);
                if (order.getCarId().intValue() > 0){
                    ReportExpense expense = new ReportExpense();
                    expense.setEtpId(order.getEtpId());
                    expense.setCarId(order.getCarId());
                    expense.setApplyCarCount(1);
                    expense.setMonthShort(order.getEndTime());
                    itemService.saveReportExpense(expense);
                }
                //生成抄送记录
                if (StrUtil.isNotBlank(detail.getCcUserList())) {
                    taskService.createCCTask(order.getOrderId(), detail.getCcUserList());
                    //TODO 通知抄送人
                }
                taskService.overTask(order.getOrderId());
                break;
            case ProcessStatusConstant.RECIVE_BACK:
                dealNextStep(order,updateOrder,process);
                break;
        }
        recordService.save(record);
        updateOrder.setLastRecordId(record.getId());
        this.updateById(updateOrder);
        return R.ok();
    }

    private void dealNextStep(ApplyCarOrder order,ApplyCarOrder updateOrder,ApplyCarProcess process){
        ApplyVerifyNode afteNode = processService.getNextNode(order.getApplyType(),
                order.getDriveType(),order.getProcessId(), order.getNextNodeType());
//        if (null == afteNode && order.getProcessStatus().intValue() == ProcessStatusConstant.RECIVE_BACK){
//            afteNode = nodeService.getNode(order.getProcessId(),ProcessNodeTypeConstant.FINISH);
//        }
        updateOrder.setNextNodeType(afteNode.getNodeType());
        Integer processStatus = null;
        //生成流程状态
        switch (afteNode.getNodeType()){
            case ProcessNodeTypeConstant.START_USE:
                processStatus = ProcessStatusEnum.WAIT_STA.getCode();
                break;
            case ProcessNodeTypeConstant.END_USE:
                processStatus = ProcessStatusEnum.WAIT_END.getCode();
                break;
            case ProcessNodeTypeConstant.GIVE_CAR:
                if (process.getProcessType().intValue() == ProcessTypeConstant.PUBLIC_APPLY){
                    //公车申请强制分配司机和车辆
                    processStatus = ProcessStatusEnum.WAIT_DISTRIBUTION.getCode();
                }else if(order.getApplyType().intValue() == ProcessTypeConstant.PRIVATE_APPLY && order.getDriveType().intValue() == ProcessOrderDriveTypeEnum.DRIVER.getCode()){
                    processStatus = ProcessStatusEnum.WAIT_DISTRIBUTION.getCode();
                }else if(order.getApplyType().intValue() == ProcessTypeConstant.PRIVATE_APPLY && order.getDriveType().intValue() == ProcessOrderDriveTypeEnum.SELF.getCode()) {
                    processStatus = ProcessStatusEnum.WAIT_STA.getCode();
                }
                break;
            case ProcessNodeTypeConstant.GET_CAR:
                processStatus = ProcessStatusEnum.WAIT_GETCAR.getCode();
                break;
            case ProcessNodeTypeConstant.RETURN_CAR:
                processStatus = ProcessStatusEnum.WAIT_RETURNCAR.getCode();
                break;
            case ProcessNodeTypeConstant.FINISH:
                processStatus = ProcessStatusEnum.WAIT_RECIVE.getCode();
                break;

        }
        if (null != processStatus){
            updateOrder.setProcessStatus(processStatus);
        }

        ApplyCarOrder taskOrder = new ApplyCarOrder();
        taskOrder.setProcessId(order.getProcessId());
        taskOrder.setProcessStatus(updateOrder.getProcessStatus());
        if (updateOrder.getDriveType() != null){
            taskOrder.setDriveType(updateOrder.getDriveType());
        }else {
            taskOrder.setDriveType(order.getDriveType());
        }
        taskOrder.setNextNodeType(order.getNextNodeType());

        if (updateOrder.getVerifyUserList() != null){
            taskOrder.setVerifyUserList(updateOrder.getVerifyUserList());
        }else {
            taskOrder.setVerifyUserList(order.getVerifyUserList());
        }
        taskOrder.setDriverId(order.getDriverId());
        //6.生成下一批任务
        taskOrder.setEtpId(order.getEtpId());
        taskOrder.setOrderId(order.getOrderId());
        taskOrder.setApplyUserId(order.getApplyUserId());
        taskService.createTask(taskOrder);
    }


    /**
     * 用车数据总览
     * @param date
     * @param etpId
     * @return
     */
    @Override
    public ApplyCarDataVo applyCarData(Date date, Integer etpId) {
        ApplyCarDataVo applyCarDataVo = new ApplyCarDataVo();
        QueryWrapper<ApplyCarOrder> query = new QueryWrapper<ApplyCarOrder>()
                .eq("etp_id", etpId)
                .eq("process_status", 14)
                .ge("sta_time", date);
        //总用车数
        Integer sumCount=baseMapper.selectCount(query);
        //普通用车次数
        Integer comCount = baseMapper.selectCount(query.in("apply_type ", 0, 2));
        //私车公用
        Integer privateCount=sumCount-comCount;
        //司机
        Integer driverCount = baseMapper.selectCount(query.eq("drive_type", 1));
        //自驾 用车数
        Integer selfCount=sumCount-driverCount;
        List<Integer> list= baseMapper.getApplyCarCount(date,etpId);
        //企业车辆数量
        Integer carCount = carInfoService.getCarCount(etpId);
        if (list.size()>0){
            //空置数
            int vcantCount=carCount-list.size();
            applyCarDataVo.setCarVacantCount(vcantCount);
            //空置率
            DecimalFormat df = new DecimalFormat("0.000");
            String format = df.format((double) vcantCount/carCount*100);
            Double dou = Double.valueOf(format);
            String s = roundStr(dou, 2);
            applyCarDataVo.setVacantRate(s+"%");
        }
        else {
            applyCarDataVo.setCarVacantCount(carCount);
        }
        applyCarDataVo.setSumCount(sumCount);
        applyCarDataVo.setComCount(comCount);
        applyCarDataVo.setPrivateCount(privateCount);
        applyCarDataVo.setDriverCount(driverCount);
        applyCarDataVo.setSelfDiverCount(selfCount);
        return applyCarDataVo;
    }

}
