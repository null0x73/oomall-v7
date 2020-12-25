package cn.edu.xmu.other.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("店家收到货物传值对象")
public class AfterSaleShopReceiveVo {
    Boolean confirm;
    String conclusion;
}
