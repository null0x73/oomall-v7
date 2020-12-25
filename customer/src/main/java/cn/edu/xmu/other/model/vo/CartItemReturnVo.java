package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.goods.client.dubbo.CouponActivityDTO;
import cn.edu.xmu.ooad.model.VoObject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class CartItemReturnVo implements VoObject {

    Long id;

    Long goodsSkuId;

    String skuName;

    Integer quantity;

    Long price;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    LocalDateTime gmtCreate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    LocalDateTime gmtModified;

    List<CouponActivityDTO> couponActivity;

    public CartItemReturnVo() {
    }

    @Override
    public String toString() {
        return "CartItemReturnVo{" +
                "id=" + id +
                ", goodsSkuId=" + goodsSkuId +
                ", skuName='" + skuName + '\'' +
                ", quantity=" + quantity +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", couponActivity=" + couponActivity +
                '}';
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public List<CouponActivityDTO> getCouponActivity() {
        return couponActivity;
    }

    public void setCouponActivity(List<CouponActivityDTO> couponActivity) {
        this.couponActivity = couponActivity;
    }

    @Override
    public Object createVo() {
        return this;
    }

    @Override
    public Object createSimpleVo() {
        return this;
    }
}
