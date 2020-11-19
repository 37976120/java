package com.htstar.ovms.enterprise.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 12:07:04
 */
@Data
public class CarSchedulingDTO extends Page {

    @ApiModelProperty(value="排班id")
    private Integer id;


    /**
     * 结束时间
     */
    @ApiModelProperty(value="企业Id")
    private Integer etpId;

    @ApiModelProperty(value="车票好")
    private String licCode;



}
