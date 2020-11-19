package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.core.util.WebUtils;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.CarItemStatusConstant;
import com.htstar.ovms.enterprise.api.constant.ItemTypeConstant;
import com.htstar.ovms.enterprise.api.dto.ApplyCostProcessDTO;
import com.htstar.ovms.enterprise.api.entity.CarEtcItem;
import com.htstar.ovms.enterprise.api.entity.CarFuelItem;
import com.htstar.ovms.enterprise.api.entity.CarOtherItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarFuelItemPageVO;
import com.htstar.ovms.enterprise.api.vo.CarOtherItemPageVO;
import com.htstar.ovms.enterprise.api.vo.CarOtherVo;
import com.htstar.ovms.enterprise.mapper.CarOtherItemMapper;
import com.htstar.ovms.enterprise.service.*;
import com.htstar.ovms.report.api.entity.ReportDriverExpense;
import com.htstar.ovms.report.api.entity.ReportExpense;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * 公车其他项目表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Service
@Slf4j
public class CarOtherItemServiceImpl extends ServiceImpl<CarOtherItemMapper, CarOtherItem> implements CarOtherItemService {
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private CarEtcItemService carEtcItemService;
    @Autowired
    private CarFuelItemService carFuelItemService;
    @Autowired
    private ApplyCostVerifyNodeService applyCostVerifyNodeService;
    @Autowired
    private ApplyCostProcessRecordService applyCostProcessRecordService;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public IPage<CarOtherItemPageVO> queryPage(CarItemPageReq carItemPageReq) {
        CarItemPageReq pageReqByRole = carInfoService.getPageReqByRole(carItemPageReq);
        return baseMapper.queryPage(pageReqByRole);
    }

