package com.htstar.ovms.report.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.report.api.entity.ReportDriverExpense;
import com.htstar.ovms.report.api.entity.ReportExpense;
import com.htstar.ovms.report.api.req.CarReportReq;
import com.htstar.ovms.report.mapper.ReportDriverExpenseMapper;
import com.htstar.ovms.report.service.ReportDriverExpenseService;
import com.htstar.ovms.report.service.ReportExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * 司机费用

 *
 * @author lw
 * @date 2020-08-01 10:11:28
 */
@Service
@Slf4j
public class ReportDriverExpenseServiceImpl extends ServiceImpl<ReportDriverExpenseMapper, ReportDriverExpense> implements ReportDriverExpenseService {
    @Autowired
    private ReportExpenseService reportExpenseService;
    /**
     * 按司机统计费用 计算总费用
     * @param carReportReq
     * @return
     */
    @Override
    public ReportDriverExpense getTotalCostByDriver(CarReportReq carReportReq) {
        String monthShort = reportExpenseService.getMonthShort(carReportReq);
        Integer etpId = SecurityUtils.getUser().getEtpId();
        ReportDriverExpense reportDriverExpense= baseMapper.getTotalCostByDriver(monthShort,etpId);
        return reportDriverExpense;
    }

    /**
     * 按司机统计费用 分页
     * @param carReportReq
     * @return
     */
    @Override
    public R<IPage<ReportDriverExpense>>  getCostPageByDriver(CarReportReq carReportReq) {
        String monthShort = reportExpenseService.getMonthShort(carReportReq);
        Integer etpId = SecurityUtils.getUser().getEtpId();
        carReportReq.setMonthShort(monthShort);
        carReportReq.setEtpId(etpId);
        if (carReportReq.getExportStatus()==1){
            return this.exportCostByDrive(carReportReq);
        }
        return R.ok(baseMapper.getCostPageByDriver(carReportReq));
    }

    private R exportCostByDrive(CarReportReq carReportReq){
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        carReportReq.setSize(Long.MAX_VALUE);
        IPage<ReportDriverExpense> costPageByDriver = baseMapper.getCostPageByDriver(carReportReq);
        List<ReportDriverExpense> records = costPageByDriver.getRecords();
        if (CollUtil.isNotEmpty(records)){
            for (ReportDriverExpense reportExpense : records) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", reportExpense.getDriverName());
                map.put("加油费", roundStr(((double) (reportExpense.getFuelCost()) / 100), 2));
                map.put("通行费", roundStr(((double) (reportExpense.getEtcCost()) / 100), 2));
                map.put("停车费", roundStr(((double) (reportExpense.getStopCost()) / 100), 2));
                map.put("洗车美容费", roundStr(((double) (reportExpense.getWashCost()) / 100), 2));
                map.put("罚单费", roundStr(((double) (reportExpense.getTicketCost()) / 100), 2));
                map.put("汽车用品费", roundStr(((double) (reportExpense.getSuppliesCost()) / 100), 2));
                map.put("其他费用", roundStr(((double) (reportExpense.getOtherCost()) / 100), 2));
                rows.add(map);
            }
        }
        reportExpenseService.exportUtil(rows,"按司机统计费用" );
        return R.ok();
    }
}
