package cn.edu.xmu.other.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 潘登
 */
@Data
@ApiModel("售后单传值对象")
public class AfterSaleVo {
    @NotNull
    private Byte type;
    @NotNull
    private Integer quantity;

    private String reason;
    @NotNull
    private Long regionId;

    private String detail;
    @NotBlank
    private String consignee;
    @NotBlank
    private String mobile;
}
