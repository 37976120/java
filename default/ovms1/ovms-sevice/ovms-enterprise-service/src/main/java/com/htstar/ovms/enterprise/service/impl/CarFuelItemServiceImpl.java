package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.core.util.WebUtils;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.CarItemStatusConstant;
import com.htstar.ovms.enterprise.api.constant.CardCostInfoConstant;
import com.htstar.ovms.enterprise.api.constant.ItemTypeConstant;
import com.htstar.ovms.enterprise.api.dto.ApplyCostProcessDTO;
import com.htstar.ovms.enterprise.api.entity.CarFuelItem;
import com.htstar.ovms.enterprise.api.entity.CardCostInfo;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarEtcItemPageVO;
import com.htstar.ovms.enterprise.api.vo.CarFuelItemPageVO;
import com.htstar.ovms.enterprise.mapper.CarFuelItemMapper;
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

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * 公车加油表
 *
 * @author lw
 * @date 2020-06-08 13:48:44
 */
@Service
@Slf4j
public class CarFuelItemServiceImpl extends ServiceImpl<CarFuelItemMapper, CarFuelItem> implements CarFuelItemService {
    @Autowired
    private CarInfoService carInfoService;

    @Autowired
    private CarFuelCardService carFuelCardService;

    @Autowired
    private CardCostInfoService cardCostInfoService;
    @Autowired
    private CarEtcItemService carEtcItemService;
    @Autowired
    private ApplyCostVerifyNodeService applyCostVerifyNodeService;
    @Autowired
    private ApplyCostProcessRecordService applyCostProcessRecordService;


    /**
     * 新增加油信息
     *
     * @param carFuelItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveInfo(CarFuelItem carFuelItem) {
        log.info("新增加油记录信息为{}", carFuelItem);
        if (carFuelItem.getCarId() == null) {
            return R.failed("请选择车辆信息");
        }
        if (carFuelItem.getId() != null) {
            return this.updateFuelById(carFuelItem);
        }
        //加油时间
        LocalDateTime fuelTime = carFuelItem.getFuelTime();
        LocalDateTime now = LocalDateTime.now();
        if (fuelTime.compareTo(now) > 0) {
            return R.failed("加油时间不得晚于当前时间");
        }
        if (carFuelItem.getFuelMoney() < 0 || carFuelItem.getUnitPrice() < 0 || carFuelItem.getInstrMileage() < 0) {
            return R.failed("不能有负数");
        }

       /* Integer orderId = carFuelItem.getOrderId();
        if (orderId != null && orderId > 0) {
            Integer count = baseMapper.selectCount(new QueryWrapper<CarFuelItem>()
                    .eq("order_id", orderId).eq("del_flag", 0));
            if (count > 0) {
                return R.failed("保存失败,用车记录已存在");
            }
        }*/

