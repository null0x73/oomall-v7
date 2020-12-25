package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.FavoritePo;
import cn.edu.xmu.other.model.vo.FavoriteRetVo;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/11/29 19:25
 */
@Data
public class Favorite implements VoObject {

    private Long id;
    private Long customerId;
    private Long goodsSkuId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private SkuDTO skuDTO;


    public Favorite() {
    }

    /**
     * 构造函数
     * @author 何明祥 24320182203193
     * @return Favorite
     * createBy 何明祥 2020/11/29 19:34
     */
    public Favorite(Long customerId, Long goodsSkuId) {
        this.id = null;
        this.customerId = customerId;
        this.goodsSkuId = goodsSkuId;
        this.gmtCreate = LocalDateTime.now();
        this.gmtModified = LocalDateTime.now();
    }


    /**
     * 构造函数
     * @author 何明祥 24320182203193
     * @param favoritetPo 用PO构造
     * @return Favorite
     * createBy 何明祥 2020/11/29 19:34
     */
    public Favorite(FavoritePo favoritetPo) {
        this.id = favoritetPo.getId();
        this.customerId = favoritetPo.getCustomerId();
        this.goodsSkuId = favoritetPo.getGoodsSkuId();
        this.gmtCreate = favoritetPo.getGmtCreate();
        this.gmtModified =favoritetPo.getGmtModified();
    }


    /**
     * 生成FavoriteRetVo对象作为返回前端
     *
     * @author 何明祥 24320182203193
     * @return Object
     * createBy 何明祥 2020/11/29 19:34
     */
    @Override
    public Object createVo() {
        return new FavoriteRetVo(this);
    }

    /**
     * 生成FavoriteSimpleRetVo对象作为返回前端
     * @author 何明祥 24320182203193
     * @return Object
     * createBy 何明祥 2020/11/29 19:34
     */
    @Override
    public Object createSimpleVo() {
        return new FavoriteRetVo(this);
    }


    /**
     * 用bo对象创建更新po对象
     * @author 何明祥 24320182203193
     * @return FavoritePo
     * createBy 何明祥 2020/11/29 19:34
     */
    public FavoritePo gotFavoritePo() {
        FavoritePo po = new FavoritePo();
        po.setId(this.getId());
        po.setCustomerId(this.getCustomerId());
        po.setGoodsSkuId(this.goodsSkuId);
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(this.gmtModified);
        return po;
    }
}
