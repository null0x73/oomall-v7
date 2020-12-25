package cn.edu.xmu.other.model.vo;


import cn.edu.xmu.other.model.bo.Customer;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/14 14:01
 */
@Data
public class CustomerModifyReceiveVo {
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    private Customer.Gender gender;
    private LocalDate birthday;

}
