package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.goods.client.dubbo.CouponActivityDTO;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.vo.CartItemReturnVo;

import java.time.LocalDateTime;
import java.util.List;

public class CartItem implements VoObject {

    private Long id;

    private Long goodsSkuId;

    private String skuName;

    private Integer quantity;

    private Long price;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private List<CouponActivityDTO> couponActivity;


    public CartItem() {
    }


    public CartItemReturnVo createReturnVo(){
        CartItemReturnVo vo = new CartItemReturnVo();
        vo.setId(this.id);
        vo.setGoodsSkuId(this.goodsSkuId);
        vo.setQuantity(this.quantity);
        vo.setPrice(this.price);
        vo.setSkuName(this.skuName);
        vo.setGmtCreate(this.gmtCreate);
        vo.setGmtModified(this.gmtModified);
        vo.setCouponActivity(this.couponActivity);
        return vo;
    }

    @Override
    public CartItemReturnVo createVo() {
        return this.createReturnVo();
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
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
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", goodsSkuId=" + goodsSkuId +
                ", skuName='" + skuName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", couponActivity=" + couponActivity +
                '}';
    }
}
