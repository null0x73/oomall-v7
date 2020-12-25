package cn.edu.xmu.other.model.vo;


import cn.edu.xmu.other.model.bo.AfterSale;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "售后单视图对象")
public class AfterSaleRetVo {
    @ApiModelProperty(value="售后单id")
    private Long id;
    @ApiModelProperty(value="订单id")
    private Long orderId;
    //@ApiModelProperty(value="订单序号")
   // private String orderSn;
    @ApiModelProperty(value="订单明细id")
    private Long orderItemId;
    @ApiModelProperty(value="skuId")
    private Long skuId;
    @ApiModelProperty(value="sku名字")
    private String skuName;
    @ApiModelProperty(value="用户id")
    private Long customerId;
    @ApiModelProperty(value="商店id")
    private Long shopId;
    @ApiModelProperty(value="服务序号")
    private String serviceSn;
    @ApiModelProperty(value="售后种类")
    private Byte type;
    @ApiModelProperty(value="售后原因")
    private String reason;
    @ApiModelProperty(value="退款")
    private Long refund;
    @ApiModelProperty(value="数量")
    private Integer quantity;
    @ApiModelProperty(value="地区id")
    private Long regionId;
    @ApiModelProperty(value="详细信息")
    private String detail;
    @ApiModelProperty(value="联系人")
    private String consignee;
    @ApiModelProperty(value="电话")
    private String mobile;
    @ApiModelProperty(value="用户运单号")
    private String customerLogSn;
    @ApiModelProperty(value="商店运单号")
    private String shopLogSn;
    @ApiModelProperty(value="状态")
    private Byte state;

    public AfterSaleRetVo(AfterSale afterSale){
        this.id=afterSale.getId();
        this.orderId=afterSale.getOrderId();
        //this.orderSn=afterSale.getOrderSn();
        this.orderItemId=afterSale.getOrderItemId();
        this.skuId=afterSale.getSkuId();
        this.skuName=afterSale.getSkuName();
        this.customerId=afterSale.getCustomerId();
        this.shopId=afterSale.getShopId();
        this.serviceSn=afterSale.getServiceSn();
        this.type=afterSale.getType();
        this.reason=afterSale.getReason();
        this.refund=afterSale.getRefund();
        this.quantity=afterSale.getQuantity();
        this.regionId=afterSale.getRegionId();
        this.detail=afterSale.getDetail();
        this.consignee=afterSale.getConsignee();
        this.mobile=afterSale.getMobile();
        this.customerLogSn=afterSale.getCustomerLogSn();
        this.shopLogSn=afterSale.getShopLogSn();
        this.state=afterSale.getState();
    }
}
