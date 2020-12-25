package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.ShareActivityPo;
import cn.edu.xmu.other.model.vo.ShareActivityRetVo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 潘登
 */
@Data
public class ShareActivity implements VoObject, Serializable{
    private Long id;
    private Long shopId;
    private Long goodsSkuId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String strategy;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
    private Byte state;

    public ShareActivity(){}

    /**
     * @author 潘登
     *
     * 构造函数
     * @param po
     */
    public ShareActivity(ShareActivityPo po){
        this.id=po.getId();
        this.shopId=po.getShopId();
        this.goodsSkuId=po.getGoodsSkuId();
        this.beginTime=po.getBeginTime();
        this.endTime=po.getEndTime();
        this.strategy=po.getStrategy();
        this.gmtCreated=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.state=po.getState();
    }

    /**
     * @author 潘登
     * @return
     * 2020/11/26 18：27
     */
    @Override
    public Object createVo() {
        return new ShareActivityRetVo(this);
    }

    /**
     * @author 潘登
     * @return
     * 2020/11/26 18：27
     */
    @Override
    public Object createSimpleVo(){
        return new ShareActivityRetVo(this);
    }

    public ShareActivityPo gotShareActivityPo(){
        ShareActivityPo po=new ShareActivityPo();
        po.setId(this.getId());
        po.setShopId(this.getShopId());
        po.setGoodsSkuId(this.getGoodsSkuId());
        po.setBeginTime(this.getBeginTime());
        po.setEndTime(this.getEndTime());
        po.setStrategy(this.getStrategy());
        po.setGmtCreate(this.getGmtCreated());
        po.setGmtModified(this.getGmtModified());
        po.setState(this.getState());
        return po;
    }
}
