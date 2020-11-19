package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.CarItemStatusConstant;
import com.htstar.ovms.enterprise.api.constant.ItemTypeConstant;
import com.htstar.ovms.enterprise.api.entity.ApplyCostProcessRecord;
import com.htstar.ovms.enterprise.api.entity.ApplyCostVerifyNode;
import com.htstar.ovms.enterprise.api.entity.CarAccidItem;
import com.htstar.ovms.enterprise.api.entity.CarInfo;
import com.htstar.ovms.enterprise.api.req.ApprovalRecordReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.req.WithdrawReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCostProcessRecordVo;
import com.htstar.ovms.enterprise.api.vo.CarMotItemPageVO;
import com.htstar.ovms.enterprise.mapper.ApplyCostProcessRecordMapper;
import com.htstar.ovms.enterprise.service.*;
import com.htstar.ovms.msg.api.feign.MsgAppPushFeign;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Signature;
import java.util.*;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * 费用审批记录
 *
 * @author lw
 * @date 2020-07-14 10:59:15
 */
@Service
@Slf4j
public class ApplyCostProcessRecordServiceImpl extends ServiceImpl<ApplyCostProcessRecordMapper, ApplyCostProcessRecord> implements ApplyCostProcessRecordService {
    @Autowired
    private ApplyCostVerifyNodeService costVerifyNodeService;
    @Autowired
    private CarEtcItemService carEtcItemService;
    @Autowired
    private CarFuelItemService carFuelItemService;
    @Autowired
    private CarMotItemService carMotItemService;
    @Autowired
    private CarInsItemService carInsItemService;
    @Autowired
    private CarMaiItemService carMaiItemService;
    @Autowired
    private CarRepairItemService  carRepairItemService;
    @Autowired
    private CarOtherItemService carOtherItemService;
    @Autowired
    private CarInfoService  carInfoService;
    @Autowired
    private CarAccidItemService accidItemService;
    @Autowired
    private MsgAppPushFeign msgAppPushFeign;
    /**
     * 保存审批申请
     *
     * @param costProcessRecord
     * @return
     */
    @Override
    public R saveApply(ApplyCostProcessRecord costProcessRecord) {
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        Integer userId = user.getId();
        costProcessRecord.setEtpId(etpId);
        //申请人
        costProcessRecord.setApplyUserId(userId);
        baseMapper.insert(costProcessRecord);
        ApplyCostVerifyNode etpNowCostVerifyNode = costVerifyNodeService.getEtpNowCostVerifyNode();
        //审批人列表
        String verifyUserList = etpNowCostVerifyNode.getVerifyUserList();
        String str[] = verifyUserList.split(",");
        List<String> list = Arrays.asList(str);
        //审批推送
        String nickName= baseMapper.getNickName(userId);
        ArrayList<SingleAppPushReq> pushReqList = new ArrayList<>();
        for (String s : list) {
            //审批人id
            Integer verityUser = Integer.valueOf(s);
            SingleAppPushReq singleAppPushReq = new SingleAppPushReq();
            singleAppPushReq.setAppCarId(costProcessRecord.getId());
            singleAppPushReq.setRemindType(1);
            singleAppPushReq.setTitle("费用审批提醒");
            singleAppPushReq.setContent(nickName+"提交的费用申请需要您审批,请及时处理");
            singleAppPushReq.setMsgType(3);
            singleAppPushReq.setUserId(verityUser);
            pushReqList.add(singleAppPushReq);

        }
        msgAppPushFeign.batchAppSinglePush(pushReqList, SecurityConstants.FROM_IN);
        return R.ok();
    }

    /**
     * 用户审批记录
     *
     * @return
     */
    @Override
    public R<IPage<ApplyCostProcessRecordVo>> queryPage(ApprovalRecordReq req) {
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        //用户是否有审批权限
        Boolean bol = costVerifyNodeService.isUpdateItem();
        //没有审批权限,也不是管理员
        if (!bol && !SecurityUtils.getRoleCode(user).contains(CommonConstants.ROLE_ADMIN)) {
            return R.failed();
        }
        req.setEtpId(etpId);

        IPage<ApplyCostProcessRecordVo> page = baseMapper.queryPage(req);
        return R.ok(page);
    }

