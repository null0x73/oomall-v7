package cn.edu.xmu.other.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("管理员处理意见传值对象")
public class AfterSaleConfirmVo {
    Boolean confirm;
    Long price;
    String conclusion;
    Byte type;
}
