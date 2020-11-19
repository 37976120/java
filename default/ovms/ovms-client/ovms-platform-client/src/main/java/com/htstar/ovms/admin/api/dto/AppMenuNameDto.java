package com.htstar.ovms.admin.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Info;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/3
 * Company: 航通星空
 * Modified By:
 * @author liuwei
 */
@Data
@ApiModel(value = "app菜单名称")
public class AppMenuNameDto  implements Serializable {
    @ApiModelProperty("菜单编号")
    private Integer id;
    @ApiModelProperty("菜单名称")
    private String name;
    @ApiModelProperty("子菜单名称集合")
    private List<AppMenuNameDto> subMenuList=new ArrayList<>();
}