    /**
     * 新增
     *
     * @param carOtherItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveInfo(CarOtherItem carOtherItem) {
        log.info("其他费用数据为{}",carOtherItem );
        if (carOtherItem.getCarId() == null) {
            return R.failed("请选择车辆信息");
        }
        if (carOtherItem.getItemType() == null) {
            return R.failed("请选择费用类型");
        }
        if (carOtherItem.getItemTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("费用产生时间不得晚于当前时间");
        }

        if (carOtherItem.getItemMoney() <= 0) {
            return R.failed("请输入正确的金额");
        }
        if (carOtherItem.getId() != null) {
            return this.update(carOtherItem);
        }
      /*  Integer orderId = carOtherItem.getOrderId();
        if (orderId != null && orderId > 0) {
            Integer count = baseMapper.selectCount(new QueryWrapper<CarOtherItem>()
                    .eq("order_id", orderId).eq("del_flag", 0)
                    .eq("item_type", carOtherItem.getItemType()));
            if (count > 0) {
                return R.failed("保存失败,该用车记录的该类费用已经存在");
            }
        }*/
        Integer itemStatus = carOtherItem.getItemStatus();
        //是否需要审批
        Boolean isApply = applyCostVerifyNodeService.isNeedVerify();
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
        OvmsUser user = SecurityUtils.getUser();
        carOtherItem.setEtpId(user.getEtpId());
        carOtherItem.setUserId(user.getId());
        carOtherItem.setItemStatus(itemStatus);
        baseMapper.insert(carOtherItem);
        //存档数据添加到报表
        if (carOtherItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
            this.addReport(carOtherItem);
        }
        //待提交 需同步
        if (itemStatus == CarItemStatusConstant.WAIT_CHECK) {
            ApplyCostProcessDTO dto = this.getDto(carOtherItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("保存成功");


    }

    /**
     * 根据Id删除
     *
     * @param ids
     * @return
     */
    @Override
    public R removeByIds(String ids) {
        if (StrUtil.isBlank(ids)) {
            return R.failed("请选择要删除的数据");
        }
        String[] split = ids.replace("\"", "").split(",");
        for (String s : split) {
            Integer id = Integer.valueOf(s);
            CarOtherItem carOtherItem = baseMapper.selectOne(new QueryWrapper<CarOtherItem>().eq("id", id));
            //存档数据不可删除
            if (carOtherItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
                continue;
            }
            carOtherItem.setDelFlag(1);
            baseMapper.updateById(carOtherItem);
            applyCostProcessRecordService.delByCostIdAndCostType(id,carOtherItem.getItemType() );

        }
        return R.ok("删除成功");
    }

    /**
     * 修改
     *
     * @param carOtherItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(CarOtherItem carOtherItem) {
        if (carOtherItem.getCarId() == null) {
            return R.failed("请选择车辆信息");
        }
        if (carOtherItem.getItemType() == null) {
            return R.failed("请选择费用类型");
        }
        if (carOtherItem.getItemTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("费用产生时间不得晚于当前时间");
        }
        if (carOtherItem.getItemMoney() < 0) {
            return R.failed("请输入正确的金额");
        }

        CarOtherItem otherItem = baseMapper.selectOne(new QueryWrapper<CarOtherItem>()
                .eq("id", carOtherItem.getId()));
        Integer oldItemStatus = otherItem.getItemStatus();
        Integer itemStatus = carOtherItem.getItemStatus();
        if (oldItemStatus == CarItemStatusConstant.ARCHIVED) {
            return R.failed("存档数据不可再次提交");
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

        /*Integer orderId = carOtherItem.getOrderId();
        Integer itemType = carOtherItem.getItemType();
        if (orderId != null && orderId > 0) {
            //前后用车id不同
            if (!otherItem.getOrderId().equals(orderId)) {
                Integer count = baseMapper.selectCount(new QueryWrapper<CarOtherItem>()
                        .eq("order_id", orderId).eq("del_flag", 0)
                        .eq("item_type", carOtherItem.getItemType()));
                if (count > 0) {
                    return R.failed("保存失败,该用车记录的该类费用已经存在");
                }
            }
            //前后订单号相同
            else {
                //订单相同 项目不同
                if (!otherItem.getItemType().equals(itemType)) {
                    Integer count = baseMapper.selectCount(new QueryWrapper<CarOtherItem>()
                            .eq("order_id", orderId)
                            .eq("item_type", itemType)
                            .eq("del_flag", 0));
                    if (count > 0) {
                        return R.failed("修改失败,用车记录的此类消费已经存在");
                    }
                }
            }
        }*/

        baseMapper.updateById(carOtherItem); //直接存档
        //存档数据添加到报表
        if (otherItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
            this.addReport(otherItem);
        }
        //非保存的数据 都要重新修改审核数据
        if ((oldItemStatus == CarItemStatusConstant.WAIT_SUBMIT && itemStatus == CarItemStatusConstant.WAIT_CHECK)
                || (oldItemStatus == CarItemStatusConstant.WAIT_CHECK && itemStatus == CarItemStatusConstant.WAIT_CHECK)) {
            ApplyCostProcessDTO dto = this.getDto(carOtherItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("提交成功");

    }

    /**
     * 导出
     *
     * @param
     * @param req
     */
    @Override
    public void exportExcel(ExportReq req) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        req.setEtpId(etpId);
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        //excel行数据
        List<CarOtherItemPageVO> carOtherItemPageVos = baseMapper.exportExcel(req);
        if (carOtherItemPageVos.size() > 0) {
            for (CarOtherItemPageVO carOtherItemPageVo : carOtherItemPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", carOtherItemPageVo.getLicCode());
                Integer itemStatus = carOtherItemPageVo.getItemStatus();
                String status = "";
                if (itemStatus == 1) {
                    status = "待审核";
                }
                else if (itemStatus == 2) {
                    status = "已存档";
                }
               else if (itemStatus == 3) {
                    status = "已退回";
                }
                map.put("状态", status);
                map.put("录入人", carOtherItemPageVo.getUsername());
                map.put("创建时间", carOtherItemPageVo.getCreateTime());
                Integer itemType = carOtherItemPageVo.getItemType();
                String type = "";
                switch (itemType) {
                    case ItemTypeConstant.STOP_CAR:
                        type = "停车费";
                        break;
                    case ItemTypeConstant.TICKET:
                        type = "违章罚款";
                        break;
                    case ItemTypeConstant.WASH_CAR:
                        type = "洗车美容";
                        break;
                    case ItemTypeConstant.SUPPLIES:
                        type = "购置车载设备";
                        break;
                    default:
                        type = "其他费用";
                        break;
                }
                map.put("费用类型", type);
                map.put("费用产生时间", DateUtil.format(carOtherItemPageVo.getItemTime(), "yyyy-MM-dd"));
                map.put("费用金额(元)", roundStr(((double) (carOtherItemPageVo.getItemMoney()) / 100), 2));
                map.put("费用司机", carOtherItemPageVo.getDriver());
                map.put("关联用车记录", carOtherItemPageVo.getOrderId());
                map.put("备注", carOtherItemPageVo.getRemark());
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows, "费用记录信息");
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
        CarOtherItem carOtherItem = baseMapper.selectById(id);
        carOtherItem.setItemStatus(CarItemStatusConstant.ARCHIVED);
        this.addReport(carOtherItem);
        if (baseMapper.updateById(carOtherItem) > 0) {
            return R.ok("存档成功");
        }
        return R.failed("存档失败");
    }

    /**
     * 批量保存
     *
     * @param carOtherItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R batchSave(CarOtherItem carOtherItem) {
        log.info("批量保存数据为{}",carOtherItem );
        List<CarOtherVo> carOtherVos = carOtherItem.getCarOtherVos();
        if (CollUtil.isEmpty(carOtherVos)) {
            return R.failed("请录入正确的费用");
        }
        if (carOtherItem.getCarId()==null){
            return R.failed("请传入车辆信息");
        }
        if (carOtherItem.getItemTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("费用产生时间不得晚于当前时间");
        }

        Integer orderId = carOtherItem.getOrderId();
        Integer driverUserId = carOtherItem.getDriverUserId();
        String remark = carOtherItem.getRemark();
        Integer carId = carOtherItem.getCarId();
        LocalDateTime itemTime = carOtherItem.getItemTime();
        for (CarOtherVo carOtherVo : carOtherVos) {
            Integer costType = carOtherVo.getCostType();
            Integer itemMoney = carOtherVo.getItemMoney();
            String billAddr = carOtherVo.getBillAddr();
            Integer itemStatus = carOtherItem.getItemStatus();
            //通行费
            if (costType == ItemTypeConstant.ETC) {
                CarEtcItem carEtcItem = new CarEtcItem();
                carEtcItem.setCarId(carId);
                if (orderId != null) {
                    carEtcItem.setOrderId(orderId);
                }
                if (driverUserId != null) {
                    carEtcItem.setDriverUserId(driverUserId);
                }
                carEtcItem.setItemTime(itemTime);
                carEtcItem.setEtcMoney(itemMoney);
                carEtcItem.setBillAddr(billAddr);
                carEtcItem.setItemStatus(itemStatus);
                R r = carEtcItemService.saveInfo(carEtcItem);
                if (r.getCode()== CommonConstants.FAIL){
                    return r;
                }
            }
         /*   //加油费
            if (costType == ItemTypeConstant.FUEL){
                CarFuelItem carFuelItem = new CarFuelItem();
                carFuelItem.setCarId(carId);
                carFuelItem.setOrderId(orderId);
                carFuelItem.setDriverUserId(driverUserId);
                carFuelItem.setFuelMoney(itemMoney);
                carFuelItem.setBillAddr(billAddr);
                carFuelItem.setItemStatus(itemStatus);
                carFuelItem.setRemark(remark);
                R r = carFuelItemService.saveInfo(carFuelItem);
                if (r.getCode()==CommonConstants.FAIL){
                    return r;
                }
            }*/
            else {
                CarOtherItem otherItem = new CarOtherItem();
                otherItem.setCarId(carId);
                otherItem.setDriverUserId(driverUserId);
                otherItem.setOrderId(orderId);
                otherItem.setItemType(costType);
                otherItem.setItemMoney(itemMoney);
                otherItem.setBillAddr(billAddr);
                otherItem.setItemStatus(itemStatus);
                otherItem.setItemTime(itemTime);
                otherItem.setRemark(remark);
                R r = this.saveInfo(otherItem);
                if (r.getCode()==CommonConstants.FAIL){
                    return r;
                }
            }
        }
        //如果是批量保存的需要清除
        Integer userId = SecurityUtils.getUser().getId();
        redisTemplate.opsForHash().delete(CacheConstants.BATCH_SAVE, userId.toString());
        return R.ok("保存成功");
    }

