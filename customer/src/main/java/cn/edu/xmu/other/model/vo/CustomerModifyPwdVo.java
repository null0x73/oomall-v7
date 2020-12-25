package cn.edu.xmu.other.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "修改密码对象")
public class CustomerModifyPwdVo {

    private String captcha;
    //@Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}", message = "密码不符合规范，至少8个字符，至少1个大写字母，1个小写字母和1个数字")
    private String newPassword;

}

