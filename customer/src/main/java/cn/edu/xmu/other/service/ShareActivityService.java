package cn.edu.xmu.other.service;

import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.goods.client.dubbo.SpuDTO;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.ShareActivityDao;
import cn.edu.xmu.other.model.bo.ShareActivity;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分享活动服务
 * @author 潘登 24320182203249
 *
 */
@Service
public class ShareActivityService {
    private Logger logger = LoggerFactory.getLogger(ShareActivityService.class);

    @Autowired
    ShareActivityDao shareActivityDao;

    /*@DubboReference(version = "0.0.1-SNAPSHOT")
    IGoodsService goodsService;*/
    /**
     * 创造分享活动
     * @author 潘登 24320182203249
     * @param shareActivity
     * @return
     */
    @Transactional
    public ReturnObject<VoObject> createShareActivity(ShareActivity shareActivity){
        //TODO: 校验sku是否属于店铺
        /*SkuDTO skuDTO=goodsService.getSku(shareActivity.getGoodsSkuId());
        if(skuDTO==null||skuDTO.getGoodsSpuId()==null)
        {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }
        else
        {
            SpuDTO spuDTO=goodsService.getSimpleSpuById(skuDTO.getGoodsSpuId());
            if(!shareActivity.getShopId().equals(spuDTO.getShopId()))
            {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());
            }
        }*/
        return shareActivityDao.insertShareActivity(shareActivity);
    }

    /**
     * 查找分享活动
     * @author 潘登 24320182203249
     * @param shopId 商店id
     * @param spuId spuId
     * @param pageNum 页数
     * @param pageSize 页大小
     * @return 返回值
     */
    @Transactional
    public ReturnObject<PageInfo<VoObject>> selectShareActivities(Long shopId,Long spuId,Integer pageNum,Integer pageSize){
        return shareActivityDao.searchShareActivities(shopId,spuId,pageNum,pageSize);
    }

    /**
     * 更新分享活动
     * @author 潘登 24320182203249
     * @param shareActivity 分享的活动
     * @return 返回值
     */
    @Transactional
    public ReturnObject<Object> updateShareActivity(ShareActivity shareActivity){
        return shareActivityDao.updateShareActivity(shareActivity);
    }

    /**
     * 下线商品
     * @author 潘登 24320182203249
     * @param shopId
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject<Object> offlineShareActivity(Long shopId,Long id){
        return shareActivityDao.offlineShareActivity(shopId,id);
    }

    /**
     * 下线商品
     * @author 潘登 24320182203249
     * @param shopId
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject<Object> onlineShareActivity(Long shopId,Long id){
        return shareActivityDao.onlineShareActivity(shopId,id);
    }
}
