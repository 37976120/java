package com.htstar.ovms.report.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.core.util.WebUtils;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.ProcessTypeConstant;
import com.htstar.ovms.report.api.req.*;
import com.htstar.ovms.report.api.vo.*;
import com.htstar.ovms.report.mapper.UseCarReportMapper;
import com.htstar.ovms.report.service.UseCarReportSevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Description: 用车报表
 * Author: flr
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:
 */
@Service
@Slf4j
public class UseCarReportSeviceImpl implements UseCarReportSevice {

    @Autowired
    private UseCarReportMapper useCarReportMapper;

    /**
     * Description: 按月统计用车
     * Author: flr
     * Date: 2020/7/27 11:06
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R<RespMonthReportVO> monthReport(ByMonthReportReq req) {
        OvmsUser user = SecurityUtils.getUser();
        int etpId = user.getEtpId();
        List<MonthReportVO> list = new ArrayList<>();
        RespMonthReportVO resp = new RespMonthReportVO();
        List<Integer> carIdList = null;
        if (StrUtil.isNotBlank(req.getLicCode())) {
            carIdList = useCarReportMapper.getCarIdList(req.getLicCode(), etpId);
            if (null == carIdList || carIdList.isEmpty()) {
                return R.ok(resp);
            }
        }

        String carIdStr = carIdList == null ? null : StrUtil.join(",", carIdList);

        Map<String, Object> params = new HashMap<>();
        params.put("etpId", etpId);
        if (StrUtil.isNotBlank(carIdStr)) {
            params.put("carIdStr", carIdStr);
        }
        params.put("staTime", req.getYearDate() + "-01-01 00:00:00");
        params.put("endTime", req.getYearDate() + "-12-31 23:59:59");

        //所有用车次数
        List<CountVO> allCt = useCarReportMapper.queryMonthUseCarCount(params);
        //公车用车次数
        params.put("applyType", ProcessTypeConstant.PUBLIC_APPLY);
        List<CountVO> publicCt = useCarReportMapper.queryMonthUseCarCount(params);
        //私车用车次数
        params.put("applyType", ProcessTypeConstant.PRIVATE_APPLY);
        List<CountVO> privateCt = useCarReportMapper.queryMonthUseCarCount(params);
        //司机出车次数
        params.remove("applyType");
        params.put("driver", 10);
        List<CountVO> driverCt = useCarReportMapper.queryMonthUseCarCount(params);
        //自驾用车次数
        params.remove("driver");
        params.put("driveType", 10);
        List<CountVO> selfCt = useCarReportMapper.queryMonthUseCarCount(params);

        Map<String, Object> carParams = new HashMap<>();
        carParams.put("etpId", etpId);
        for (int i = 1; i < 13; i++) {
            MonthReportVO vo = new MonthReportVO();
            vo.setMonthDate(req.getYearDate() + "-" + i);
            int nowmonth = OvmDateUtil.getNowMoth();
            if (i > nowmonth) {
                continue;
            }
            int finalI = i;
            Optional<CountVO> uct = allCt.stream().filter(countVO -> countVO.getMd() == finalI).findFirst();
            vo.setUseCarCount(uct.isPresent() ? uct.get().getCt() : 0);

            Optional<CountVO> puct = publicCt.stream().filter(countVO -> countVO.getMd() == finalI).findFirst();
            vo.setPublicUseCarCount(puct.isPresent() ? puct.get().getCt() : 0);

            Optional<CountVO> prct = privateCt.stream().filter(countVO -> countVO.getMd() == finalI).findFirst();
            vo.setPrivateUseCarCount(prct.isPresent() ? prct.get().getCt() : 0);

            Optional<CountVO> dct = driverCt.stream().filter(countVO -> countVO.getMd() == finalI).findFirst();
            vo.setDirverWorkCount(dct.isPresent() ? dct.get().getCt() : 0);

            Optional<CountVO> sct = selfCt.stream().filter(countVO -> countVO.getMd() == finalI).findFirst();
            vo.setSlfeDriveCount(sct.isPresent() ? sct.get().getCt() : 0);

            carParams.put("staTime", req.getYearDate() + "-" + i + "-01 00:00:00");
            String endTime = OvmDateUtil.getMonthLastDay(Integer.parseInt(req.getYearDate()), i);
            carParams.put("endTime", endTime);
            CarUseVO carUseVO = useCarReportMapper.getCarUseVO(carParams);
            if (null != carUseVO) {
                //车辆空置数
                int carEmptyCount = carUseVO.getAllCarCount() - carUseVO.getUseCarCount();
                if (carEmptyCount < 0) {
                    carEmptyCount = 0;
                }
                //车辆空置率
                if (carUseVO.getAllCarCount() > 0) {
                    BigDecimal carEmptyRate = new BigDecimal(
                            NumberUtil.decimalFormatMoney(NumberUtil.div(carEmptyCount, carUseVO.getAllCarCount()))
                    );
                    vo.setCarEmptyRate(carEmptyRate);
                } else {
                    vo.setCarEmptyRate(new BigDecimal("0.00"));
                }
                vo.setCarEmptyCount(carEmptyCount);
            }
            list.add(vo);
        }
        //总计
        //用车次数
        int useCarCount = 0;
        //公车用车次数
        int publicUseCarCount = 0;
        //私车公用次数
        int privateUseCarCount = 0;
        //司机出车次数
        int dirverWorkCount = 0;
        //自驾用车次数
        int slfeDriveCount = 0;
        if (!list.isEmpty()) {
            for (MonthReportVO vo : list) {
                useCarCount += vo.getUseCarCount();
                publicUseCarCount += vo.getPublicUseCarCount();
                privateUseCarCount += vo.getPrivateUseCarCount();
                dirverWorkCount += vo.getDirverWorkCount();
                slfeDriveCount += vo.getSlfeDriveCount();
            }
            resp.setUseCarCount(useCarCount);
            resp.setPublicUseCarCount(publicUseCarCount);
            resp.setPrivateUseCarCount(privateUseCarCount);
            resp.setDirverWorkCount(dirverWorkCount);
            resp.setSlfeDriveCount(slfeDriveCount);

            Map<String, Object> totalParams = new HashMap<>();
            totalParams.put("etpId", etpId);
            if (StrUtil.isNotBlank(carIdStr)) {
                totalParams.put("carIdStr", carIdStr);
            }
            totalParams.put("staTime", req.getYearDate() + "-01-01 00:00:00");
            totalParams.put("endTime", req.getYearDate() + "-12-31 23:59:59");

            //总空置数
            CarUseVO total = useCarReportMapper.getTotalCarUseVO(totalParams);
            if (null != total) {
                //车辆空置数
                int carEmptyCount = total.getAllCarCount() - total.getUseCarCount();
                if (carEmptyCount < 0) {
                    carEmptyCount = 0;
                }
                //车辆空置率
                if (total.getAllCarCount() > 0) {
                    BigDecimal carEmptyRate = new BigDecimal(
                            NumberUtil.decimalFormatMoney(NumberUtil.div(carEmptyCount, total.getAllCarCount()))
                    );
                    resp.setCarEmptyRate(carEmptyRate);
                } else {
                    resp.setCarEmptyRate(new BigDecimal("0.00"));
                }
                resp.setCarEmptyCount(carEmptyCount);
            }
        }


        resp.setMonthReportList(list);

        if (req.getExportStatus() == 1) {
            return monthReportExport(resp);
        }
        return R.ok(resp);
    }

    /**
     * Description: 按车辆统计用车
     * Author: flr
     * Date: 2020/8/4 11:43
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R<UseByCarVO> carReport(ByCarReportPageReq req) {
        OvmsUser user = SecurityUtils.getUser();
        req.setEtpId(user.getEtpId());
        if (req.getExportStatus() == 1) {
            return carReportExport(req);
        }
        //计算总数
        UseByCarVO vo = new UseByCarVO();
        IPage<CarReportVO> page = useCarReportMapper.getUseCarPage(req);
        vo.setPage(page);
        if (page != null && page.getRecords().size() > 0) {
            ByCarReportReq byCarReportReq = new ByCarReportReq();
            BeanUtil.copyProperties(req, byCarReportReq);
            Map<String, Object> map = useCarReportMapper.getUseCarSum(byCarReportReq);
            if (null != map) {
                vo.setCostTotal(map.get("costCount").toString());
                vo.setUseTotal(Integer.parseInt(map.get("useCount").toString()));
            }
        }
        return R.ok(vo);
    }


    private R<UseByCarVO> carReportExport(ByCarReportPageReq req) {
        HttpServletResponse response = WebUtils.getResponse();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("报表信息");

        ByCarReportReq byCarReportReq = new ByCarReportReq();
        BeanUtil.copyProperties(req, byCarReportReq);
        List<CarReportVO> list = useCarReportMapper.getUseCarList(byCarReportReq);
        if (list == null || list.isEmpty()) {
            return R.ok();
        }

        List<Map<String, Object>> totalrows = new ArrayList<>();
        Map<String, Object> row1 = new LinkedHashMap<>();

        Map<String, Object> map = useCarReportMapper.getUseCarSum(byCarReportReq);
        if (null != map) {
            row1.put("出车次数总计", map.get("useCount").toString());
            row1.put("费用总计（元）", map.get("costCount").toString());
            totalrows.add(row1);
        }
        writer.write(totalrows, true);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (CarReportVO vo : list) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("车辆", vo.getLicCode());
            row.put("出车次数", vo.getUseCount());
            row.put("所属部门", vo.getDeptName());
            row.put("总计费用（元）", vo.getCostCount());
            rows.add(row);
        }
        writer.write(rows, true);
        OutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(URLEncoder.encode(
                    "用车报表-按车统计" + OvmDateUtil.getCstNowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    , "UTF-8").getBytes("UTF-8"), "ISO8859-1") + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (
                IOException e) {
            log.error("用车报表-按月统计", e);
        } finally {
            writer.close();
            IoUtil.close(out);
            return null;
        }
    }


    public R monthReportExport(RespMonthReportVO resp) {
        List<MonthReportVO> list = resp.getMonthReportList();
        HttpServletResponse response = WebUtils.getResponse();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("报表信息");
        if (list == null || list.isEmpty()) {
            return R.ok();
        }
        List<Map<String, Object>> rows = new ArrayList<>();

        List<Map<String, Object>> totalrows = new ArrayList<>();
        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("/", "总计：");
        row1.put("用车次数总计", resp.getUseCarCount());
        row1.put("普通用车次数总计", resp.getPublicUseCarCount());
        row1.put("私车公用次数总计", resp.getPrivateUseCarCount());
        row1.put("司机出车次数总计", resp.getDirverWorkCount());
        row1.put("自驾用车次数总计", resp.getSlfeDriveCount());
        row1.put("车辆空置数总计", resp.getCarEmptyCount());
        row1.put("车辆总空置率", resp.getCarEmptyRate().multiply(new BigDecimal("100")) + "%");
        totalrows.add(row1);
        writer.write(totalrows, true);

        for (MonthReportVO vo : list) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("月份", vo.getMonthDate());
            row.put("用车次数", vo.getUseCarCount());
            row.put("普通用车次数", vo.getPublicUseCarCount());
            row.put("私车公用次数", vo.getPrivateUseCarCount());
            row.put("司机出车次数", vo.getDirverWorkCount());
            row.put("自驾用车次数", vo.getSlfeDriveCount());
            row.put("车辆空置数", vo.getCarEmptyCount());
            row.put("车辆空置率", vo.getCarEmptyRate().multiply(new BigDecimal("100")) + "%");
            rows.add(row);
        }
        writer.write(rows, true);
        OutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(URLEncoder.encode(
                    "用车报表-按月统计" + OvmDateUtil.getCstNowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    , "UTF-8").getBytes("UTF-8"), "ISO8859-1") + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (
                IOException e) {
            log.error("用车报表-按月统计", e);
        } finally {
            writer.close();
            IoUtil.close(out);
            return null;
        }
    }

    /**
     * @param req
     * @return
     */
    @Override
    public R<MyPage<PersonalReportReqVO>> personalReport(PersonalReportReq req) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        String monthShort = getMonthShort(req);
        req.setMonthShort(monthShort);
        req.setEtpId(etpId);
        if (req.getExportStatus() == 1) {
            return carReportExportByPerson(req);
        }
        MyPage<PersonalReportReqVO> rs = useCarReportMapper.personalReport(req);
        List<PersonalReportReqVO> records = rs.getRecords();
        double costTotal = 0;
        int useTotal = 0;
        for (PersonalReportReqVO one : records) {
            costTotal += Double.parseDouble(one.getCostCount());
            useTotal += Integer.parseInt(one.getUseCount());
        }
        rs.setCostTotal(costTotal);
        rs.setUseTotal(useTotal);
        return R.ok(rs);
    }

    private R<MyPage<PersonalReportReqVO>> carReportExportByPerson(PersonalReportReq req) {
        HttpServletResponse response = WebUtils.getResponse();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("报表信息");

        PersonalReportReqNoPage personalReportReqNoPage = new PersonalReportReqNoPage();
        BeanUtil.copyProperties(req, personalReportReqNoPage);
        List<PersonalReportReqVO> list = useCarReportMapper.personalReportNopage(req);
        if (list == null || list.isEmpty()) {
            return R.ok();
        }

        List<Map<String, Object>> totalrows = new ArrayList<>();
        Map<String, Object> row1 = new LinkedHashMap<>();

        double costTotal = 0;
        int useTotal = 0;
        for (PersonalReportReqVO one : list) {
            costTotal += Double.parseDouble(one.getCostCount());
            useTotal += Integer.parseInt(one.getUseCount());
        }

        row1.put("出车次数总计", useTotal + "");
        row1.put("费用总计（元）", costTotal + "");
        totalrows.add(row1);

        writer.write(totalrows, true);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (PersonalReportReqVO vo : list) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("车辆", vo.getName());
            row.put("出车次数", vo.getUseCount());
            row.put("所属部门", vo.getDeptName());
            row.put("总计费用（元）", vo.getCostCount());
            rows.add(row);
        }
        writer.write(rows, true);
        OutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(URLEncoder.encode(
                    "用车报表-按人统计" + OvmDateUtil.getCstNowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    , "UTF-8").getBytes("UTF-8"), "ISO8859-1") + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (
                IOException e) {
            log.error("用车报表-按人统计", e);
        } finally {
            writer.close();
            IoUtil.close(out);
            return null;
        }
    }


    /**
     * 查询年月份处理
     *
     * @param req
     * @return
     */
    public String getMonthShort(PersonalReportReq req) {
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
}
