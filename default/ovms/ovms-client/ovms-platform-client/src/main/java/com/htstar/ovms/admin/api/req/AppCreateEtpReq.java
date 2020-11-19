package com.htstar.ovms.admin.api.req;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import feign.Body;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.openxml4j.opc.PackageRelationship;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/2
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel(value = "移动端创建企业请求数据")
public class AppCreateEtpReq implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="企业名称",required = true)
    @NotBlank(message = "企业名称不能为空")
    private String etpName;

    /**
     * 管理员姓名
     */
    @ApiModelProperty(value="管理员姓名",required = true)
    @NotBlank(message = "管理员姓名不能为空")
    private String nickName;
    /**
     * 联系方式
     */
    @ApiModelProperty(value="联系方式",required = true)
    @NotBlank(message = "联系方式不能为空")
    private String mobile;

    @ApiModelProperty(value="密码（6-20位）", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value="验证码")
    private  String code;

}
