package cn.edu.xmu.other.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("运单号传值对象")
public class AfterSaleLogSnVo {
    @NotBlank
    String logSn;
}
