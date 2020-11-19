package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.CarItemStatusConstant;
import com.htstar.ovms.enterprise.api.constant.ItemTypeConstant;
import com.htstar.ovms.enterprise.api.dto.ApplyCostProcessDTO;
import com.htstar.ovms.enterprise.api.entity.CarMotItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarMotItemPageVO;
import com.htstar.ovms.enterprise.api.vo.CarOtherItemPageVO;
import com.htstar.ovms.enterprise.mapper.CarMotItemMapper;
import com.htstar.ovms.enterprise.service.*;
import com.htstar.ovms.report.api.entity.ReportExpense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * 公车年检表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Service
public class CarMotItemServiceImpl extends ServiceImpl<CarMotItemMapper, CarMotItem> implements CarMotItemService {
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private CarEtcItemService carEtcItemService;
    @Autowired
    private ApplyCostVerifyNodeService applyCostVerifyNodeService;
    @Autowired
    private ApplyCostProcessRecordService applyCostProcessRecordService;

    /**
     * 年检信息分页
     *
     * @param
     * @param carItemPageReq
     * @return
     */
    @Override
    public IPage<CarMotItemPageVO> queryPage(CarItemPageReq carItemPageReq) {
        CarItemPageReq pageReqByRole = carInfoService.getPageReqByRole(carItemPageReq);
        return baseMapper.queryPage(pageReqByRole);
    }

    /**
     * 新增
     *
     * @param carMotItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveInfo(CarMotItem carMotItem) {
        if (carMotItem.getCarId() == null) {
            return R.failed("请选择车辆信息");
        }
        if (carMotItem.getId() != null) {
            return this.update(carMotItem);
        }
        //年检时间
        LocalDateTime motTime = carMotItem.getMotTime();
        if (motTime.compareTo(LocalDateTime.now()) > 0) {
            return R.failed("年检时间不得晚于当前时间");
        }
        if (motTime.compareTo(carMotItem.getNextTime()) > 0) {
            return R.failed("年检时间不得晚于下次年检时间");
        }
        if (carMotItem.getMotMoney() < 0) {
            return R.failed("请输入正确的金额");
        }
        Integer itemStatus = carMotItem.getItemStatus();
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
        //获取企业Id
        OvmsUser user = SecurityUtils.getUser();
        carMotItem.setEtpId(user.getEtpId());
        carMotItem.setUserId(user.getId());
        carMotItem.setItemStatus(itemStatus);
        baseMapper.insert(carMotItem);
        //同步到报表
        if (carMotItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
            this.addReport(carMotItem);
        }
        //待提交 同步到审核
        if (itemStatus == CarItemStatusConstant.WAIT_CHECK) {
            ApplyCostProcessDTO dto = this.getDto(carMotItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("提交成功");

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
            CarMotItem carMotItem = baseMapper.selectOne(new QueryWrapper<CarMotItem>().eq("id", id));
            //存档数据不可删除
            if (carMotItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
                continue;
            }
            carMotItem.setDelFlag(1);
            baseMapper.updateById(carMotItem);
            applyCostProcessRecordService.delByCostIdAndCostType(id,ItemTypeConstant.MOT );
        }
        return R.ok("删除成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(CarMotItem carMotItem) {
        if (carMotItem.getMotTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("年检不得晚于当前时间");
        }
        if (carMotItem.getMotTime().compareTo(carMotItem.getNextTime()) > 0) {
            return R.failed("年检时间不得晚于下次年检时间");
        }
        if (carMotItem.getMotMoney() < 0) {
            return R.failed("不能有负数");
        }

        CarMotItem motItem = baseMapper.selectOne(new QueryWrapper<CarMotItem>()
                .eq("id", carMotItem.getId()));
        Integer oldItemStatus = motItem.getItemStatus();
        Integer itemStatus = carMotItem.getItemStatus();

        if ( oldItemStatus== CarItemStatusConstant.ARCHIVED) {
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
        baseMapper.updateById(carMotItem);
        if (itemStatus == CarItemStatusConstant.ARCHIVED) {
            this.addReport(carMotItem);
        }
        //非保存的数据 都要重新修改审核数据
        if ((oldItemStatus==CarItemStatusConstant.WAIT_SUBMIT&&itemStatus==CarItemStatusConstant.WAIT_CHECK)
                || (oldItemStatus==CarItemStatusConstant.WAIT_CHECK&&itemStatus==CarItemStatusConstant.WAIT_CHECK)) {
            ApplyCostProcessDTO dto = this.getDto(carMotItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("修改成功");
}

    @Override
    public void exportExcel(ExportReq req) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        req.setEtpId(etpId);
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        //excel行数据
        List<CarMotItemPageVO> carMotItemPageVos = baseMapper.exportExcel(req);
        if (carMotItemPageVos.size() > 0) {
            for (CarMotItemPageVO carMotItemPageVo : carMotItemPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", carMotItemPageVo.getLicCode());
                Integer itemStatus = carMotItemPageVo.getItemStatus();
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
                map.put("录入人", carMotItemPageVo.getUsername());
                map.put("创建时间", carMotItemPageVo.getCreateTime());
                map.put("年检时间", DateUtil.format(carMotItemPageVo.getMotTime(), "yyyy-MM-dd"));
                map.put("下次年检日期", DateUtil.format(carMotItemPageVo.getNextTime(), "yyyy-MM-dd"));
                map.put("年检地点", carMotItemPageVo.getAddress());
                map.put("年检费用（元）", roundStr(((double) (carMotItemPageVo.getMotMoney()) / 100), 2));
                map.put("送检人", carMotItemPageVo.getMotAdmin());
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows, "年检信息");
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
        CarMotItem carMotItem = baseMapper.selectById(id);
        carMotItem.setItemStatus(CarItemStatusConstant.ARCHIVED);
        //存档数据保存到报表
        this.addReport(carMotItem);
        baseMapper.updateById(carMotItem);
        return R.ok("存档成功");

    }

    /**
     * 企业年检过期车辆
     *
     * @param etpId
     * @param date
     * @return
     */
    @Override
    public Integer expiredNumByEtp(Integer etpId, LocalDate date) {
        return baseMapper.expiredNumByEtp(etpId, date);
    }

