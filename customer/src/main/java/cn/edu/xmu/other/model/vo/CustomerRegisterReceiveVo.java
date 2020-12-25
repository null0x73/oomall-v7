package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/13 22:25
 */
@Data
public class CustomerRegisterReceiveVo
{
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @Pattern(regexp="[+]?[0-9*#]{11}",message="手机号格式不正确")
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @Email(message = "email格式不正确")
    @NotBlank(message = "email不能为空")
    private String email;

    @NotNull(message = "性别不能为空")
    private Integer gender;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate birthday;

    public Customer createCustomer(){
        Customer customer=new Customer();
        customer.setUserName(this.userName);
        customer.setPassword(this.password);
        customer.setRealName(this.realName);
        customer.setMobile(this.mobile);
        customer.setEmail(this.email);
        customer.setGender(this.gender==1? Customer.Gender.FEMALE: Customer.Gender.MALE);
        customer.setBirthday(this.birthday);
        return customer;
    }
}