        Integer itemStatus = carFuelItem.getItemStatus();
        //是否需要审批
        Boolean isApply = applyCostVerifyNodeService.isNeedVerify();
        //pc端
        if (itemStatus == null) {
            //需要审批
            if (isApply) {
                itemStatus = (CarItemStatusConstant.WAIT_CHECK);
            }
            //不需要审批 直接存档
            else {
                itemStatus = (CarItemStatusConstant.ARCHIVED);
            }
        }
        else {
            if (isApply && itemStatus == CarItemStatusConstant.ARCHIVED) {
                return R.failed("提交失败,需审核");
            }
            if (!isApply && itemStatus == CarItemStatusConstant.WAIT_CHECK) {
                return R.failed("提交失败,无需审核");
            }
        }
        Integer cardId = carFuelItem.getCardId();
        //绑定有油卡
        if (cardId != null) {
            //判断余额是否可以扣费
            boolean bol = carFuelCardService.checkIsBinding(cardId, carFuelItem.getFuelMoney());
            if (!bol) {
                return R.ok("油卡余额不足,添加失败");
            }
            //扣费,返回扣费最新余额
            Integer endCost = carFuelCardService.fuelDeduction(cardId, carFuelItem.getFuelMoney());
            //加油后余额
            carFuelItem.setEndCost(endCost);
            //直接存档
            if (itemStatus == CarItemStatusConstant.ARCHIVED) {
                //添加流水
                this.addCostInfo(carFuelItem);
                //添加到报表
                this.addReport(carFuelItem);
            }
        }
        OvmsUser user = SecurityUtils.getUser();
        carFuelItem.setEtpId(user.getEtpId());
        carFuelItem.setUserId(user.getId());
        carFuelItem.setItemStatus(itemStatus);
        baseMapper.insert(carFuelItem);
        if (itemStatus == CarItemStatusConstant.ARCHIVED) {
            //添加到报表
            this.addReport(carFuelItem);
        }
        //需要审批
        if (itemStatus == CarItemStatusConstant.WAIT_CHECK) {
            ApplyCostProcessDTO dto = this.getDto(carFuelItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("保存成功");
    }


    /**
     * 加油信息分页
     *
     * @param carItemPageReq
     * @return
     */
    @Override
    public IPage<CarFuelItemPageVO> queryPage(CarItemPageReq carItemPageReq) {
        CarItemPageReq pageReqByRole = carInfoService.getPageReqByRole(carItemPageReq);
        return baseMapper.queryPage(pageReqByRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R removeByIds(String ids) {
        if (StrUtil.isBlank(ids)) {
            return R.failed("请选择要删除的数据");
        }
        String[] split = ids.replace("\"", "").split(",");
        for (String s : split) {
            Integer id = Integer.valueOf(s);
            CarFuelItem carFuelItem = baseMapper.selectOne(new QueryWrapper<CarFuelItem>().eq("id", id));
            if (carFuelItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
                continue;
            }
            //加油费用
            Integer fuelMoney = carFuelItem.getFuelMoney();
            Integer cardId = carFuelItem.getCardId();
            //扣除的费用重新添加
            if (cardId != null) {
                carFuelCardService.fuelDeduction(cardId, fuelMoney * -1);
            }
            carFuelItem.setDelFlag(1);
            baseMapper.updateById(carFuelItem);
            applyCostProcessRecordService.delByCostIdAndCostType(id,ItemTypeConstant.FUEL);
        }
        return R.ok("删除成功");
    }

    /**
     * 根据id修改
     *
     * @param carFuelItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateFuelById(CarFuelItem carFuelItem) {
        if ( carFuelItem.getFuelTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("加油时间不得晚于当前时间");
        }
        if (carFuelItem.getFuelMoney() < 0 || carFuelItem.getUnitPrice() < 0 || carFuelItem.getInstrMileage() < 0) {
            return R.failed("不能有负数 ");
        }

        CarFuelItem fuelItem = baseMapper.selectOne(new QueryWrapper<CarFuelItem>().eq("id", carFuelItem.getId()));
        //原状态
        Integer oldItemStatus = fuelItem.getItemStatus();
        //新状态
        Integer itemStatus = carFuelItem.getItemStatus();
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
                return R.failed("修改失败,已提交数据只有审批人可以修改");
            }
        }
       /* Integer orderId = carFuelItem.getOrderId();
        if (orderId != null && orderId > 0) {
            if (!fuelItem.getOrderId().equals(orderId)) {
                Integer count = baseMapper.selectCount(new QueryWrapper<CarFuelItem>()
                        .eq("order_id", orderId)
                        .eq("del_flag", 0));
                if (count > 0) {
                    return R.failed("保存失败,用车记录已存在");
                }
            }
        }*/
        //原本是否有绑卡
        Integer oldCardId = fuelItem.getCardId();
        boolean oldBool = oldCardId == null ? false : true;
        //修改前后都有绑定卡
        Integer cardId = carFuelItem.getCardId();
        //加油前余额
        Integer endCost = carFuelItem.getEndCost();
        if (cardId != null) {
            //修改时加油花费
            Integer fuelMoney = carFuelItem.getFuelMoney();
            //修改前的加油费用
            Integer oldFuelMoney = fuelItem.getFuelMoney();
            if (oldBool) {
                //前后卡号相同
                if (oldCardId.equals(cardId)) {
                    //前后费用不相同
                    if (!fuelMoney.equals(oldFuelMoney)) {
                        //差额
                        Integer margin = fuelMoney - oldFuelMoney;
                        //判断能否扣费
                        boolean bol = carFuelCardService.checkIsBinding(cardId, margin);
                        if (!bol) {
                            return R.failed("油卡余额不足,提交失败");
                        }
                        //扣费
                        endCost = carFuelCardService.fuelDeduction(cardId, margin);

                    }
                }
                //前后卡号不同
                else {
                    boolean b = carFuelCardService.checkIsBinding(cardId, fuelMoney);
                    if (!b) {
                        return R.failed("油卡余额不足,提交失败");
                    }
                    //新卡扣费
                    endCost = carFuelCardService.fuelDeduction(cardId, fuelMoney);
                    //原卡余额增加
                    carFuelCardService.fuelDeduction(oldCardId, oldFuelMoney * -1);
                }
            }
            //原本没有绑卡
            if (!oldBool) {
                boolean b = carFuelCardService.checkIsBinding(cardId, fuelMoney);
                if (!b) {
                    return R.failed("油卡余额不足,提交失败");
                }
                endCost = carFuelCardService.fuelDeduction(cardId, fuelMoney);
            }
            carFuelItem.setEndCost(endCost);
            //直接存档,添加流水
            if (carFuelItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
                this.addCostInfo(carFuelItem);
                //添加到报表
                this.addReport(carFuelItem);
            }
        }
        baseMapper.updateById(carFuelItem);
        //非保存的数据 都要重新修改审核数据
        if ((oldItemStatus==CarItemStatusConstant.WAIT_SUBMIT&&itemStatus==CarItemStatusConstant.WAIT_CHECK)
                || (oldItemStatus==CarItemStatusConstant.WAIT_CHECK&&itemStatus==CarItemStatusConstant.WAIT_CHECK)) {
            ApplyCostProcessDTO dto = this.getDto(carFuelItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }

        return R.ok("提交成功");

    }


    /**
     * 加油信息导出Excel
     *
     * @param
     * @return
     */
    @Override
    public void exportExcel(ExportReq req) {
        log.info("导出请求数据为{}",req );
        Integer etpId = SecurityUtils.getUser().getEtpId();
        req.setEtpId(etpId);
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        //excel行数据
        List<CarFuelItemPageVO> carFuelItemPageVos = baseMapper.exportExcel(req);
        //车辆加油信息
        if (carFuelItemPageVos.size() > 0) {
            for (CarFuelItemPageVO carFuelItemPageVo : carFuelItemPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", carFuelItemPageVo.getLicCode());
                Integer itemStatus = carFuelItemPageVo.getItemStatus();
                String status = "";
                if (itemStatus == 1) {
                    status = "已提交";
                }
                if (itemStatus == 2) {
                    status = "已存档";
                }
                if (itemStatus == 3) {
                    status = "已退回";
                }
                map.put("状态", status);
                map.put("记录人", carFuelItemPageVo.getUsername());
                map.put("创建时间", carFuelItemPageVo.getCreateTime());
                map.put("加油时间", DateUtil.format(carFuelItemPageVo.getFuelTime(),"yyyy-MM-dd"));
                map.put("加油类型",carFuelItemPageVo.getFuelType()+"号汽油" );
                map.put("油品单价(元)", carFuelItemPageVo.getUnitPrice() == null ? "" : roundStr(((double) (carFuelItemPageVo.getUnitPrice()) / 100), 2));
                map.put("加油站",  carFuelItemPageVo.getFuelAddr());
                map.put("加油金额(元)", carFuelItemPageVo.getFuelMoney() == null ? "" : roundStr(((double) (carFuelItemPageVo.getFuelMoney()) / 100), 2));
                map.put("加油时公里数", carFuelItemPageVo.getInstrMileage());
                map.put("油卡",  carFuelItemPageVo.getCardNo());
                map.put("费用司机",  carFuelItemPageVo.getFuelAdmin());
                map.put("用车记录编号", carFuelItemPageVo.getOrderId());
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows, "加油信息");
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
        CarFuelItem carFuelItem = baseMapper.selectById(id);
        carFuelItem.setItemStatus(CarItemStatusConstant.ARCHIVED);
        //绑定了卡的话 去添加流水
        Integer cardId = carFuelItem.getCardId();
        if (cardId != null) {
            //添加流水;
            this.addCostInfo(carFuelItem);

        }
        //添加到报表
        this.addReport(carFuelItem);
        baseMapper.updateById(carFuelItem);
        return R.ok("存档成功");
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public CarFuelItemPageVO getCarFuelItemById(Integer id) {
        return baseMapper.getCarFuelItemById(id);

    }

    /**
     * 获取用户保存的信息
     *
     * @return
     */
    @Override
    public CarFuelItemPageVO getItemByUser() {
        Integer userId = SecurityUtils.getUser().getId();
        return baseMapper.getItemByUser(userId);

    }

    /**
     * 审批退回
     *
     * @param id
     * @param remark
     * @return
     */
    @Override
    public R withdraw(Integer id, String remark) {
        CarFuelItem item = baseMapper.selectOne(new QueryWrapper<CarFuelItem>().eq("id", id));
        //油卡
        Integer cardId = item.getCardId();
        if (item.getCardId()!=null){
            carFuelCardService.fuelDeduction(cardId, item.getFuelMoney()*-1);
        }
        item.setItemStatus(CarItemStatusConstant.WITHDRAW);
        item.setApplyRemark(remark);
        baseMapper.updateById(item);
        return R.ok();
    }

    /**
     * 添加费用流水
     *
     * @param carFuelItem
     */
    private void addCostInfo(CarFuelItem carFuelItem) {
        CardCostInfo cardCostInfo = new CardCostInfo();
        cardCostInfo.setCardId(carFuelItem.getCardId());
        cardCostInfo.setCarId(carFuelItem.getCarId());
        cardCostInfo.setCost(carFuelItem.getFuelMoney() * -1);
        cardCostInfo.setStaAddr(carFuelItem.getFuelAddr());
        cardCostInfo.setFuelType(carFuelItem.getFuelType());
        cardCostInfo.setCardType(CardCostInfoConstant.FUEL_CARD);
        cardCostInfo.setActionType(CardCostInfoConstant.CONSUME);
        cardCostInfo.setRemark(cardCostInfo.getRemark());
        cardCostInfo.setBalance(carFuelItem.getEndCost());
        cardCostInfo.setCostTime(carFuelItem.getFuelTime());
        cardCostInfoService.saveInfo(cardCostInfo);
    }

    /**
     * 存档数据同步到报表
     *
     * @param carFuelItem
     */
    private void addReport(CarFuelItem carFuelItem) {
        LocalDateTime itemTime = carFuelItem.getFuelTime();
        Integer carId = carFuelItem.getCarId();
        Integer etpId = carFuelItem.getEtpId();
        Integer cost = carFuelItem.getFuelMoney();
        ReportExpense reportExpense = new ReportExpense();
        reportExpense.setCarId(carId);
        reportExpense.setEtpId(etpId);
        reportExpense.setFuelCost(cost);
        reportExpense.setMonthShort(itemTime);
        carEtcItemService.saveReportExpense(reportExpense);
        Integer driverUserId = carFuelItem.getDriverUserId();
        //添加到司机数据
        if (driverUserId != null) {
            ReportDriverExpense reportDriverExpense = new ReportDriverExpense();
            BeanUtil.copyProperties(reportExpense, reportDriverExpense);
            reportDriverExpense.setDriverUserId(driverUserId);
            carEtcItemService.saveReportDriverExpense(reportDriverExpense);
        }
    }

    private ApplyCostProcessDTO getDto(CarFuelItem item){
        ApplyCostProcessDTO applyCostProcessDTO = new ApplyCostProcessDTO();
        applyCostProcessDTO.setCostId(item.getId());
        applyCostProcessDTO.setCarId(item.getCarId());
        applyCostProcessDTO.setCost(item.getFuelMoney());
        applyCostProcessDTO.setCostTime(item.getFuelTime());
        applyCostProcessDTO.setCostType(ItemTypeConstant.FUEL);
        return applyCostProcessDTO;
    }

}