    /**
     * 获取申请记录
     *
     * @param costId
     * @param costType
     * @return
     */
    @Override
    public ApplyCostProcessRecordVo getApplyRecordById(Integer costId, Integer costType) {
        return baseMapper.getApplyRecordById(costId, costType);
    }

    /**
     * 是否有待审批的记录
     *
     * @param etpId
     * @return
     */
    @Override
    public Boolean isNoApproved(Integer etpId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<ApplyCostProcessRecord>()
                .eq("etp_id", etpId).eq("operation_type", 1));
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 存档
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R filing(Integer id) {
        OvmsUser user = SecurityUtils.getUser();
        Integer userId = user.getId();
        //最新审批配置
        Boolean isUpdate = costVerifyNodeService.isUpdateItem();
        if (!isUpdate) {
            return R.failed("您没有审批权限,审批失败");
        }

        ApplyCostProcessRecord applyCostProcessRecord = baseMapper.selectOne(new QueryWrapper<ApplyCostProcessRecord>().eq("id", id));
        if (applyCostProcessRecord.getOperationType() == CarItemStatusConstant.ARCHIVED) {
            return R.ok("操作成功");
        }
        //费用类型 id
        Integer costType = applyCostProcessRecord.getCostType();
        Integer costId = applyCostProcessRecord.getCostId();
        switch (costType) {
            case ItemTypeConstant.ETC:
                carEtcItemService.filing(costId, CarItemStatusConstant.WAIT_CHECK);
                break;
            case ItemTypeConstant.FUEL:
                carFuelItemService.filing(costId, CarItemStatusConstant.WAIT_CHECK);
                break;
            case ItemTypeConstant.INS:
                carInsItemService.filing(costId, CarItemStatusConstant.WAIT_CHECK);
                break;
            case ItemTypeConstant.MAI:
                carMaiItemService.filing(costId, CarItemStatusConstant.WAIT_CHECK);
                break;
            case ItemTypeConstant.MOT:
                carMotItemService.filing(costId, CarItemStatusConstant.WAIT_CHECK);
                break;
            case ItemTypeConstant.REPAIR:
                carRepairItemService.filing(costId, CarItemStatusConstant.WAIT_CHECK);
                break;
            case ItemTypeConstant.ACCID:
                accidItemService.filing(costId, CarItemStatusConstant.WAIT_CHECK);
                break;
            default:
                carOtherItemService.filing(costId, CarItemStatusConstant.WAIT_CHECK);
                break;
        }
        applyCostProcessRecord.setOperationType(CarItemStatusConstant.ARCHIVED);
        applyCostProcessRecord.setOperationUserId(userId);
        baseMapper.updateById(applyCostProcessRecord);
        return R.ok("操作成功");
    }

