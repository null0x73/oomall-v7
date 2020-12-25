package cn.edu.xmu.other.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/14 16:55
 */
@Data
@ApiModel
public class CustomerSimpleRetVo {
    @ApiModelProperty(name = "用户id", value = "id")
    private Long id;

    @ApiModelProperty(name = "用户名", value = "userName")
    private String userName;

    @ApiModelProperty(name = "真实姓名", value = "realName")
    private String realName;
}
