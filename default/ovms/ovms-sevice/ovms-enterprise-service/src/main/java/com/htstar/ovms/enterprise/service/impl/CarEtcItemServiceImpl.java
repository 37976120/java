package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.core.util.WebUtils;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.CarItemStatusConstant;
import com.htstar.ovms.enterprise.api.constant.CardCostInfoConstant;
import com.htstar.ovms.enterprise.api.constant.ItemTypeConstant;
import com.htstar.ovms.enterprise.api.dto.ApplyCostProcessDTO;
import com.htstar.ovms.enterprise.api.entity.ApplyCostProcessRecord;
import com.htstar.ovms.enterprise.api.entity.CarEtcItem;
import com.htstar.ovms.enterprise.api.entity.CardCostInfo;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCostProcessRecordVo;
import com.htstar.ovms.enterprise.api.vo.CarEtcItemPageVO;
import com.htstar.ovms.enterprise.mapper.CarEtcItemMapper;
import com.htstar.ovms.enterprise.service.*;
import com.htstar.ovms.report.api.entity.ReportDriverExpense;
import com.htstar.ovms.report.api.entity.ReportExpense;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * etc通行记录
 *
 * @author lw
 * @date 2020-06-22 10:11:23
 */
@Service
@Slf4j
public class CarEtcItemServiceImpl extends ServiceImpl<CarEtcItemMapper, CarEtcItem> implements CarEtcItemService {
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private CarEtcCardService carEtcCardService;
    @Autowired
    private CardCostInfoService cardCostInfoService;
    @Autowired
    private ApplyCostVerifyNodeService applyCostVerifyNodeService;
    @Autowired
    private ApplyCostProcessRecordService applyCostProcessRecordService;

