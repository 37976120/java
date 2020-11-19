package com.htstar.ovms.report.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.util.concurrent.ExecutionList;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.core.util.WebUtils;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.report.api.req.ByDrivingReportPageReq;
import com.htstar.ovms.report.api.req.DrivingReportReq;
import com.htstar.ovms.report.api.vo.*;
import com.htstar.ovms.report.mapper.DrivingReportMapper;
import com.htstar.ovms.report.service.DrivingReportSevice;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Description:
 * Author: JinZhu
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:  行驶报表
 */
@Service
@Slf4j
public class DrivingReportSeviceImpl  implements DrivingReportSevice {

    @Resource
    DrivingReportMapper drivingReportMapper;

    /*按月统计行驶记录*/
    @Override
    public R<List<DrivingReportVO>> drivingReportI(DrivingReportReq req) {
        int totalDrivingKmCount = 0;
        int totalDrivingTimeCount = 0;
        int totalDrivingOilTotal= 0;

        //获取当前用户
        OvmsUser user = SecurityUtils.getUser();
        //获取当前用户企业ID
        int etpId = user.getEtpId();
        //映射变量，返回视图对象VO
        List<DrivingReportVO> list = new ArrayList<>();
        List<Integer> dvgIdList = null;
        //条件查询判断是否为空
        if(StrUtil.isNotBlank(req.getLicCode())){
            //利用车牌号查询该车每月行驶记录

           dvgIdList = drivingReportMapper.getDvgIdList(req.getLicCode(),etpId);
           if(null == dvgIdList || dvgIdList.isEmpty()){ //如果没有数据，就返回无数据
               return  R.ok(new ArrayList<>());
           }
        }
        //有数据可能是一串，利用StrUtil.join用，截取
        String dvgIdStar = dvgIdList == null ? null :StrUtil.join(",", dvgIdList);
        //解析日期某年到某年，部门条件
        Map<String,Object> params = new HashMap<>();
        params.put("etpId",etpId);
        if (StrUtil.isNotBlank(dvgIdStar)){
            params.put("dvgIdStar",dvgIdStar);
        }
        params.put("staTime",req.getYearDate() + "-01-01 00:00:00");
        params.put("endTime",req.getYearDate() + "-12-31 23:59:59");

        //查询记录
        List<TotalVO> mileaGeTalList = drivingReportMapper.queryMonthDrivingCount(params);
        Map<String,Object> drvParams = new HashMap<>();
        for (int i = 1; i < 13 ; i++) {
            //映射月份
            DrivingReportVO vo = new DrivingReportVO();
            vo.setMonthDate(req.getYearDate() + "-" +i);
            int finalI = i; //当前月份
            //TotalVO  tvo= new TotalVO(i,0,0,0,0);
            Supplier<TotalVO>  tvo= TotalVO::new;
            //过滤掉没有的月份数据，显示为0
            Optional<TotalVO> to = mileaGeTalList
                    .stream()
                    .filter(totalVo -> totalVo.getMd() == finalI).findFirst();
                     vo.setDrivingKmCount(to.isPresent() ? to.get().getMileaGeTal():tvo.get().getMileaGeTal());
                     vo.setDrivingTimeCount(to.isPresent() ? to.get().getTimeLong():tvo.get().getTimeLong());
                     vo.setDrivingOilTotal(to.isPresent() ? to.get().getConsumPtionTal() : tvo.get().getConsumPtionTal());
                     vo.setDrivingHkmOilTotal(to.isPresent() ? to.get().getHdredKm():tvo.get().getTimeLong());
            totalDrivingKmCount = vo.getDrivingKmCount()+totalDrivingKmCount; //行驶公里总计
            totalDrivingTimeCount =  vo.getDrivingTimeCount() + totalDrivingTimeCount;//行驶时间总计
            totalDrivingOilTotal = vo.getDrivingOilTotal() + totalDrivingOilTotal;//用油量总计

                     //BeanUtils.copyProperties(vo,to.isPresent() ? to.get().getHdredKm():tvo.getHdredKm());

            //转换北京时间
//            drvParams.put("staTime",req.getYearDate() + "-" + i + "-01 00:00:00");
//            String endTime = OvmDateUtil.getMonthLastDay(Integer.parseInt(req.getYearDate()),i);
//            drvParams.put("endTime",endTime);
            list.add(vo);
        }
        List list2 =new ArrayList<>();
        Map msp = new HashMap();
        msp.put("totalDrivingKmCount",totalDrivingKmCount);
        msp.put("totalDrivingTimeCount",totalDrivingTimeCount);
        msp.put("totalDrivingOilTotal",totalDrivingOilTotal);
        list2.add(list);
        list2.add(msp);
        if(req.getExportStatus() == 1){
            return monthReportExport(list,msp);
        } else {
            return  R.ok(list2);
        }

    }
    /**
     * Description: 按车辆统计用行驶
     * Date: 2020/8/4 11:43
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R<VceTotalPageVO> vceDrivingReport(ByDrivingReportPageReq req) {
        //获取当前登录用户
           OvmsUser user = SecurityUtils.getUser();
        req.setEtpId(user.getEtpId());
       //获取数据存入返回前端视图
        VceTotalPageVO vo = new VceTotalPageVO();
        //查询数据
        IPage<VceTotalVO> page = drivingReportMapper.queryVceMonthDrivingCount(req);
        vo.setPage(page);
        if(page !=null && page.getRecords().size() > 0){
            for (int i = 0; i < page.getRecords().size() ; i++) {
                VceTotalVO vo1 = page.getRecords().get(i);
                vo.setTotalDrivingKmCount(vo1.getMileaGeTal()+vo.getTotalDrivingKmCount());
                vo.setTotalDrivingTimeCount(vo1.getTimeLong()+vo.getTotalDrivingTimeCount());
                vo.setTotalDrivingOilTotal(vo1.getConsumPtionTal()+vo.getTotalDrivingOilTotal());
            }
        }
        if (req.getExportStatus() == 1){
            return vceReportExport(page,vo);
        }
        return R.ok(vo);
    }

    /**
     * 按车辆导出行驶记录
     * @param page
     * @return
     */
    public R vceReportExport(IPage<VceTotalVO> page,VceTotalPageVO vo) {
     HttpServletResponse response = WebUtils.getResponse();
        ExcelWriter writer =new ExcelWriter();
        writer.renameSheet("车辆报表行驶记录");
        if(page ==null && page.getRecords().size() <= 0){
            return  null;
        }
        List<Map<String,Object>> rows = new ArrayList<>();
        List<Map<String,Object>> rows1 = new ArrayList<>();
        Map<String,Object> row1 = new LinkedHashMap<>();
        row1.put("/","总计：");
        row1.put("行驶公里",vo.getTotalDrivingKmCount());
        row1.put("行驶时间",vo.getTotalDrivingTimeCount());
        row1.put("用油量",vo.getTotalDrivingOilTotal());
        rows1.add(row1);
        writer.write(rows1,true );//写入数据
        for (int i = 0; i < page.getRecords().size() ; i++) {
            Map<String,Object> row= new LinkedHashMap<>();
            VceTotalVO vo1 = page.getRecords().get(i);
            row.put("车牌号", vo1.getVce());
            row.put("行驶公里", vo1.getMileaGeTal());
            row.put("时间/h", vo1.getTimeLong());
            row.put("用油量/L", vo1.getConsumPtionTal());
            row.put("里程表数", vo1.getOmtNumber());
            row.put("百公里耗油/L", vo1.getHdredKm());
            rows.add(row);
        }
        writer.write(rows,true );//写入数据
        OutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(URLEncoder.encode(
                    "行驶报表-按车辆统计" + OvmDateUtil.getCstNowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    , "UTF-8").getBytes("UTF-8"), "ISO8859-1") + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out,true);
        } catch (Exception e) {
            log.error("行驶报表-按车辆统计", e);
        } finally {
            writer.close(); //关不输入输出
            IoUtil.close(out);//关闭io流
            return null;
        }
    }

    /**
     *  按月份导出行驶记录
     * @param list
     * @return
     */
    public R monthReportExport(List<DrivingReportVO> list,Map map) {
        //获取客户端响应
        HttpServletResponse response = WebUtils.getResponse();
        //开启写入写出器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("月份报表行驶记录");
        if (list == null || list.isEmpty()){
            return null; //返回为空，因为如果返回响应的话，就提交了二次会报 UT010019: Response already commited
        }
        List<Map<String,Object>> rows = new ArrayList<>();
        List<Map<String,Object>> rows1 = new ArrayList<>();
        Map<String,Object> row1 = new LinkedHashMap<>();
        row1.put("/","总计：");
        row1.put("行驶公里",map.get("totalDrivingKmCount"));
        row1.put("行驶时间",map.get("totalDrivingTimeCount"));
        row1.put("用油量",map.get("totalDrivingOilTotal"));
        rows1.add(row1);
        writer.write(rows1,true );//写入数据
        //传输不能拿实体类，传输视图数据可以用vo
        for (DrivingReportVO vo:list) {
            //导出数据利用HashMap是无序的，要用LinkedHashMap有序
            Map<String,Object> row = new LinkedHashMap<>();
            row.put("月份", vo.getMonthDate());
            row.put("行驶公里", vo.getDrivingKmCount());
            row.put("时间/h", vo.getDrivingTimeCount());
            row.put("用油量/L", vo.getDrivingOilTotal());
            row.put("百公里耗油/L", vo.getDrivingHkmOilTotal());
            rows.add(row);
        }
         writer.write(rows,true); //写入数据
         OutputStream out = null; //输出流

        try {
            //设置响应格式显示为excel
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(URLEncoder.encode(
                    "行驶报表-按月统计" + OvmDateUtil.getCstNowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    , "UTF-8").getBytes("UTF-8"), "ISO8859-1") + ".xlsx");
            out = response.getOutputStream();//输出流响应
            writer.flush(out,true);//开启缓冲
        } catch (IOException e) {
            log.error("行驶报表-按月统计", e);

        } finally {
            writer.close(); //关不输入输出
            IoUtil.close(out);//关闭io流
            return null; //返回为空，因为如果返回有响应的话，就提交了二次会报 UT010019: Response already commited
        }
    }
}