    /**
     * 根据id获取
     *
     * @param id
     * @return
     */
    @Override
    public CarOtherItemPageVO getItemById(int id) {
        return baseMapper.getItemById(id);
    }

    /**
     * 退回
     *
     * @param id
     * @param remark
     * @return
     */
    @Override
    public R withdraw(Integer id, String remark) {
        CarOtherItem item = baseMapper.selectOne(new QueryWrapper<CarOtherItem>().eq("id", id));
        item.setItemStatus(CarItemStatusConstant.WITHDRAW);
        item.setApplyRemark(remark);
        baseMapper.updateById(item);
        return R.ok();
    }

    /**
     * 获取用户保存的
     *
     * @param itemType
     * @return
     */
    @Override
    public CarOtherItemPageVO getItemByUser(Integer itemType) {
        Integer userId = SecurityUtils.getUser().getId();
        return baseMapper.getItemByUser(itemType, userId);
    }

    /**
     * 暂时批量保存到redis
     * @param carOtherItem
     * @return
     */
    @Override
    public R temporarySave(CarOtherItemPageVO carOtherItem) {
        log.info("暂存请求参数{}",carOtherItem );
        Integer userId = SecurityUtils.getUser().getId();
        String s = JSONObject.toJSONString(carOtherItem);
        redisTemplate.opsForHash().put(CacheConstants.BATCH_SAVE,userId.toString(), s);
        return R.ok("保存成功");
    }