    /**
     * 新增
     *
     * @param carEtcItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveInfo(CarEtcItem carEtcItem) {
        if (carEtcItem.getCarId() == null) {
            return R.failed("请选择车辆信息");
        }
        if (carEtcItem.getId() != null) {
            return this.updateEtcById(carEtcItem);
        }
        if (carEtcItem.getItemTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("费用产生时间不得晚于当前时间");
        }
        Integer etcMoney = carEtcItem.getEtcMoney();
        if (etcMoney == null || carEtcItem.getEtcMoney() < 0) {
            return R.failed("请输入正确的金额");
        }
      /*  Integer orderId = carEtcItem.getOrderId();
        if (orderId != null && orderId > 0) {
            Integer count = baseMapper.selectCount(new QueryWrapper<CarEtcItem>().eq("order_id", orderId)
                    .eq("del_flag", 0));
            if (count > 0) {
                return R.failed("保存失败,用车记录已存在");
            }
        }*/
        Integer itemStatus = carEtcItem.getItemStatus();
        Boolean isApply = applyCostVerifyNodeService.isNeedVerify();
        //PC端提交
        if (itemStatus == null) {
            //需要审批
            if (isApply) {
                itemStatus = (CarItemStatusConstant.WAIT_CHECK);
            }
            //不需要审批 直接存档
            else {
                itemStatus = (CarItemStatusConstant.ARCHIVED);
            }
        } else {
            if (isApply && itemStatus == CarItemStatusConstant.ARCHIVED) {
                return R.failed("提交失败,需审核");
            }
            if (!isApply && itemStatus == CarItemStatusConstant.WAIT_CHECK) {
                return R.failed("提交失败,无需审核");
            }
        }
        Integer cardId = carEtcItem.getCardId();
        if (cardId != null) {
            //判断是否可以扣
            boolean bol = carEtcCardService.checkIsBinding(cardId, etcMoney);
            if (!bol) {
                return R.ok("卡里余额不足,添加失败");
            }
            //扣费
            Integer endCost = carEtcCardService.etcDeduction(cardId, etcMoney);
            carEtcItem.setEndCost(endCost);
            //直接存档  添加流水
            if (itemStatus == CarItemStatusConstant.ARCHIVED) {
                this.addCostInfo(carEtcItem);
                this.addReport(carEtcItem);
            }
        }
        OvmsUser user = SecurityUtils.getUser();
        carEtcItem.setUserId(user.getId());
        carEtcItem.setEtpId(user.getEtpId());
        carEtcItem.setItemStatus(itemStatus);
        baseMapper.insert(carEtcItem);
        //直接存档  报表
        if (itemStatus == CarItemStatusConstant.ARCHIVED) {
            this.addReport(carEtcItem);
        }
        //需要审批
        if (itemStatus == CarItemStatusConstant.WAIT_CHECK) {
            ApplyCostProcessDTO dto = this.getDto(carEtcItem);
            this.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("添加成功");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R removeByIds(String ids) {
        if (StrUtil.isBlank(ids)) {
            return R.failed("请选择要删除的数据");
        }
        String[] split = ids.replace("\"", "").split(",");
        for (String s : split) {
            Integer id = Integer.valueOf(s);
            CarEtcItem carEtcItem = baseMapper.selectOne(new QueryWrapper<CarEtcItem>().eq("id", id));
            if (carEtcItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
                continue;
            }
            Integer cardId = carEtcItem.getCardId();
            if (cardId != null) {
                carEtcCardService.etcDeduction(cardId, carEtcItem.getEtcMoney() * -1);
            }
            carEtcItem.setDelFlag(1);
            baseMapper.updateById(carEtcItem);
            applyCostProcessRecordService.delByCostIdAndCostType(id, ItemTypeConstant.ETC);
        }
        return R.ok("删除成功");
    }

    /**
     * 修改
     *
     * @param carEtcItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateEtcById(CarEtcItem carEtcItem) {
        Integer etcMoney = carEtcItem.getEtcMoney();
        if (carEtcItem.getItemTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("费用产生时间不得晚于当前时间");
        }
        if (etcMoney == null || etcMoney < 0) {
            return R.failed("请输入正确的费用");
        }
        CarEtcItem oldEtcItem = baseMapper.selectOne(new QueryWrapper<CarEtcItem>().eq("id", carEtcItem.getId()));
        Integer oldItemStatus = oldEtcItem.getItemStatus();
        Integer itemStatus = carEtcItem.getItemStatus();

        //原本是已经存档的数据
        if (oldItemStatus == CarItemStatusConstant.ARCHIVED) {
            return R.failed("存档数据不可修改");
        }
        //判断是否需要审批
        Boolean needVerify = applyCostVerifyNodeService.isNeedVerify();
        if (needVerify && itemStatus == CarItemStatusConstant.ARCHIVED) {
            return R.failed("提交失败,需审核");
        }
        if (!needVerify && itemStatus == CarItemStatusConstant.WAIT_CHECK) {
            return R.failed("提交失败,无需审核");
        }
        //原本是待存档状态 只有审批人可以修改
        if (oldItemStatus == CarItemStatusConstant.WAIT_CHECK) {
            Boolean updateItem = applyCostVerifyNodeService.isUpdateItem();
            if (!updateItem) {
                return R.failed("提交失败,已提交数据只有审批人可以修改");
            }
        }
        //原本是否有绑卡
        Integer oldCardId = oldEtcItem.getCardId();
        boolean oldBool = oldCardId == null ? false : true;
        //修改前后都有绑定卡
        Integer cardId = carEtcItem.getCardId();
        //扣费后余额
        Integer endCost = carEtcItem.getEndCost();
        if (cardId != null) {
            Integer oldEtcMoney = oldEtcItem.getEtcMoney();
            if (oldBool) {
                //前后卡号相同
                if (cardId.equals(oldCardId)) {
                    //前后金额不相同
                    if (!etcMoney.equals(oldEtcMoney)) {
                        int margin = etcMoney - oldEtcMoney;
                        boolean bol = carEtcCardService.checkIsBinding(cardId, margin);
                        if (!bol) {
                            return R.ok("卡里余额不足,提交失败");
                        }
                        endCost = carEtcCardService.etcDeduction(cardId, margin);
                    }
                }
                //前后卡号不同
                else {
                    boolean b = carEtcCardService.checkIsBinding(cardId, etcMoney);
                    if (!b) {
                        return R.failed("卡里余额不足,提交失败");
                    }
                    //新卡扣费
                    endCost = carEtcCardService.etcDeduction(cardId, etcMoney);
                    //旧卡增加
                    carEtcCardService.etcDeduction(oldCardId, oldEtcMoney * -1);
                }
            }
            //原本没有绑卡
            if (!oldBool) {
                Integer sumCostByCard = baseMapper.getSumCostByCard(cardId);
                boolean b = carEtcCardService.checkIsBinding(cardId, sumCostByCard + etcMoney);
                if (!b) {
                    return R.failed("卡里余额不足,提交失败");
                }
                endCost = carEtcCardService.etcDeduction(cardId, etcMoney);
            }
            carEtcItem.setEndCost(endCost);
            //存档 需要添加流水 直接扣费
            if (itemStatus == CarItemStatusConstant.ARCHIVED) {
                this.addCostInfo(carEtcItem);
                this.addReport(carEtcItem);
            }
        }
        baseMapper.updateById(carEtcItem);
        //非保存的数据 都要重新修改审核数据
        if ((oldItemStatus == CarItemStatusConstant.WAIT_SUBMIT && itemStatus == CarItemStatusConstant.WAIT_CHECK)
                || (oldItemStatus == CarItemStatusConstant.WAIT_CHECK && itemStatus == CarItemStatusConstant.WAIT_CHECK)) {
            ApplyCostProcessDTO dto = this.getDto(carEtcItem);
            this.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("提交成功");
    }

    /**
     * 分页查询
     *
     * @param carItemPageReq
     * @return
     */
    @Override
    public IPage<CarEtcItemPageVO> queryPage(CarItemPageReq carItemPageReq) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        carItemPageReq.setEtpId(etpId);
        return baseMapper.queryPage(carItemPageReq);
    }

