package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/6 20:46
 */
@Data
@ApiModel(description = "地址视图对象")
public class AddressRecieveVo {

    @NotNull
    @ApiModelProperty(value = "地区码")
    private  Long regionId;

    @NotBlank(message = "详细地址不能为空")
    @ApiModelProperty(value = "详细地址")
    private String detail;

    @NotBlank(message = "联系人不能为空")
    @ApiModelProperty(value = "联系人")
    private String consignee;

    @Pattern(regexp="[+]?[0-9*#]{11}",message="手机号格式不正确")
    @NotBlank(message = "联系方式不能为空")
    @ApiModelProperty(value = "联系方式")
    private String mobile;


    public Address createAddress() {
        Address address = new Address();
        address.setRegionId(this.regionId);
        address.setDetail(this.detail);
        address.setConsignee(this.consignee);
        address.setMobile(this.mobile);
        return address;
    }

}