    /**
     * 退回
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R withdraw(WithdrawReq req) {
        OvmsUser user = SecurityUtils.getUser();
        Integer userId = user.getId();
        Integer id = req.getId();
        String remark = req.getRemark();
        //最新审批配置
        Boolean isUpdate = costVerifyNodeService.isUpdateItem();
        if (!isUpdate) {
            return R.failed("您没有审批权限,退回失败");
        }
        ApplyCostProcessRecord applyCostProcessRecord = baseMapper.selectOne(new QueryWrapper<ApplyCostProcessRecord>().eq("id", id));
        Integer operationType = applyCostProcessRecord.getOperationType();
        if (operationType.equals(CarItemStatusConstant.ARCHIVED)) {
            return R.failed("已存档记录无法退回");
        }
        Integer costType = applyCostProcessRecord.getCostType();
        Integer costId = applyCostProcessRecord.getCostId();
        switch (costType) {
            case ItemTypeConstant.ETC:
                carEtcItemService.withdraw(costId, remark);
                break;
            case ItemTypeConstant.FUEL:
                carFuelItemService.withdraw(costId, remark);
                break;
            case ItemTypeConstant.INS:
                carInsItemService.withdraw(costId, remark);
                break;
            case ItemTypeConstant.MAI:
                carMaiItemService.withdraw(costId, remark);
                break;
            case ItemTypeConstant.MOT:
                carMotItemService.withdraw(costId, remark);
                break;
            case ItemTypeConstant.REPAIR:
                carRepairItemService.withdraw(costId, remark);
                break;
            case ItemTypeConstant.ACCID:
                accidItemService.withdraw(costId, remark);
                break;
            default:
                carOtherItemService.withdraw(costId,remark);
                break;
        }
        //退回
        applyCostProcessRecord.setOperationType(CarItemStatusConstant.WITHDRAW);
        applyCostProcessRecord.setRemark(remark);
        applyCostProcessRecord.setOperationUserId(userId);
        baseMapper.updateById(applyCostProcessRecord);
        //推送
        ArrayList<SingleAppPushReq> pushReqList = new ArrayList<>();
        SingleAppPushReq singleAppPushReq = new SingleAppPushReq();
        singleAppPushReq.setTitle("退回消息");
        singleAppPushReq.setContent("您的费用申请已被退回,请修改后再进行提交");
        singleAppPushReq.setMsgType(3);
        singleAppPushReq.setUserId(applyCostProcessRecord.getApplyUserId());
        singleAppPushReq.setRemindType(1);
        pushReqList.add(singleAppPushReq);
        msgAppPushFeign.batchAppSinglePush(pushReqList, SecurityConstants.FROM_IN);
        return R.ok("操作成功");
    }

    /**
     * 导出excel
     * @param req
     */
    @Override
    public void exportExcel(ExportReq req) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        req.setEtpId(etpId);
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        //excel行数据
        List<ApplyCostProcessRecordVo> list = baseMapper.exportExcel(req);
        if (CollUtil.isNotEmpty(list)){
            for (ApplyCostProcessRecordVo vo : list) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", vo.getLicCode());
                Integer operationType = vo.getOperationType();
                String status="";
                if (operationType == 1) {
                    status = "待审核";
                }
                else if (operationType == 2) {
                    status = "已存档";
                }
                else if (operationType == 3) {
                    status = "已退回";
                }
                map.put("审批状态",status );
                map.put("申请人",vo.getApplyName());
                map.put("创建时间", vo.getCreateTime());
                Integer costType = vo.getCostType();
                String type="";
                switch (costType) {
                    case ItemTypeConstant.ETC:
                        type="通行费用";
                        break;
                    case ItemTypeConstant.FUEL:
                        type="加油费用";
                        break;
                    case ItemTypeConstant.INS:
                        type="保险费用";
                        break;
                    case ItemTypeConstant.ACCID:
                        type="事故记录";
                        break;
                    case ItemTypeConstant.MAI:
                        type="保养费用";
                        break;
                    case ItemTypeConstant.MOT:
                        type="年检费用";
                        break;
                    case ItemTypeConstant.REPAIR:
                        type="维修费用";
                        break;
                    case ItemTypeConstant.STOP_CAR:
                        type="停车费用";
                        break;
                    case ItemTypeConstant.TICKET:
                        type="违章罚款";
                        break;
                    case ItemTypeConstant.WASH_CAR:
                        type="洗车费用";
                        break;
                    case ItemTypeConstant.SUPPLIES:
                        type="汽车用品";
                        break;
                    case ItemTypeConstant.OTHER:
                        type="其他费用";
                        break;
                }
                map.put("费用类型", type);
                map.put("费用产生时间", DateUtil.format(vo.getCostTime(), "yyyy-MM-dd") );
                map.put("费用金额(元)", roundStr(((double) (vo.getCost()) / 100), 2));
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows, "审批信息");
    }

    /**
     * 删除审批记录根据费用id 跟费用类型
     * @param costId
     * @param costType
     * @return
     */
    @Override
    public R delByCostIdAndCostType(Integer costId, Integer costType) {
        baseMapper.delByCostIdAndCostType(costId,costType);
        return R.ok();
    }

}