    @Override
    public R getTemporarySave() {
        Integer userId = SecurityUtils.getUser().getId();
        //取完就删
        String  o = (String) redisTemplate.opsForHash().get(CacheConstants.BATCH_SAVE, userId.toString());
        CarOtherItemPageVO otherItem = JSONObject.parseObject(o, CarOtherItemPageVO.class);
        return R.ok(otherItem);
    }


    /**
     * 存档数据同步到报表
     *
     * @param item
     */
    private void addReport(CarOtherItem item) {
        LocalDateTime itemTime = item.getItemTime();
        Integer carId = item.getCarId();
        Integer etpId = item.getEtpId();
        Integer cost = item.getItemMoney();
        ReportExpense reportExpense = new ReportExpense();
        reportExpense.setCarId(carId);
        reportExpense.setEtpId(etpId);
        reportExpense.setMonthShort(itemTime);
        //停车 罚单 洗车  购买设备 其他
         Integer itemType = item.getItemType();
        if (itemType == ItemTypeConstant.STOP_CAR) {
            reportExpense.setStopCost(cost);
        }
        else if (itemType == ItemTypeConstant.TICKET) {
            reportExpense.setTicketCost(cost);
        }
        else if (itemType == ItemTypeConstant.WASH_CAR) {
            reportExpense.setWashCost(cost);
        }
        else if (itemType == ItemTypeConstant.SUPPLIES) {
            reportExpense.setSuppliesCost(cost);
        }
        else if (itemType == ItemTypeConstant.OTHER) {
            reportExpense.setOtherCost(cost);
        }
        carEtcItemService.saveReportExpense(reportExpense);
        Integer driverUserId = item.getDriverUserId();
        //添加到司机数据
        if (driverUserId != null) {
            ReportDriverExpense reportDriverExpense = new ReportDriverExpense();
            BeanUtil.copyProperties(reportExpense, reportDriverExpense);
            reportDriverExpense.setDriverUserId(driverUserId);
            carEtcItemService.saveReportDriverExpense(reportDriverExpense);
        }
    }

    private ApplyCostProcessDTO getDto(CarOtherItem item) {
        ApplyCostProcessDTO applyCostProcessDTO = new ApplyCostProcessDTO();
        applyCostProcessDTO.setCostId(item.getId());
        applyCostProcessDTO.setCarId(item.getCarId());
        applyCostProcessDTO.setCost(item.getItemMoney());
        applyCostProcessDTO.setCostTime(item.getItemTime());
        applyCostProcessDTO.setCostType(item.getItemType());
        return applyCostProcessDTO;
    }
}
