package com.htstar.ovms.enterprise.service.impl;

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
import com.htstar.ovms.enterprise.api.entity.CarRepairItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarOtherItemPageVO;
import com.htstar.ovms.enterprise.api.vo.CarRepairItemPageVO;
import com.htstar.ovms.enterprise.mapper.CarRepairItemMapper;
import com.htstar.ovms.enterprise.service.*;
import com.htstar.ovms.report.api.entity.ReportExpense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * 公车维修表
 *
 * @author lw
 * @date 2020-06-08 13:48:46
 */
@Service
public class CarRepairItemServiceImpl extends ServiceImpl<CarRepairItemMapper, CarRepairItem> implements CarRepairItemService {
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private CarEtcItemService carEtcItemService;
    @Autowired
    private ApplyCostVerifyNodeService applyCostVerifyNodeService;
    @Autowired
    private ApplyCostProcessRecordService applyCostProcessRecordService;

    /**
     * 维修信息分页
     *
     * @param carItemPageReq
     * @return
     */
    @Override
    public IPage<CarRepairItemPageVO> queryPage(CarItemPageReq carItemPageReq) {
        CarItemPageReq pageReqByRole = carInfoService.getPageReqByRole(carItemPageReq);
        return baseMapper.queryPage(pageReqByRole);
    }

