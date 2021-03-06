package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.Region;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/5 22:05
 */
@Data
@ApiModel(description = "地区视图对象")
public class RegionRecieveVo {
    @NotBlank(message = "地区名不能为空")
    @ApiModelProperty(value = "地区名称")
    private String name;

    @ApiModelProperty(value = "邮政编码")
    private Long postalCode;

    public Region createRegion() {
        Region region = new Region();
        region.setPostalCode(this.postalCode);
        region.setName(this.name);
        return region;
    }
}
