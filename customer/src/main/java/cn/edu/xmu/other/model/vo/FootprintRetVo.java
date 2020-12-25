package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.Footprint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * author 潘登 24320182203249
 * createBy 潘登 2020/11/22 18:41
 */
@Data
@ApiModel(description = "足迹视图对象")
public class FootprintRetVo {
    @ApiModelProperty(value="足迹id")
    private Long id;

    @ApiModelProperty(value="商品sku")
    private FootprintSkuVo goodsSku;

    @ApiModelProperty(value="创建日期")
    private String gmtCreate;

    public FootprintRetVo(Footprint footprint){
        this.id=footprint.getId();
        this.goodsSku= footprint.getSku();
        this.gmtCreate= footprint.getGmtCreate().toString();
    }

}
