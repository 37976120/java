package com.htstar.ovms.job.controller;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.job.service.ExpenseService;
import com.htstar.ovms.job.service.ItemExpirePushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * @Author: lw
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/expense")
@Api(value = "获取数据", tags = "获取数据")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private ItemExpirePushService itemExpirePushService;
    @GetMapping("/getData")
    @ApiOperation(value = "获取数据", notes = "获取数据")
    public R getData(){
        //expenseService.getDataForMonth();
        itemExpirePushService.getMotItemExpirePush();
        return R.ok();
    }
}
