package com.htstar.ovms.enterprise.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description: 提车上报
 * Author: flr
 * Date: Created in 2020/7/8
 * Company: 航通星空
 * Modified By:
 */
@Data
public class SaveGiveCarDataReq {
    @ApiModelProperty(value="公车申请事件id(主键，不自增)",required = true)
    private Integer orderId;

    /**
     * 提车里程
     */
    @ApiModelProperty(value="提车里程")
    private BigDecimal giveCarMileage;
    /**
     * 提车续航里程
     */
    @ApiModelProperty(value="提车续航里程")
    private BigDecimal giveCarKeepMileage;
    /**
     * 提车时间
     */
    @ApiModelProperty(value="提车时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime giveCarTime;
    /**
     * 提车凭证照片,逗号隔开
     */
    @ApiModelProperty(value="提车凭证照片,逗号隔开")
    private String giveCarPhoto;
    /**
     * 提车备注（30字内）
     */
    @ApiModelProperty(value="提车备注（30字内）")
    private String giveCarRemark;

}
