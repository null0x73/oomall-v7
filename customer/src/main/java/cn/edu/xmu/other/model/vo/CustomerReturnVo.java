package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.Customer;
import cn.edu.xmu.other.model.po.CustomerPo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/15 20:15
 */
@Data
public class CustomerReturnVo {


    String userName;
    String name;
    String mobile;
    String email;
    Integer gender;
    LocalDate birthday;
    Integer state;


    public CustomerReturnVo() {
    }

    public CustomerReturnVo(CustomerPo customerPo){
        this.userName = customerPo.getUserName();
        this.name = customerPo.getRealName();
        this.email = customerPo.getEmail();
        this.mobile = customerPo.getMobile();
        this.gender = customerPo.getGender().intValue();
        this.birthday = customerPo.getBirthday();
        this.state=customerPo.getState().intValue();
    }

}
