package com.htstar.ovms.enterprise.api.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公车申请事件
 *
 * @author flr
 * @date 2020-06-30 18:24:20
 */
@Data
@ApiModel(value = "公车申请事件")
public class ApplyCarOrderDerverVO implements Serializable {
private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="用车开始时间")
    private LocalDateTime staTime;
    /**
     * 用车结束时间
     */
    @ApiModelProperty(value="用车结束时间")
    private LocalDateTime endTime;


    @ApiModelProperty(value="司机名称")
    private String driverName;

}
