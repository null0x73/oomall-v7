package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.SharePo;
import cn.edu.xmu.other.model.vo.ShareRetVo;
import cn.edu.xmu.other.model.vo.ShareSkuVo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Share implements VoObject, Serializable{
    private Long id;
    private Long sharerId;
    private Long goodsSkuId;
    private Integer quantity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long shareActivityId;

    private ShareSkuVo sku;

    public Share(){this.sku=new ShareSkuVo();}

    public Share(SharePo po){
        this.id=po.getId();
        this.sharerId=po.getSharerId();
        this.goodsSkuId=po.getGoodsSkuId();
        this.quantity=po.getQuantity();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.shareActivityId=po.getShareActivityId();
        this.sku=new ShareSkuVo();
    }

    @Override
    public Object createVo() {
        return new ShareRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new ShareRetVo(this);
    }

    public SharePo gotSharePo(){
        SharePo sharePo=new SharePo();
        sharePo.setId(this.getId());
        sharePo.setSharerId(this.getSharerId());
        sharePo.setGoodsSkuId(this.getGoodsSkuId());
        sharePo.setQuantity(this.getQuantity());
        sharePo.setGmtCreate(this.getGmtCreate());
        sharePo.setGmtModified(this.getGmtModified());
        sharePo.setShareActivityId(this.getShareActivityId());
        return sharePo;
    }
}
