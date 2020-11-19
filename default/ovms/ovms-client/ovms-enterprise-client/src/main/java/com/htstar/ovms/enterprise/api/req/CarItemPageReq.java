package com.htstar.ovms.enterprise.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javafx.scene.chart.ValueAxis;
import javassist.expr.NewArray;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 * @author liuwei
 */
@Data
@ApiModel(value = "车辆台账管理查询条件")
public class CarItemPageReq  extends Page implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value="结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value="车牌号")
    private String licCode;
    @ApiModelProperty(value="状态")
    private Integer itemStatus;
    @ApiModelProperty(value="企业ID")
    private Integer etpId;
    @ApiModelProperty(value = "司机名下车辆车牌")
    private List<String> licCodes;
    @ApiModelProperty(value = "录入人id")
    private Integer userId;
    @ApiModelProperty(value="导出的ids")
    private String ids;


}
