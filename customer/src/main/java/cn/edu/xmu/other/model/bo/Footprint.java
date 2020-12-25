package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.FootprintPo;
import cn.edu.xmu.other.model.vo.FootprintRetVo;
import cn.edu.xmu.other.model.vo.FootprintSkuVo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Footprint implements VoObject, Serializable {
    private Long id;
    private Long customerId;
    private Long goodsSkuId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    private FootprintSkuVo sku;

    public Footprint(){sku=new FootprintSkuVo();}

    /**
     * 构造函数
     *
     * author 潘登 24320182203249
     * @param footprintPo po
     * createBy 潘登 2020/11/22 18：53
     */
    public Footprint(FootprintPo footprintPo){
        this.id= footprintPo.getId();
        this.customerId= footprintPo.getCustomerId();
        this.goodsSkuId= footprintPo.getGoodsSkuId();
        this.gmtCreate=footprintPo.getGmtCreate();
        this.gmtModified=footprintPo.getGmtModified();
        this.sku=new FootprintSkuVo();

    }

    /**
     * 构造函数
     *
     * author 潘登
     * @pram customerId 用户id
     * @pram goodsSpuId 商品spu的id
     * createBy 潘登 2020/11/23 15：54
     */
    public Footprint(Long customerId,Long goodsSkuId){
        this.id=null;
        this.customerId=customerId;
        this.goodsSkuId=goodsSkuId;
        this.gmtCreate=LocalDateTime.now();
        //this.gmtModified=LocalDateTime.now();
    }

    /**
     * author 潘登 24320182203249
     * @return Object
     * createBy 潘登 2020/11/22 19:02
     */
    @Override
    public Object createVo(){return new FootprintRetVo(this); }

    /**
     * author 潘登
     * @return Object
     * createBy 潘登 2020/11/22 19:02
     */
    @Override
    public Object createSimpleVo(){return new FootprintRetVo(this);}

    /**
     * author 潘登 24320182203249
     * @return FootprintPo
     * createBy 潘登 2020/11/22 19:10
     */
    public FootprintPo gotFootprintPo()
    {
        FootprintPo po=new FootprintPo();
        po.setId(this.getId());
        po.setCustomerId(this.getCustomerId());
        po.setGoodsSkuId(this.getGoodsSkuId());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        return po;
    }
}
