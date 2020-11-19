package com.htstar.ovms.enterprise.controller;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.annotation.Inner;
import com.htstar.ovms.enterprise.service.ItemExpirePushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/20
 * Company: 航通星空
 * Modified By:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/itemPush" )
@Api(value = "itemPush", tags = "项目过期推送")
public class ItemExpirePushController {
    @Autowired
    private ItemExpirePushService itemExpirePushService;
    @ApiOperation(value = "年检过期到期", notes = "年检过期")
    @GetMapping("/motItem" )
    @Inner
    public R getMotItemExpirePush() {
        return itemExpirePushService.getMotItemExpirePush();
    }
    @ApiOperation(value = "保险过期", notes = "保险过期")
    @GetMapping("/insItem" )
    @Inner
    public R getInsItemExpirePush() {
        return itemExpirePushService.getInsItemExpirePush();
    }

}
