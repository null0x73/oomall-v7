package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/6 20:46
 */
@Data
@ApiModel(description = "地址视图对象")
public class AddressReturnVo {

    private Long id;
    private Long customerId;
    private Long regionId;
    private String detail;
    private String consignee;
    private String mobile;
    private Boolean beDefault;
    private LocalDateTime gmtCreate;


}
