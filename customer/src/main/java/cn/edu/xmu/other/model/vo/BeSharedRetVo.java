package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.BeShared;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 被分享记录视图对象
 * @author 潘登
 *
 */
@Data
@ApiModel("被分享记录视图对象")
public class BeSharedRetVo {
    @ApiModelProperty(value="被分享成功id")
    private Long id;
    @ApiModelProperty(value="sku详细信息")
    private ShareSkuVo sku;//需要集成
    @ApiModelProperty(value="分享者id")
    private Long sharerId;
    @ApiModelProperty(value="被分享者id")
    private Long customerId;
    @ApiModelProperty(value="订单id")
    private Long orderId;
    @ApiModelProperty(value="返点")
    private Integer rebate;
    @ApiModelProperty(value="记录创建时间")
    private String gmtCreate;

    public BeSharedRetVo(BeShared beShared){
        this.id=beShared.getId();
        this.sku=beShared.getSku();
        this.sharerId=beShared.getSharerId();
        this.customerId=beShared.getCustomerId();
        this.orderId=beShared.getOrderId();
        this.rebate=beShared.getRebate();
        this.gmtCreate=beShared.getGmtCreate().toString();
    }
}