    /**
     * 保存维修信息
     *
     * @param carRepairItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveInfo(CarRepairItem carRepairItem) {
        if (carRepairItem.getCarId() == null) {
            return R.failed("请选择车辆信息");
        }
        LocalDateTime repTime = carRepairItem.getRepTime();
        if (repTime==null){
            return R.failed("请选择维修日期");
        }
        if (repTime.compareTo(LocalDateTime.now()) > 0) {
            return R.failed("维修时间不得晚于当前时间");
        }
        if (carRepairItem.getRepMoney() < 0 || carRepairItem.getPartsMoney() < 0) {
            return R.failed("请输入正确的金额");
        }
        if (carRepairItem.getId() != null) {
            return this.update(carRepairItem);
        }
        Integer itemStatus = carRepairItem.getItemStatus();
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
                return R.failed("保存失败,需审核");
            }
            if (!isApply && itemStatus == CarItemStatusConstant.WAIT_CHECK) {
                return R.failed("保存失败,无需审核");
            }
        }
        //获取企业id
        OvmsUser user = SecurityUtils.getUser();
        carRepairItem.setEtpId(user.getEtpId());
        carRepairItem.setUserId(user.getId());
        carRepairItem.setItemStatus(itemStatus);
        baseMapper.insert(carRepairItem);
        //存档数据直接添加到报表
        if (carRepairItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
            this.addReport(carRepairItem);
        }
        //待提交 需同步
        if (itemStatus == CarItemStatusConstant.WAIT_CHECK) {
            ApplyCostProcessDTO dto = this.getDto(carRepairItem);
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
            CarRepairItem carRepairItem = baseMapper.selectOne(new QueryWrapper<CarRepairItem>().eq("id", id));
            //存档数据不能删除
            if (carRepairItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
                continue;
            }
            carRepairItem.setDelFlag(1);
            baseMapper.updateById(carRepairItem);
            applyCostProcessRecordService.delByCostIdAndCostType(id,ItemTypeConstant.REPAIR );
        }
        return R.ok("删除成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(CarRepairItem carRepairItem) {
        if (carRepairItem.getRepTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("维修时间不得晚于当前时间");
        }
        if (carRepairItem.getRepMoney() < 0 || carRepairItem.getPartsMoney() < 0) {
            return R.failed("请输入正确的金额");
        }
        CarRepairItem repairItem = baseMapper.selectOne(new QueryWrapper<CarRepairItem>()
                .eq("id", carRepairItem.getId()));
        Integer oldItemStatus = repairItem.getItemStatus();
        Integer itemStatus = carRepairItem.getItemStatus();
        if (oldItemStatus == CarItemStatusConstant.ARCHIVED) {
            return R.failed("该数据已存档，不可修改");
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
        baseMapper.updateById(carRepairItem);
        //存档数据直接添加到报表
        if (carRepairItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
            this.addReport(carRepairItem);
        }
        //非保存的数据 都要重新修改审核数据
        if ((oldItemStatus == CarItemStatusConstant.WAIT_SUBMIT && itemStatus == CarItemStatusConstant.WAIT_CHECK)
                || (oldItemStatus == CarItemStatusConstant.WAIT_CHECK && itemStatus == CarItemStatusConstant.WAIT_CHECK)) {
            ApplyCostProcessDTO dto = this.getDto(carRepairItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("提交成功");
    }

    /**
     * 导出excel
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
        List<CarRepairItemPageVO> carRepairItemPageVos = baseMapper.exportExcel(req);
        if (carRepairItemPageVos.size() > 0) {
            for (CarRepairItemPageVO carRepairItemPageVo : carRepairItemPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", carRepairItemPageVo.getLicCode());
                Integer itemStatus = carRepairItemPageVo.getItemStatus();
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
                map.put("录入人", carRepairItemPageVo.getUsername());
                map.put("创建时间", carRepairItemPageVo.getCreateTime());
                map.put("维修时间", carRepairItemPageVo.getRepTime());
                map.put("保修人", carRepairItemPageVo.getReportPeople());
                map.put("送修人", carRepairItemPageVo.getSendPeople());
                map.put("承修单位", carRepairItemPageVo.getRepairEtp());
                map.put("维修费用(元)", roundStr(((double) (carRepairItemPageVo.getRepMoney()) / 100), 2));
                map.put("配件费用(元)", roundStr(((double) (carRepairItemPageVo.getPartsMoney()) / 100), 2));
                map.put("维修项目", carRepairItemPageVo.getRemark());
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows, "车辆维修记录");
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
        CarRepairItem carRepairItem = baseMapper.selectById(id);
        carRepairItem.setItemStatus(CarItemStatusConstant.ARCHIVED);
        this.addReport(carRepairItem);
        baseMapper.updateById(carRepairItem);
        return R.ok("存档成功");
    }

    /**
     * 申请退回
     * @param id
     * @param remark
     * @return
     */
    @Override
    public R withdraw(Integer id, String remark) {
        CarRepairItem item = baseMapper.selectOne(new QueryWrapper<CarRepairItem>().eq("id", id));
        item.setItemStatus(CarItemStatusConstant.WITHDRAW);
        item.setApplyRemark(remark);
        baseMapper.updateById(item);
        return R.ok();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public CarRepairItemPageVO getItemById(Integer id) {
        return baseMapper.getItemById(id);
    }

    /**
     * 获取用户保存的数据
     * @return
     */
    @Override
    public CarRepairItemPageVO getItemByUser() {
        Integer userId = SecurityUtils.getUser().getId();
        return baseMapper.getItemByUser(userId);
    }

    /**
     * 存档数据同步到报表
     *
     * @param item
     */
    private void addReport(CarRepairItem item) {
        LocalDateTime itemTime = item.getRepTime();
        Integer carId = item.getCarId();
        Integer etpId = item.getEtpId();
        Integer cost = item.getRepMoney();
        ReportExpense reportExpense = new ReportExpense();
        reportExpense.setCarId(carId);
        reportExpense.setEtpId(etpId);
        reportExpense.setRepairCost(cost);
        reportExpense.setMonthShort(itemTime);
        carEtcItemService.saveReportExpense(reportExpense);
    }

    private ApplyCostProcessDTO getDto(CarRepairItem item){
        ApplyCostProcessDTO applyCostProcessDTO = new ApplyCostProcessDTO();
        applyCostProcessDTO.setCostId(item.getId());
        applyCostProcessDTO.setCarId(item.getCarId());
        applyCostProcessDTO.setCost(item.getRepMoney());
        applyCostProcessDTO.setCostTime(item.getRepTime());
        applyCostProcessDTO.setCostType(ItemTypeConstant.REPAIR);
        return applyCostProcessDTO;
    }


}
