package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.AfterSaleServicePo;
import cn.edu.xmu.other.model.vo.AfterSaleRetVo;
import cn.edu.xmu.other.model.vo.AfterSaleSampleRetVo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 潘登 24320182203249
 */
@Data
public class AfterSale implements VoObject, Serializable {
    /**RetVo部分*/
    private Long id;
    private Long orderId;
    private String orderSn;
    private Long orderItemId;
    private Long skuId;
    private String skuName;
    private Long customerId;
    private Long shopId;
    private String serviceSn;
    private Byte type;
    private String reason;
    private Long refund;
    private Integer quantity;
    private Long regionId;
    private String detail;
    private String consignee;
    private String mobile;
    private String customerLogSn;
    private String shopLogSn;
    private Byte state;

    /**po部分*/
    private String conclusion;
    private Byte beDeleted;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    /**自己用来区分不同返回vo的标志位*/
    private Integer differFlg;

    public AfterSale(){}

    public AfterSale(AfterSaleServicePo po){
        this.id=po.getId();
        this.orderItemId=po.getOrderItemId();
        this.customerId=po.getCustomerId();
        this.shopId=po.getShopId();
        this.serviceSn=po.getServiceSn();
        this.type=po.getType();
        this.reason=po.getReason();
        this.conclusion=po.getConclusion();
        this.refund=po.getRefund();
        this.quantity=po.getQuantity();
        this.regionId=po.getRegionId();
        this.detail=po.getDetail();
        this.consignee=po.getConsignee();
        this.mobile=po.getMobile();
        this.customerLogSn=po.getCustomerLogSn();
        this.shopLogSn=po.getShopLogSn();
        this.state=po.getState();
        this.beDeleted=po.getBeDeleted();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.orderId=po.getOrderId();
        /**
         * 尚未完成，需要集成其他模块得到其他信息,下面的信息必集成
         */
        this.orderSn="待集成";
        this.skuId=341L;
        this.skuName=null;
    }

    /**
     * 用来返回插入返回的vo
     * @param arg
     */
    public AfterSale(AfterSaleServicePo po,Integer arg){
        this(po);
        this.differFlg=arg;
    }

    @Override
    public Object createVo() {
        if(this.differFlg==null)return new AfterSaleSampleRetVo(this);
        else
        {
            return new AfterSaleRetVo(this);
        }
    }

    @Override
    public Object createSimpleVo() {
        return new AfterSaleRetVo(this);
    }

    public AfterSaleServicePo gotAfterSaleServicePo(){
        AfterSaleServicePo retPo=new AfterSaleServicePo();
        retPo.setId(this.getId());
        retPo.setOrderItemId(this.getOrderItemId());
        retPo.setCustomerId(this.getCustomerId());
        retPo.setShopId(this.getShopId());
        retPo.setServiceSn(this.getServiceSn());
        retPo.setType(this.getType());
        retPo.setReason(this.getReason());
        retPo.setConclusion(this.getConclusion());
        retPo.setRefund(this.getRefund());
        retPo.setQuantity(this.getQuantity());
        retPo.setRegionId(this.getRegionId());
        retPo.setDetail(this.getDetail());
        retPo.setConsignee(this.getConsignee());
        retPo.setMobile(this.getMobile());
        retPo.setCustomerLogSn(this.getCustomerLogSn());
        retPo.setShopLogSn(this.getShopLogSn());
        retPo.setState(this.getState());
        retPo.setBeDeleted(this.getBeDeleted());
        retPo.setGmtCreate(this.getGmtCreate());
        retPo.setGmtModified(this.getGmtModified());
        retPo.setOrderId(this.getOrderId());
        return retPo;
    }
}
