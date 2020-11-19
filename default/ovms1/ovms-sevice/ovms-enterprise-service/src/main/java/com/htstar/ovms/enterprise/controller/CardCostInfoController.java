package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CardCostInfo;
import com.htstar.ovms.enterprise.api.req.CardCostInfoReq;
import com.htstar.ovms.enterprise.service.CardCostInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 卡流水
 *
 * @author lw
 * @date 2020-07-22 14:25:29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cardcostinfo" )
@Api(value = "cardcostinfo", tags = "流水管理")
public class CardCostInfoController {

    private final CardCostInfoService cardCostInfoService;


    /**
     *
     * 流水分页
     * @param req
     * @return
     *
     */
    @ApiOperation(value = "流水分页", notes = "流水分页")
    @PostMapping("/page" )
    public R<IPage<CardCostInfo>> getCardCostInfoPage(@RequestBody CardCostInfoReq req) {
        return cardCostInfoService.queryPage(req);
    }

}
