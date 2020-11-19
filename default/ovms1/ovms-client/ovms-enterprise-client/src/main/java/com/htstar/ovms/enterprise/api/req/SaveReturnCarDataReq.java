package com.htstar.ovms.enterprise.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/8
 * Company: 航通星空
 * Modified By:
 */
@Data
public class SaveReturnCarDataReq {
    @ApiModelProperty(value="公车申请事件id(主键，不自增)",required = true)
    private Integer orderId;

    /**
     * 回车里程
     */
    @ApiModelProperty(value="回车里程")
    private BigDecimal retuenCarMileage;
    /**
     * 回车续航里程
     */
    @ApiModelProperty(value="回车续航里程")
    private BigDecimal retuenCarKeepMileage;
    /**
     * 回车时间
     */
    @ApiModelProperty(value="回车时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime retuenCarTime;
    /**
     * 回车凭证照片
     */
    @ApiModelProperty(value="回车凭证照片")
    private String retuenCarPhoto;
    /**
     * 提车备注（30字内）
     */
    @ApiModelProperty(value="提车备注（30字内）")
    private String returnCarRemark;


    /**
     * 私车公用(停车费)
     */
    @ApiModelProperty(value="私车公用(停车费)")
    private BigDecimal privateCarStopFee;
    /**
     * 私车公用(通行费)
     */
    @ApiModelProperty(value="私车公用(通行费)")
    private BigDecimal privateCarPassFee;
    /**
     * 私车公用(加油费)
     */
    @ApiModelProperty(value="私车公用(加油费)")
    private BigDecimal privateCarOilFee;
    /**
     * 私车公用(洗车费)
     */
    @ApiModelProperty(value="私车公用(洗车费)")
    private BigDecimal privateCarWashFee;
    /**
     * 私车公用(其他费用)
     */
    @ApiModelProperty(value="私车公用(其他费用)")
    private BigDecimal privateCarOtherFee;

    /**
     * 用车评分(1-5分)
     */
    @ApiModelProperty(value="用车评分(1-5分)")
    private Integer useScore;

    /**
     * 用车评价
     */
    @ApiModelProperty(value="用车评价")
    private String useRemark;
}
