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
import com.htstar.ovms.enterprise.api.entity.CarAccidItem;
import com.htstar.ovms.enterprise.api.entity.CarMaiItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarAccidItemPageVO;
import com.htstar.ovms.enterprise.api.vo.CarMotItemPageVO;
import com.htstar.ovms.enterprise.mapper.CarAccidItemMapper;
import com.htstar.ovms.enterprise.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆事故信息
 *
 * @author lw
 * @date 2020-06-22 10:11:23
 */
@Service
@Slf4j
public class CarAccidItemServiceImpl extends ServiceImpl<CarAccidItemMapper, CarAccidItem> implements CarAccidItemService {
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private CarEtcItemService carEtcItemService;
    @Autowired
    private ApplyCostVerifyNodeService applyCostVerifyNodeService;
    @Autowired
    private ApplyCostProcessRecordService applyCostProcessRecordService;


    /**
     * 新增
     *
     * @param carAccidItem
     * @return
     */
    @Override
    public R saveInfo(CarAccidItem carAccidItem) {
        if (carAccidItem.getCarId() == null) {
            log.info("请选择车辆信息");
        }
        if (carAccidItem.getAccidTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("事故时间不得晚于当前时间");
        }
        if (carAccidItem.getId() != null) {
            return this.updateAccidById(carAccidItem);
        }

        //是否需要审批
        Boolean isApply = applyCostVerifyNodeService.isNeedVerify();
        Integer itemStatus = carAccidItem.getItemStatus();
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
        OvmsUser user = SecurityUtils.getUser();
        carAccidItem.setUserId(user.getId());
        carAccidItem.setEtpId(user.getEtpId());
        carAccidItem.setItemStatus(itemStatus);
        baseMapper.insert(carAccidItem);
        //待提交 需申请同步
        if (itemStatus == CarItemStatusConstant.WAIT_CHECK) {
            ApplyCostProcessDTO dto = this.getDto(carAccidItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
        }
        return R.ok("保存成功");
    }

    /**
     * 批量删除
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
            CarAccidItem carAccidItem = baseMapper.selectOne(new QueryWrapper<CarAccidItem>().eq("id", id));
            if (carAccidItem.getItemStatus() == 2) {
                continue;
            }
            carAccidItem.setDelFlag(1);
            baseMapper.updateById(carAccidItem);
            applyCostProcessRecordService.delByCostIdAndCostType(id,ItemTypeConstant.ACCID );
        }
        return R.ok("删除成功");
    }

    /**
     * 修改
     *Method breakpoints may dramatically slow down debuggin
     * @param carAccidItem
     * @return
     */
    @Override
    public R updateAccidById(CarAccidItem carAccidItem) {
        if (carAccidItem.getItemStatus() == 2) {
            return R.failed("存档数据不可修改");
        }
        if (carAccidItem.getAccidTime().compareTo(OvmDateUtil.getCstNow()) > 0) {
            return R.failed("存档时间不得晚于当前时间");
        }
        CarAccidItem accidItem = baseMapper.selectOne(new QueryWrapper<CarAccidItem>().eq("id", carAccidItem.getId()));
        Integer oldStatus = accidItem.getItemStatus();
        Integer itemStatus = carAccidItem.getItemStatus();
        if (oldStatus == CarItemStatusConstant.ARCHIVED) {
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
        if (oldStatus == CarItemStatusConstant.WAIT_CHECK) {
            Boolean updateItem = applyCostVerifyNodeService.isUpdateItem();
            if (!updateItem) {
                return R.failed("提交失败,已提交数据只有审批人可以修改");
            }
        }
        baseMapper.updateById(carAccidItem);
        //非保存的数据 都要重新修改审核数据
        if ((oldStatus==CarItemStatusConstant.WAIT_SUBMIT&&itemStatus==CarItemStatusConstant.WAIT_CHECK)
                || (oldStatus==CarItemStatusConstant.WAIT_CHECK&&itemStatus==CarItemStatusConstant.WAIT_CHECK)) {
            ApplyCostProcessDTO dto = this.getDto(accidItem);
            carEtcItemService.addOrUpdateCostProcessRecord(dto);
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
    public IPage<CarAccidItemPageVO> queryPage(CarItemPageReq carItemPageReq) {
        CarItemPageReq pageReqByRole = carInfoService.getPageReqByRole(carItemPageReq);
        IPage<CarAccidItemPageVO> carAccidItemPageVoIPage = baseMapper.queryPage(pageReqByRole);
        return carAccidItemPageVoIPage;


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
        List<CarAccidItemPageVO> carAccidItemPageVos = baseMapper.exportExcel(req);

        if (carAccidItemPageVos.size() > 0) {
            for (CarAccidItemPageVO carAccidItemPageVo : carAccidItemPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", carAccidItemPageVo.getLicCode());
                Integer itemStatus = carAccidItemPageVo.getItemStatus();
                String status = "";
                if (itemStatus == 1) {
                    status = "待审核";
                } else if (itemStatus == 2) {
                    status = "已存档";
                } else if (itemStatus == 3) {
                    status = "已退回";
                }
                map.put("状态", status);
                map.put("录入人", carAccidItemPageVo.getUsername());
                map.put("创建时间", carAccidItemPageVo.getCreateTime());
                map.put("事故时间", DateUtil.format(carAccidItemPageVo.getAccidTime(), "yyyy-MM-dd"));
                map.put("驾驶人", carAccidItemPageVo.getDriver());
                map.put("事故地点", carAccidItemPageVo.getAccidAddr());
                Integer accidType = carAccidItemPageVo.getAccidType();
                String nature = "";
                if (accidType == 0) {
                    nature = "一般事故";
                } else if (accidType == 1) {
                    nature = "重大事故";
                } else if (accidType == 2) {
                    nature = "特大事故";
                }
                map.put("事故性质", nature);
                Integer accidDuty = carAccidItemPageVo.getAccidDuty();
                String duty = "";
                if (accidDuty == 0) {
                    duty = "全部责任";
                } else if (accidDuty == 1) {
                    duty = "主要责任";
                } else if (accidDuty == 2) {
                    duty = "同等责任";
                } else if (accidDuty == 3) {
                    duty = "次要责任";
                } else if (accidDuty == 4) {
                    duty = "无责任";
                }
                map.put("事故责任", duty);
                map.put("车损情况", carAccidItemPageVo.getRemark());
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows, "事故记录");
    }

    /**
     * 存档
     *
     * @param id
     * @param itemStatus
     * @return
     */
    @Override
    public R filing(Integer id, Integer itemStatus) {
        if (itemStatus == 2) {
            return R.ok("存档成功");
        }
        CarAccidItem carAccidItem = baseMapper.selectById(id);
        carAccidItem.setItemStatus(2);
        if (baseMapper.updateById(carAccidItem) > 0) {
            return R.ok("存档成功");
        }
        return R.failed("存档失败");
    }

    @Override
    public CarAccidItemPageVO getItemById(Integer id) {
        return baseMapper.getItemById(id);
    }

    @Override
    public CarAccidItemPageVO getItemByUser() {
        Integer userId = SecurityUtils.getUser().getId();
        return baseMapper.getItemByUser(userId);
    }

    /**
     * 退回
     * @param id
     * @param remark
     * @return
     */
    @Override
    public R withdraw(Integer id, String remark) {
        CarAccidItem item = baseMapper.selectOne(new QueryWrapper<CarAccidItem>().eq("id", id));
        item.setItemStatus(CarItemStatusConstant.WITHDRAW);
        item.setApplyRemark(remark);
        baseMapper.updateById(item);
        return R.ok();
    }

    private ApplyCostProcessDTO getDto(CarAccidItem item) {
        ApplyCostProcessDTO applyCostProcessDTO = new ApplyCostProcessDTO();
        applyCostProcessDTO.setCostId(item.getId());
        applyCostProcessDTO.setCarId(item.getCarId());
        applyCostProcessDTO.setCostTime(item.getAccidTime());
        applyCostProcessDTO.setCostType(ItemTypeConstant.ACCID);
        return applyCostProcessDTO;
    }
}
