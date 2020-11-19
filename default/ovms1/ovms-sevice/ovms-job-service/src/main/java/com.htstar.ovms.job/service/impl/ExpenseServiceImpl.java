package com.htstar.ovms.job.service.impl;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.job.mapper.ExpenseMapper;
import com.htstar.ovms.job.service.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 * @Author: lw
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:
 */
@Service
@Slf4j
public class ExpenseServiceImpl extends ServiceImpl<ExpenseMapper,T > implements ExpenseService {
    private static Integer max_size=3;

    /**
     * 获取当月数据
     */
    @Override
    public void getDataForMonth() {
        //上月时间
        DateTime dateTime = DateUtil.date();
        DateTime startTime = DateUtil.beginOfMonth(dateTime);
        DateTime endTime = DateUtil.endOfMonth(dateTime);
        //month_short
        String format = DateUtil.format(dateTime, "yyyy-MM");
        //有效车辆列表
        List<Integer> carIds = baseMapper.getCarIds();
        Integer limit = countStep(carIds.size());
        List<List<Integer>> mglist = new ArrayList<>();
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i ->
        { mglist.add(carIds.stream().skip(i * max_size).limit(max_size).collect(Collectors.toList()));
        });


        for (List<Integer> list : mglist) {
            baseMapper.saveFuelCost(list,startTime,endTime);
            baseMapper.saveEtcCost(list,startTime ,endTime );
            baseMapper.saveInsCost(list,startTime ,endTime );
            baseMapper.saveMaiCost(list,startTime ,endTime );
            baseMapper.saveMotCost(list,startTime ,endTime );
            baseMapper.saveRepairCost(list,startTime ,endTime );
            baseMapper.saveStopCost(list,startTime ,endTime );
            baseMapper.saveWashCost(list,startTime ,endTime );
            baseMapper.saveTicketCost(list,startTime ,endTime );
            baseMapper.saveSuppliesCost(list,startTime ,endTime );
            baseMapper.saveOtherCost(list,startTime ,endTime );
        }
    }

    private static Integer countStep(Integer size) {
        return (size + max_size - 1) / max_size;
    }
}
