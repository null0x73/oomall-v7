package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.Share;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("分享视图对象")
public class ShareRetVo {
    @ApiModelProperty(value="分享id")
    private Long id;
    @ApiModelProperty(value="分享者id")
    private Long sharerId;
    @ApiModelProperty(value="sku详细信息")
    private ShareSkuVo sku;
    @ApiModelProperty(value="数量")
    private Integer quantity;
    @ApiModelProperty(value="创建时间")
    private String gmtCreate;

    public ShareRetVo(Share share){
        this.id=share.getId();
        this.sharerId=share.getSharerId();
        this.sku=share.getSku();
        this.quantity=share.getQuantity();
        this.gmtCreate=share.getGmtCreate().toString();
    }
}
