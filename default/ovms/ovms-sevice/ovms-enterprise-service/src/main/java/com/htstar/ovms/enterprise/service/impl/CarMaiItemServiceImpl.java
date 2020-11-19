package com.htstar.ovms.enterprise.service.impl;

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
import com.htstar.ovms.enterprise.api.constant.ItemTypeConstant;
import com.htstar.ovms.enterprise.api.dto.ApplyCostProcessDTO;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.entity.CarMaiItem;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarMaiItemPageVO;
import com.htstar.ovms.enterprise.api.vo.CarOtherItemPageVO;
import com.htstar.ovms.enterprise.mapper.CarMaiItemMapper;
import com.htstar.ovms.enterprise.service.*;
import com.htstar.ovms.report.api.entity.ReportExpense;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static cn.hutool.core.util.NumberUtil.roundStr;


/**
 * 公车保养表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Service
@Slf4j
public class CarMaiItemServiceImpl extends ServiceImpl<CarMaiItemMapper, CarMaiItem> implements CarMaiItemService {

    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private CarEtcItemService carEtcItemService;
    @Autowired
    private ApplyCostVerifyNodeService applyCostVerifyNodeService;
    @Autowired
    private ApplyCostProcessRecordService applyCostProcessRecordService;

    @Override
    public IPage<CarMaiItemPageVO> queryPage(CarItemPageReq carItemPageReq) {
        CarItemPageReq pageReqByRole = carInfoService.getPageReqByRole(carItemPageReq);
        return baseMapper.queryPage(pageReqByRole);
    }

    /**
     * 保存保养信息
     *
     * @param carMaiItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveInfo(CarMaiItem carMaiItem) {
        if (carMaiItem.getCarId() == null) {
            return R.failed("请选择车辆信息");
        }
        if (carMaiItem.getId()!=null){
            return this.update(carMaiItem);
        }
        //
        LocalDateTime maiTime = carMaiItem.getMaiTime();
        LocalDateTime nextTime = carMaiItem.getNextTime();
        //当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        if (maiTime.compareTo(nextTime) > 0) {
            return R.failed("保养日期不得晚于下次日期");
        }
        if (maiTime.compareTo(localDateTime) > 0) {
            return R.failed("保养日期不得晚于当前时间");
        }
        if (carMaiItem.getMaiMileage() < 0 || carMaiItem.getIntervalMileage() < 0 || carMaiItem.getMaiMoney() < 0) {
            return R.failed("请输入正确的数字格式");
        }

        Integer itemStatus = carMaiItem.getItemStatus();
        //是否需要审批
        Boolean isApply = applyCostVerifyNodeService.isNeedVerify();
        //PC端不传
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
                return R.failed("保存失败,需审核");
            }
            if (!isApply && itemStatus == CarItemStatusConstant.WAIT_CHECK) {
                return R.failed("保存失败,无需审核");
            }
        }
        //获取企业Id
        OvmsUser user = SecurityUtils.getUser();
        carMaiItem.setEtpId(user.getEtpId());
        carMaiItem.setUserId(user.getId());
        carMaiItem.setItemStatus(itemStatus);
        //存档数据直接添加到报表
        if (carMaiItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
            this.addReport(carMaiItem);
        }
        baseMapper.insert(carMaiItem);
        //待提交 需申请同步
        if (itemStatus == CarItemStatusConstant.WAIT_CHECK) {
            ApplyCostProcessDTO dto = this.getDto(carMaiItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("保存成功");

    }

    /**
     * 删除
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
            CarMaiItem carMaiItem = baseMapper.selectOne(new QueryWrapper<CarMaiItem>().eq("id", id));
            if (carMaiItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
                continue;
            }
            carMaiItem.setDelFlag(1);
            baseMapper.updateById(carMaiItem);
            applyCostProcessRecordService.delByCostIdAndCostType(id,ItemTypeConstant.MAI );
        }
        return R.ok("删除成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(CarMaiItem carMaiItem) {
        if (carMaiItem.getMaiTime().compareTo(carMaiItem.getNextTime()) > 0) {
            return R.failed("保养日期不得晚于下次保养日期");
        }
        if (carMaiItem.getMaiTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("保养日期不得晚于当前时间");
        }
        if (carMaiItem.getMaiMileage() < 0 || carMaiItem.getIntervalMileage() < 0 || carMaiItem.getMaiMoney() < 0) {
            return R.failed("请输入正确的数字格式");
        }

        CarMaiItem maiItem = baseMapper.selectOne(new QueryWrapper<CarMaiItem>().eq("id", carMaiItem.getId()));
        Integer oldItemStatus = maiItem.getItemStatus();
        Integer itemStatus = carMaiItem.getItemStatus();

        if (oldItemStatus == CarItemStatusConstant.ARCHIVED) {
            return R.failed("存档数据不可再提交");
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
        baseMapper.updateById(carMaiItem);
        //存档数据直接添加到报表
        if (carMaiItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
            this.addReport(carMaiItem);
        }
        //非保存的数据 都要重新修改审核数据
        if ((oldItemStatus==CarItemStatusConstant.WAIT_SUBMIT&&itemStatus==CarItemStatusConstant.WAIT_CHECK)
                || (oldItemStatus==CarItemStatusConstant.WAIT_CHECK&&itemStatus==CarItemStatusConstant.WAIT_CHECK)) {
            ApplyCostProcessDTO dto = this.getDto(carMaiItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("提交成功");

    }

    @Override
    public void exportExcel(ExportReq req) {
        log.info("导出请求数据为{}", req);
        Integer etpId = SecurityUtils.getUser().getEtpId();
        req.setEtpId(etpId);
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        //excel行数据
        List<CarMaiItemPageVO> carMaiItemPageVos = baseMapper.exportExcel(req);
        if (carMaiItemPageVos.size() > 0) {
            for (CarMaiItemPageVO carMaiItemPageVo : carMaiItemPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", carMaiItemPageVo.getLicCode());
                Integer itemStatus = carMaiItemPageVo.getItemStatus();
                String status = "";
                if (itemStatus == 1) {
                    status = "待审核";
                }
                else if (itemStatus == 2) {
                    status = "已存档";
                }
                else if (itemStatus == 2) {
                    status = "已退回";
                }
                map.put("状态", status);
                map.put("录入人", carMaiItemPageVo.getUsername());
                map.put("创建时间", carMaiItemPageVo.getCreateTime());
                map.put("保养日期", DateUtil.format(carMaiItemPageVo.getMaiTime(), "yyyy-MM-dd"));
                map.put("下次保养日期", DateUtil.format(carMaiItemPageVo.getNextTime(), "yyyy-MM-dd"));
                map.put("进保里程(公里)", carMaiItemPageVo.getMaiMileage());
                map.put("间隔里程(公里)", carMaiItemPageVo.getIntervalMileage());
                map.put("保养类别", carMaiItemPageVo.getMaiType());
                map.put("保养单位", carMaiItemPageVo.getMaiEtp());
                map.put("保养费用(元)", roundStr(((double) (carMaiItemPageVo.getMaiMoney()) / 100), 2));
                map.put("保养项目", carMaiItemPageVo.getMaiPro());
                map.put("保养人", carMaiItemPageVo.getMaiAdmin());
                rows.add(map);
            }
        }

        carInfoService.carExportUtil(rows, "保养信息");
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
        CarMaiItem carMaiItem = baseMapper.selectById(id);
        carMaiItem.setItemStatus(CarItemStatusConstant.ARCHIVED);
        this.addReport(carMaiItem);
        baseMapper.updateById(carMaiItem);
        return R.ok("存档成功");

    }

    /**
     * 车辆保养过期数
     *
     * @param etpId
     * @param date
     * @return
     */
    @Override
    public Integer expiredNumByEtp(Integer etpId, LocalDate date) {
        return baseMapper.expiredNumByEtp(etpId, date);
    }

    /**
     * 根据id获取
     * @param id
     * @return
     */
    @Override
    public CarMaiItemPageVO getItemById(Integer id) {
        return baseMapper.getItemById(id);
    }

    /**
     * 获取用户保存的
     * @return
     */
    @Override
    public CarMaiItemPageVO getItemByUser() {
        Integer userId = SecurityUtils.getUser().getId();
        return baseMapper.getItemByUser(userId);
    }

    @Override
    public R withdraw(Integer id, String remark) {
        CarMaiItem item = baseMapper.selectOne(new QueryWrapper<CarMaiItem>().eq("id", id));
        item.setItemStatus(CarItemStatusConstant.WITHDRAW);
        item.setApplyRemark(remark);
        baseMapper.updateById(item);
        return R.ok();
    }

    /**
     * 存档数据同步到报表
     *
     * @param carMaiItem
     */
    private void addReport(CarMaiItem carMaiItem) {
        LocalDateTime itemTime = carMaiItem.getMaiTime();
        Integer carId = carMaiItem.getCarId();
        Integer etpId = carMaiItem.getEtpId();
        Integer cost = carMaiItem.getMaiMoney();
        ReportExpense reportExpense = new ReportExpense();
        reportExpense.setCarId(carId);
        reportExpense.setEtpId(etpId);
        reportExpense.setMaiCost(cost);
        reportExpense.setMonthShort(itemTime);
        carEtcItemService.saveReportExpense(reportExpense);
    }

    private ApplyCostProcessDTO getDto(CarMaiItem item){
        ApplyCostProcessDTO applyCostProcessDTO = new ApplyCostProcessDTO();
        applyCostProcessDTO.setCostId(item.getId());
        applyCostProcessDTO.setCarId(item.getCarId());
        applyCostProcessDTO.setCost(item.getMaiMoney());
        applyCostProcessDTO.setCostTime(item.getMaiTime());
        applyCostProcessDTO.setCostType(ItemTypeConstant.MAI);
        return applyCostProcessDTO;
    }
}
