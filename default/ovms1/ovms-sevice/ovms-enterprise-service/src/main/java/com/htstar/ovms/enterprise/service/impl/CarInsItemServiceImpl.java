package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.CarItemStatusConstant;
import com.htstar.ovms.enterprise.api.constant.ItemTypeConstant;
import com.htstar.ovms.enterprise.api.dto.ApplyCostProcessDTO;
import com.htstar.ovms.enterprise.api.entity.CarInsItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarInsItemPageVO;
import com.htstar.ovms.enterprise.api.vo.CarOtherItemPageVO;
import com.htstar.ovms.enterprise.mapper.CarInsItemMapper;
import com.htstar.ovms.enterprise.service.*;
import com.htstar.ovms.report.api.entity.ReportExpense;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * 公车保险表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Service
@Slf4j
public class CarInsItemServiceImpl extends ServiceImpl<CarInsItemMapper, CarInsItem> implements CarInsItemService {
    @Autowired
    private CarInfoService carInfoService;

    @Autowired
    private CarEtcItemService carEtcItemService;
    @Autowired
    private ApplyCostVerifyNodeService applyCostVerifyNodeService;
    private ApplyCostProcessRecordService applyCostProcessRecordService;


    /**
     * 保险信息分页
     *
     * @param
     * @return
     */
    @Override
    public IPage<CarInsItemPageVO> queryPage(CarItemPageReq carItemPageReq) {
        CarItemPageReq pageReqByRole = carInfoService.getPageReqByRole(carItemPageReq);
        return baseMapper.queryPage(pageReqByRole);
    }

    /**
     * 保存
     *
     * @param carInsItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveInfo(CarInsItem carInsItem) {
        if (carInsItem.getCarId() == null) {
            return R.failed("请选择车辆信息");
        }
        if (carInsItem.getId() != null) {
            return this.update(carInsItem);
        }
        //年检时间
        LocalDateTime startTime = carInsItem.getStartTime();
        LocalDateTime endTime = carInsItem.getEndTime();
        if (startTime.compareTo(endTime) > 0) {
            return R.failed("开始日期不得晚于到期日期");
        }
        if (carInsItem.getInsMoney() < 0) {
            return R.failed("请输入正确的金额");
        }
        Integer itemStatus = carInsItem.getItemStatus();
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
        carInsItem.setEtpId(user.getEtpId());
        carInsItem.setUserId(user.getId());
        carInsItem.setItemStatus(itemStatus);
        baseMapper.insert(carInsItem);
        //直接存档的数据
        if (itemStatus == CarItemStatusConstant.ARCHIVED) {
            this.addReport(carInsItem);
        }
        //待提交 需同步
        if (itemStatus == CarItemStatusConstant.WAIT_CHECK) {
            ApplyCostProcessDTO dto = this.getDto(carInsItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("保存成功");
    }

    /**
     * 根据id删除
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
            CarInsItem carInsItem = baseMapper.selectOne(new QueryWrapper<CarInsItem>().eq("id", id));
            if (carInsItem.getItemStatus() == CarItemStatusConstant.ARCHIVED) {
                continue;
            }
            carInsItem.setDelFlag(1);
            baseMapper.updateById(carInsItem);
            applyCostProcessRecordService.delByCostIdAndCostType(id,ItemTypeConstant.INS );
        }
        return R.ok("删除成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(CarInsItem carInsItem) {
        if (carInsItem.getStartTime().compareTo(carInsItem.getEndTime()) > 0) {
            return R.failed("开始日期不得晚于到期日期");
        }
        if (carInsItem.getInsMoney() < 0) {
            return R.failed("请输入正确的金额");
        }
        CarInsItem insItem = baseMapper.selectOne(new QueryWrapper<CarInsItem>().eq("id", carInsItem.getId()));
        Integer oldItemStatus = insItem.getItemStatus();
        Integer itemStatus = carInsItem.getItemStatus();
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
        baseMapper.updateById(carInsItem);
        //直接存档的数据
        if (itemStatus == CarItemStatusConstant.ARCHIVED) {
            this.addReport(carInsItem);
        }
        //非保存的数据 都要重新修改审核数据
        if ((oldItemStatus == CarItemStatusConstant.WAIT_SUBMIT && itemStatus == CarItemStatusConstant.WAIT_CHECK)
                || (oldItemStatus == CarItemStatusConstant.WAIT_CHECK && itemStatus == CarItemStatusConstant.WAIT_CHECK)) {
            ApplyCostProcessDTO dto = this.getDto(carInsItem);
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
        List<CarInsItemPageVO> carInsItemPageVos = baseMapper.exportExcel(req);
        if (carInsItemPageVos.size() > 0) {
            for (CarInsItemPageVO carInsItemPageVo : carInsItemPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", carInsItemPageVo.getLicCode());

                Integer itemStatus = carInsItemPageVo.getItemStatus();
                String status = "";
                if (itemStatus == 1) {
                    status = "待审核";
                } else if (itemStatus == 2) {
                    status = "已存档";
                } else if (itemStatus == 3) {
                    status = "已退回";
                }
                map.put("状态", status);
                map.put("录入人", carInsItemPageVo.getUsername());
                map.put("创建时间", carInsItemPageVo.getCreateTime());
                map.put("保险时间", DateUtil.format(carInsItemPageVo.getStartTime(), "yyyy-MM-dd"));
                map.put("到期时间", DateUtil.format(carInsItemPageVo.getEndTime(), "yyyy-MM-dd"));
                map.put("经办人", carInsItemPageVo.getOperator());
                map.put("受理单位", carInsItemPageVo.getInsCompany());
                Integer insType = carInsItemPageVo.getInsType();
                String type="";
                if (insType == 0) {
                    type = "交强险";
                } else if (insType == 1) {
                    type = "商业险";
                } else if (insType == 2) {
                    type = "货物保险";
                } else if (insType == 3) {
                    type = "司机保险";
                }
                map.put("保险类型", type);
                map.put("保险费用（元）", roundStr(((double) (carInsItemPageVo.getInsMoney()) / 100), 2));
                map.put("办理人", carInsItemPageVo.getTrustees());
                map.put("保险项目内容", carInsItemPageVo.getRemark());
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows, "车辆保险记录");
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
        CarInsItem carInsItem = baseMapper.selectById(id);
        carInsItem.setItemStatus(CarItemStatusConstant.ARCHIVED);
        //添加数据到报表
        this.addReport(carInsItem);
        baseMapper.updateById(carInsItem);
        return R.ok("存档成功");

    }

    /**
     * 企业保险过期数
     *
     * @param etpId
     * @return
     */
    @Override
    public Integer expiredNumByEtp(Integer etpId, LocalDate date) {
        return baseMapper.expiredNumByEtp(etpId, date);
    }

