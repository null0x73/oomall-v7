package cn.edu.xmu.other.model.vo;

import lombok.Data;

@Data
public class ShareSkuVo {
    Long id;
    String name;
    String skuSn;
    String imageUrl;
    Integer inventory;
    Long originalPrice;
    Long price;
    Boolean disable;
}
