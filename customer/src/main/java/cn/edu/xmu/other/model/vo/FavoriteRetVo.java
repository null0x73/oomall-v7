package cn.edu.xmu.other.model.vo;


import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.other.model.bo.Favorite;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/11/30 15:43
 */
@Data
public class FavoriteRetVo {

    @Data
    public class MyGoodsSku
    {
        private Long id;

        private String name;

        private String skuSn;

        private String imageUrl;

        private Integer inventory;

        private Long originalPrice;

        private Long price;

        private Boolean disable;

        public MyGoodsSku(SkuDTO skuDTO){
            this.id=skuDTO.getId();
            this.imageUrl=skuDTO.getImageUrl();
            this.disable=skuDTO.getDisable()==0?false:true;
            this.inventory=skuDTO.getInventory();
            this.skuSn=skuDTO.getSkuSn();
            this.originalPrice=skuDTO.getOriginalPrice();
            this.price=skuDTO.getPrice();
            this.name=skuDTO.getName();
        }
    }

    private Long id;
    private MyGoodsSku goodsSku;
    private LocalDateTime gmtCreate;

    /**
     * 用bo对象建立Vo对象
     * @author 何明祥 24320182203193
     * @param favorite
     * @return FavoriteRetVo
     * createBy 何明祥 2020/11/30 15:43
     */
    public FavoriteRetVo(Favorite favorite) {
        this.id = favorite.getId();
        this.goodsSku=new MyGoodsSku(favorite.getSkuDTO());
        this.gmtCreate=favorite.getGmtCreate();
    }
}
