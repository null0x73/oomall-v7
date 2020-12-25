package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.BeSharePo;
import cn.edu.xmu.other.model.vo.BeSharedRetVo;
import cn.edu.xmu.other.model.vo.ShareSkuVo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BeShared implements VoObject, Serializable {
    private Long id;
    private Long goodsSkuId;
    private Long sharerId;
    private Long shareId;
    private Long customerId;
    private Long orderId;
    private Integer rebate;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long shareActivityId;

    private ShareSkuVo sku;

    public BeShared(BeSharePo po){
        this.id=po.getId();
        this.goodsSkuId=po.getGoodsSkuId();
        this.sharerId=po.getSharerId();
        this.shareId=po.getShareId();
        this.customerId=po.getCustomerId();
        this.orderId=po.getOrderId();
        this.rebate=po.getRebate();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.shareActivityId=po.getShareActivityId();
        this.sku=new ShareSkuVo();
    }

    @Override
    public BeSharedRetVo createVo() {
        return new BeSharedRetVo(this);
    }

    @Override
    public BeSharedRetVo createSimpleVo() {
        return new BeSharedRetVo(this);
    }
}
