package com.htstar.ovms.report.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.core.util.WebUtils;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.report.api.entity.ReportExpense;
import com.htstar.ovms.report.api.req.CarReportReq;
import com.htstar.ovms.report.api.req.CostMonthReq;
import com.htstar.ovms.report.mapper.ReportExpenseMapper;
import com.htstar.ovms.report.service.ReportExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * 费用报表数据
 *
 * @author lw
 * @date 2020-07-27 15:19:34
 */
@Service
@Slf4j
public class ReportExpenseServiceImpl extends ServiceImpl<ReportExpenseMapper, ReportExpense> implements ReportExpenseService {

    /**
     * 月份统计费用总计费用
     *
     * @param req
     * @return
     */
    @Override
    public R<ReportExpense> getTotalCostByMonth(CostMonthReq req) {
        CostMonthReq req1 = getReq(req);
        ReportExpense reportExpense = baseMapper.getTotalCostByMonth(req1);
        return R.ok(reportExpense);
    }

    /**
     * 按月统计列表
     *
     * @param req
     * @return
     */
    @Override
    public R<List<ReportExpense>> getCostTableByMonth(CostMonthReq req) {
        CostMonthReq req1 = getReq(req);
        if (req.getExportStatus() == 1) {
           return this.exportCostTableByMont(req1);
        }
        List<ReportExpense> list = baseMapper.getCostTableByMonth(req1);
        return R.ok(list);
    }

    /**
     * 按车辆统计费用  当月总费用
     *
     * @param req
     * @return
     */
    @Override
    public R<ReportExpense> getTotalCostByCar(CarReportReq req) {
        String monthShort = this.getMonthShort(req);
        Integer etpId = SecurityUtils.getUser().getEtpId();
        ReportExpense reportExpense = baseMapper.getTotalCostByCar(etpId, monthShort);

        return R.ok(reportExpense);
    }

    /**
     * 按车辆统计费用列表
     *
     * @param req
     * @return
     */
    @Override
    public R<IPage<ReportExpense>> getCostPageByCar(CarReportReq req) {
        //时间
        String monthShort = this.getMonthShort(req);
        Integer etpId = SecurityUtils.getUser().getEtpId();
        req.setMonthShort(monthShort);
        req.setEtpId(etpId);
        if (req.getExportStatus()==1){
            return this.exportCostByCar(req);
        }
        IPage<ReportExpense> costTableByCar = baseMapper.getCostPageByCar(req);
        return R.ok(costTableByCar);
    }

    /**
     * 查询年月份处理
     *
     * @param req
     * @return
     */
    @Override
    public String getMonthShort(CarReportReq req) {
        String year = req.getYear();
        String month = req.getMonth();
        //年份
        if (StrUtil.isEmpty(year)) {
            LocalDateTime cstNow = OvmDateUtil.getCstNow();
            year = cstNow.format(DateTimeFormatter.ofPattern("yyyy"));
        }
        if (StrUtil.isEmpty(month)) {
            LocalDateTime cstNow = OvmDateUtil.getCstNow();
            //获得月的部分
            month = cstNow.format(DateTimeFormatter.ofPattern("MM"));
        }
        String monthShort = year + "-" + month;
        return monthShort;
    }

    /**
     * 导出
     *
     * @param rows
     * @param fileName
     */
    @Override
    public void exportUtil(List<Map<String, Object>> rows, String fileName) {
        HttpServletResponse response = WebUtils.getResponse();
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        // 一次性写出内容，使用默认样式，强制输出标题
        //writer.merge(rows.size() - 1, fileName);
        writer.write(rows, true);
        //out为OutputStream，需要写出到的目标流

        //response为HttpServletResponse对象

        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            out = response.getOutputStream();
        } catch (Exception e) {
            log.error("{}导出Excel异常", fileName);
        }
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    private CostMonthReq getReq(CostMonthReq req) {
        String year = req.getYear();
        if (year == null) {
            LocalDateTime cstNow = OvmDateUtil.getCstNow();
            //获得当前时间年的部分
            year = cstNow.format(DateTimeFormatter.ofPattern("yyyy"));
            req.setYear(year);
        }
        Integer etpId = SecurityUtils.getUser().getEtpId();
        req.setEtpId(etpId);
        return req;
    }

    private R<List<ReportExpense>> exportCostTableByMont(CostMonthReq req) {
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        List<ReportExpense> list = baseMapper.getCostTableByMonth(req);

        if (CollUtil.isNotEmpty(list)) {
            for (ReportExpense reportExpense : list) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("月份", reportExpense.getMonthValue());
                map.put("加油费", roundStr(((double) (reportExpense.getFuelCost()) / 100), 2));
                map.put("通行费", roundStr(((double) (reportExpense.getEtcCost()) / 100), 2));
                map.put("停车费", roundStr(((double) (reportExpense.getStopCost()) / 100), 2));
                map.put("洗车美容费", roundStr(((double) (reportExpense.getWashCost()) / 100), 2));
                map.put("罚单费", roundStr(((double) (reportExpense.getTicketCost()) / 100), 2));
                map.put("汽车用品费", roundStr(((double) (reportExpense.getSuppliesCost()) / 100), 2));
                map.put("保养费", roundStr(((double) (reportExpense.getMaiCost()) / 100), 2));
                map.put("保险费", roundStr(((double) (reportExpense.getInsCost()) / 100), 2));
                map.put("年检费", roundStr(((double) (reportExpense.getMotCost()) / 100), 2));
                map.put("维修费", roundStr(((double) (reportExpense.getRepairCost()) / 100), 2));
                map.put("其他费用", roundStr(((double) (reportExpense.getOtherCost()) / 100), 2));
                rows.add(map);
            }
        }
        this.exportUtil(rows, "按月统计费用");
        return R.ok();
    }

    /**
     * 按车辆统计费用
     * @param req
     * @return
     */
    private R exportCostByCar(CarReportReq req){
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        req.setSize(Long.MAX_VALUE);
        IPage<ReportExpense> costTableByCar = baseMapper.getCostPageByCar(req);
        List<ReportExpense> records = costTableByCar.getRecords();
        if (CollUtil.isNotEmpty(records)){
            for (ReportExpense reportExpense : records) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", reportExpense.getLicCode());
                map.put("加油费", roundStr(((double) (reportExpense.getFuelCost()) / 100), 2));
                map.put("通行费", roundStr(((double) (reportExpense.getEtcCost()) / 100), 2));
                map.put("停车费", roundStr(((double) (reportExpense.getStopCost()) / 100), 2));
                map.put("洗车美容费", roundStr(((double) (reportExpense.getWashCost()) / 100), 2));
                map.put("罚单费", roundStr(((double) (reportExpense.getTicketCost()) / 100), 2));
                map.put("汽车用品费", roundStr(((double) (reportExpense.getSuppliesCost()) / 100), 2));
                map.put("保养费", roundStr(((double) (reportExpense.getMaiCost()) / 100), 2));
                map.put("保险费", roundStr(((double) (reportExpense.getInsCost()) / 100), 2));
                map.put("年检费", roundStr(((double) (reportExpense.getMotCost()) / 100), 2));
                map.put("维修费", roundStr(((double) (reportExpense.getRepairCost()) / 100), 2));
                map.put("其他费用", roundStr(((double) (reportExpense.getOtherCost()) / 100), 2));
                rows.add(map);
            }
        }
        this.exportUtil(rows,"按车辆统计费用" );
        return R.ok();
    }
}
