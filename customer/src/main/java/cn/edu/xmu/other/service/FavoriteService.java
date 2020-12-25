package cn.edu.xmu.other.service;

import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.FavoriteDao;
import cn.edu.xmu.other.model.bo.Favorite;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/1 10:02
 */
@Service
public class FavoriteService {
    private Logger logger = LoggerFactory.getLogger(FavoriteService.class);

    @Autowired
    private FavoriteDao favoriteDao;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    IGoodsService goodsService;

    /**
     * 增加一条收藏
     *
     * @author 何明祥 24320182203193
     * @param favorite 收藏bo
     * @return ReturnObject<Object> 新增结果
     * createBy 何明祥 2020/11/30 19:51
     */
    @Transactional
    public ReturnObject insertFavorite(Favorite favorite) {

        if(goodsService.getSku(favorite.getGoodsSkuId()).getId()==null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        ReturnObject<Object> retObj = favoriteDao.insertFavorite(favorite);
        return retObj;
    }

    /**
     * 分页查询所有收藏商品
     *
     * @author 何明祥 24320182203193
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @param customerId 用户id
     * @return ReturnObject<PageInfo<VoObject>>
     * createBy 何明祥 2020/11/30 20:20
     */
    @Transactional
    public ReturnObject<PageInfo<VoObject>> selectAllFavorites(Long customerId , Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = favoriteDao.selectAllFavorites(customerId , pageNum, pageSize);
        return returnObject;
    }

    /**
     * 用户删除某个收藏商品
     *
     * @author 何明祥 24320182203193
     * @param id 收藏商品id
     * @return ReturnObject<Object>
     * createBy 何明祥 2020/12/1 10:02
     */
    @Transactional
    public ReturnObject deleteFavorite(Long id,Long customerId)
    {
        return favoriteDao.deleteFavorite(id,customerId);
    }


}