    /**
     * 导出excel
     *
     * @param
     * @param
     */
    @Override
    public void exportExcel(ExportReq req) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        req.setEtpId(etpId);
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        //excel行数据
        List<CarEtcItemPageVO> carEtcItemPageVos = baseMapper.exportExcel(req);
        if (carEtcItemPageVos.size() > 0) {
            for (CarEtcItemPageVO carEtcItemPageVo : carEtcItemPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", carEtcItemPageVo.getLicCode());
                map.put("用车记录编号", carEtcItemPageVo.getOrderId());
                Integer itemStatus = carEtcItemPageVo.getItemStatus();
                String status = "";
                if (itemStatus == 1) {
                    status = "待审核";
                }
                if (itemStatus == 2) {
                    status = "已存档";
                }
                if (itemStatus == 3) {
                    status = "已退回";
                }
                map.put("状态", status);
                map.put("记录人", carEtcItemPageVo.getUsername());
                map.put("创建时间", carEtcItemPageVo.getCreateTime());
                map.put("费用产生时间", DateUtil.format(carEtcItemPageVo.getItemTime(), "yyyy-MM-dd"));
                map.put("入口", carEtcItemPageVo.getEntrance());
                map.put("出口", carEtcItemPageVo.getExport());
                map.put("金额（元）", carEtcItemPageVo.getEtcMoney() == null ? "" : roundStr(((double) (carEtcItemPageVo.getEtcMoney()) / 100), 2));
                map.put("ETC号", carEtcItemPageVo.getCardNo());
                map.put("费用司机", carEtcItemPageVo.getItemDriver());
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows, "etc信息");
    }

    /**
     * 存档
     *
     * @param id
     * @param itemStatus
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R filing(Integer id, Integer itemStatus) {
        if (itemStatus == CarItemStatusConstant.ARCHIVED) {
            return R.ok("存档成功");
        }
        CarEtcItem carEtcItem = baseMapper.selectById(id);
        //绑定了卡的话 去添加流水
        Integer cardId = carEtcItem.getCardId();
        if (cardId != null) {
            this.addCostInfo(carEtcItem);
        }
        carEtcItem.setItemStatus(CarItemStatusConstant.ARCHIVED);
        baseMapper.updateById(carEtcItem);
        //添加到数据报表
        this.addReport(carEtcItem);
        return R.ok("存档成功");
    }

    /**
     * 车辆费用到报表
     *
     * @param reportExpense
     */
    @Override
    public void saveReportExpense(ReportExpense reportExpense) {

        baseMapper.saveReportExpenseByCar(reportExpense);
    }

    /**
     * 保存司机费用到报表
     *
     * @param reportDriverExpense
     */
    @Override
    public void saveReportDriverExpense(ReportDriverExpense reportDriverExpense) {
        baseMapper.saveReportExpenseByDriver(reportDriverExpense);
    }

    @Override
    public CarEtcItemPageVO getCarEtcItemById(Integer id) {
        return baseMapper.getCarEtcItemById(id);
    }

