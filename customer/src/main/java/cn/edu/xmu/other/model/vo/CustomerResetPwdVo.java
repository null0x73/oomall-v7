package cn.edu.xmu.other.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "重置密码对象")
public class CustomerResetPwdVo {
    private String mobile;
    private String email;
}