    @Override
    public CarMotItemPageVO getItemById(Integer id) {
        return baseMapper.getItemById(id);
    }

    @Override
    public CarMotItemPageVO getItemByUser() {
        Integer userId = SecurityUtils.getUser().getId();
        return baseMapper.getItemByUser(userId);
    }

    @Override
    public R withdraw(Integer id, String remark) {
        CarMotItem item = baseMapper.selectOne(new QueryWrapper<CarMotItem>().eq("id", id));
        item.setItemStatus(CarItemStatusConstant.WITHDRAW);
        item.setApplyRemark(remark);
        baseMapper.updateById(item);
        return R.ok();
    }

    /**
     * 存档数据同步到报表
     *
     * @param item
     */
    private void addReport(CarMotItem item) {
        LocalDateTime itemTime = item.getMotTime();
        Integer carId = item.getCarId();
        Integer etpId = item.getEtpId();
        Integer cost = item.getMotMoney();
        ReportExpense reportExpense = new ReportExpense();
        reportExpense.setCarId(carId);
        reportExpense.setEtpId(etpId);
        reportExpense.setMotCost(cost);
        reportExpense.setMonthShort(itemTime);
        carEtcItemService.saveReportExpense(reportExpense);
    }

    private ApplyCostProcessDTO getDto(CarMotItem item){
        ApplyCostProcessDTO applyCostProcessDTO = new ApplyCostProcessDTO();
        applyCostProcessDTO.setCostId(item.getId());
        applyCostProcessDTO.setCarId(item.getCarId());
        applyCostProcessDTO.setCost(item.getMotMoney());
        applyCostProcessDTO.setCostTime(item.getMotTime());
        applyCostProcessDTO.setCostType(ItemTypeConstant.MOT);
        return applyCostProcessDTO;
    }
}
