package cn.edu.xmu.other.model.vo;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public class CartItemReceiveVo {

//    @Nullable
    Long goodsSkuId;

//    @Nullable
    Integer quantity;

    public CartItemReceiveVo() {
    }

    @Override
    public String toString() {
        return "CartItemCreateReceiveVo{" +
                "goodsSkuId=" + goodsSkuId +
                ", quantity=" + quantity +
                '}';
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
