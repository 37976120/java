package com.htstar.ovms.job.controller;

import com.htstar.ovms.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/test")
@Api(value = "测试", tags = "测试")
public class TestController {
    @ApiOperation(value = "测试接口", notes = "测试接口")
    @GetMapping("/1")
    public R test() {
        return R.ok();
    }
}