    /**
     * 获取用户保存的信息
     *
     * @return
     */
    @Override
    public CarEtcItemPageVO getCarEtcItemByUser() {
        Integer userId = SecurityUtils.getUser().getId();
        return baseMapper.getCarEtcItemByUser(userId);
    }

    /**
     * 退回
     *
     * @param id
     * @return
     */
    @Override
    public R withdraw(Integer id, String remark) {
        CarEtcItem etcItem = baseMapper.selectOne(new QueryWrapper<CarEtcItem>().eq("id", id));
        //油卡
        Integer cardId = etcItem.getCardId();
        if (etcItem.getCardId() != null) {
            carEtcCardService.etcDeduction(cardId, etcItem.getEtcMoney() * -1);
        }
        etcItem.setItemStatus(CarItemStatusConstant.WITHDRAW);
        etcItem.setApplyRemark(remark);
        baseMapper.updateById(etcItem);
        return R.ok();
    }

    /**
     * 新增审批
     *
     * @param costDto
     */
    @Override
    public void addOrUpdateCostProcessRecord(ApplyCostProcessDTO costDto) {
        ApplyCostProcessRecordVo applyRecord = applyCostProcessRecordService.getApplyRecordById(costDto.getCostId(), costDto.getCostType());
        if (applyRecord != null) {
            BeanUtil.copyProperties(costDto, applyRecord);
            applyCostProcessRecordService.updateById(applyRecord);
        } else {
            //新增审批
            ApplyCostProcessRecord applyCostProcessRecord = new ApplyCostProcessRecord();
            BeanUtil.copyProperties(costDto, applyCostProcessRecord);
            applyCostProcessRecordService.saveApply(applyCostProcessRecord);

        }
    }

    /**
     * 添加费用流水
     *
     * @param carEtcItem
     */
    private void addCostInfo(CarEtcItem carEtcItem) {
        CardCostInfo cardCostInfo = new CardCostInfo();
        cardCostInfo.setCardId(carEtcItem.getCardId());
        cardCostInfo.setCarId(carEtcItem.getCarId());
        cardCostInfo.setCost(carEtcItem.getEtcMoney() * -1);
        cardCostInfo.setStaAddr(carEtcItem.getEntrance());
        cardCostInfo.setEndAddr(carEtcItem.getExport());
        cardCostInfo.setActionType(CardCostInfoConstant.CONSUME);
        cardCostInfo.setCardType(CardCostInfoConstant.ETC_CARD);
        cardCostInfo.setCostTime(carEtcItem.getItemTime());
        cardCostInfoService.saveInfo(cardCostInfo);
    }


    /**
     * 存档数据同步到报表
     *
     * @param carEtcItem
     */

    private void addReport(CarEtcItem carEtcItem) {
        LocalDateTime itemTime = carEtcItem.getItemTime();
        Integer carId = carEtcItem.getCarId();
        Integer etpId = carEtcItem.getEtpId();
        Integer cost = carEtcItem.getEtcMoney();
        ReportExpense reportExpense = new ReportExpense();
        reportExpense.setCarId(carId);
        reportExpense.setEtpId(etpId);
        reportExpense.setEtcCost(cost);
        reportExpense.setMonthShort(itemTime);
        this.saveReportExpense(reportExpense);
        Integer driverUserId = carEtcItem.getDriverUserId();
        //添加到司机数据
        if (driverUserId != null) {
            ReportDriverExpense reportDriverExpense = new ReportDriverExpense();
            BeanUtil.copyProperties(reportExpense, reportDriverExpense);
            reportDriverExpense.setDriverUserId(driverUserId);
            this.saveReportDriverExpense(reportDriverExpense);
        }
    }

    private ApplyCostProcessDTO getDto(CarEtcItem carEtcItem) {
        ApplyCostProcessDTO applyCostProcessDTO = new ApplyCostProcessDTO();
        applyCostProcessDTO.setCostId(carEtcItem.getId());
        applyCostProcessDTO.setCarId(carEtcItem.getCarId());
        applyCostProcessDTO.setCost(carEtcItem.getEtcMoney());
        applyCostProcessDTO.setCostTime(carEtcItem.getItemTime());
        applyCostProcessDTO.setCostType(ItemTypeConstant.ETC);
        return applyCostProcessDTO;
    }
}