    /**
     * 根据id获取
     *
     * @param id
     * @return
     */
    @Override
    public CarInsItemPageVO getItemById(Integer id) {
        return baseMapper.getItemById(id);
    }

    /**
     * 获取用户保存的
     *
     * @return
     */
    @Override
    public CarInsItemPageVO getItemByUser() {
        Integer userId = SecurityUtils.getUser().getId();
        return baseMapper.getItemByUser(userId);
    }

    @Override
    public R withdraw(Integer id, String remark) {
        CarInsItem item = baseMapper.selectOne(new QueryWrapper<CarInsItem>().eq("id", id));
        item.setItemStatus(CarItemStatusConstant.WITHDRAW);
        item.setApplyRemark(remark);
        baseMapper.updateById(item);
        return R.ok();
    }


    /**
     * 存档数据同步到报表
     *
     * @param carInsItem
     */
    private void addReport(CarInsItem carInsItem) {
        //加油时间
        LocalDateTime itemTime = carInsItem.getStartTime();
        Integer carId = carInsItem.getCarId();
        Integer etpId = carInsItem.getEtpId();
        Integer cost = carInsItem.getInsMoney();
        ReportExpense reportExpense = new ReportExpense();
        reportExpense.setCarId(carId);
        reportExpense.setEtpId(etpId);
        reportExpense.setInsCost(cost);
        reportExpense.setMonthShort(itemTime);
        carEtcItemService.saveReportExpense(reportExpense);
    }


    private ApplyCostProcessDTO getDto(CarInsItem item) {
        ApplyCostProcessDTO applyCostProcessDTO = new ApplyCostProcessDTO();
        applyCostProcessDTO.setCostId(item.getId());
        applyCostProcessDTO.setCarId(item.getCarId());
        applyCostProcessDTO.setCost(item.getInsMoney());
        applyCostProcessDTO.setCostTime(item.getStartTime());
        applyCostProcessDTO.setCostType(ItemTypeConstant.INS);
        return applyCostProcessDTO;
    }
}