package cn.edu.xmu.other.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("售后单状态对象")
public class AfterSaleStateRetVo {
    Integer code;
    String name;
}
