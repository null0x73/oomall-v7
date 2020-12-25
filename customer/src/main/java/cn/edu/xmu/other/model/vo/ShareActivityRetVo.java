package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.ShareActivity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分享活动视图对象
 * @author 潘登 24320182203249
 *
 */
@Data
@ApiModel("分享活动视图对象")
public class ShareActivityRetVo {
    @ApiModelProperty(value="活动id")
    private Long id;
    @ApiModelProperty(value="商铺id")
    private Long shopId;
    @ApiModelProperty(value="商品id")
    private Long skuId;
    @ApiModelProperty(value="开始时间")
    private String beginTime;
    @ApiModelProperty(value="结束时间")
    private String endTime;
    @ApiModelProperty(value="状态")
    private Byte state;

    /**
     * @author 潘登 24320182203249
     * @param shareActivity bo
     */
    public ShareActivityRetVo(ShareActivity shareActivity){
        this.id=shareActivity.getId();
        this.shopId=shareActivity.getShopId();
        this.skuId=shareActivity.getGoodsSkuId();
        this.beginTime=shareActivity.getBeginTime().toString();
        this.endTime=shareActivity.getEndTime().toString();
        this.state=shareActivity.getState();
        /*Integer state=1;
        if(shareActivity.getBeDeleted()!=null&&shareActivity.getBeDeleted().intValue()==1)
            state=0;
        *//*
        *state:
        * 0-无记录
        * 1-正常
        * 2-未开始
        * 3-已结束
         *//*
        LocalDateTime now=LocalDateTime.now();
        if(now.isBefore(shareActivity.getBeginTime()))state=2;
        else if(now.isAfter(shareActivity.getEndTime()))state=3;
        this.state=state;*/
    }
}
