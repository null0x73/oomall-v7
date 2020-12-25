package cn.edu.xmu.other.dao;

import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.FavoritePoMapper;
import cn.edu.xmu.other.model.bo.Favorite;
import cn.edu.xmu.other.model.po.FavoritePo;
import cn.edu.xmu.other.model.po.FavoritePoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/11/30 19:51
 */
@Repository
public class FavoriteDao {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteDao.class);

    @Autowired
    private FavoritePoMapper favoritePoMapper;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    private IGoodsService goodsService;

    /**
     * 增加一条收藏
     *
     * @author 何明祥 24320182203193
     * @param favorite 收藏bo
     * @return ReturnObject<Favorite> 新增结果
     * createBy 何明祥 2020/11/30 19:51
     */
    public ReturnObject<Object> insertFavorite(Favorite favorite) {
        FavoritePoExample example = new FavoritePoExample();
        FavoritePoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(favorite.getCustomerId());
        criteria.andGoodsSkuIdEqualTo(favorite.getGoodsSkuId());
        List<FavoritePo> favoritePos = favoritePoMapper.selectByExample(example);
        if(favoritePos.size()>0){
            Favorite favorite1=new Favorite(favoritePos.get(0));

            SkuDTO skuDTO = goodsService.getSku(favorite.getGoodsSkuId());
//            SkuDTO skuDTO = new SkuDTO();
//            skuDTO.setName("+");
//            skuDTO.setId(273L);                        // TODO: Check RPC here
//            skuDTO.setImageUrl("http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg");
//            skuDTO.setOriginalPrice(980000L);
//            skuDTO.setPrice(980000L);
//            skuDTO.setInventory(1);
//            Integer ds=0;
//            skuDTO.setDisable(ds.byteValue());Null

            favorite1.setSkuDTO(skuDTO);
            return new ReturnObject<>(favorite1);
        }

        System.out.println("202012241716 MARK 1");

        SkuDTO skuDTO = goodsService.getSku(favorite.getGoodsSkuId());
        if(skuDTO==null){
            System.out.println("202012241716 MARK 2");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("商品ID不存在"));
        }
        System.out.println("202012241716 MARK 3");





//        if(favorite.getGoodsSkuId().equals(12345678L))
//            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("商品ID不存在"));


//        SkuDTO skuDTO=new SkuDTO();
//        skuDTO.setName("+");
//        skuDTO.setId(273L);                        // TODO: Check RPC here
//        skuDTO.setImageUrl("http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg");
//        skuDTO.setOriginalPrice(980000L);
//        skuDTO.setPrice(980000L);
//        skuDTO.setInventory(1);
//        Integer ds=0;
//        skuDTO.setDisable(ds.byteValue());

        FavoritePo favoritePo = favorite.gotFavoritePo();
        ReturnObject<Object> retObj = null;

        int ret = favoritePoMapper.insertSelective(favoritePo);
        if (ret == 0) {
            //插入失败
            logger.debug("insertFavorite: insert favorite fail " + favoritePo.toString());
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败,id：" + favoritePo.getId()));
        } else {
            //插入成功
            logger.debug("insertFavorite: insert favorite = " + favoritePo.toString());
            favorite.setId(favoritePo.getId());
            favorite.setSkuDTO(skuDTO);
            retObj = new ReturnObject<>(favorite);
        }

        return retObj;
    }



    /**
     * 分页查询所有收藏商品
     *
     * @author 何明祥 24320182203193
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @param customerId 用户id
     * @return ReturnObject<List> 收藏商品列表
     * createBy 何明祥 2020/11/30 20:20
     */
    public ReturnObject<PageInfo<VoObject>> selectAllFavorites(Long customerId, Integer pageNum, Integer pageSize) {
        FavoritePoExample example = new FavoritePoExample();
        FavoritePoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        //分页查询
        PageHelper.startPage(pageNum, pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        List<FavoritePo> favoritePos = null;
            //不加限定条件查询所有
            favoritePos = favoritePoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(favoritePos.size());
            for (FavoritePo po : favoritePos) {
                Favorite favorite = new Favorite(po);

                SkuDTO skuDTO = goodsService.getSku(favorite.getGoodsSkuId());
//                SkuDTO skuDTO = new SkuDTO();
//                skuDTO.setName("+");
//                skuDTO.setId(273L);                        // TODO: Check RPC here
//                skuDTO.setImageUrl("http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg");
//                skuDTO.setOriginalPrice(980000L);
//                skuDTO.setPrice(980000L);
//                skuDTO.setInventory(1);
//                Integer ds=0;
//                skuDTO.setDisable(ds.byteValue());

                favorite.setSkuDTO(skuDTO);
                ret.add(favorite);
            }
        PageInfo<FavoritePo> favoritesPoPageInfo = PageInfo.of(favoritePos);
        PageInfo<VoObject> favoritePage = new PageInfo<>(ret);
        favoritePage.setTotal(favoritesPoPageInfo.getTotal());
        favoritePage.setPageSize(favoritesPoPageInfo.getPageSize());
        favoritePage.setPageNum(favoritesPoPageInfo.getPageNum());
        favoritePage.setPages(favoritesPoPageInfo.getPages());
        return new ReturnObject<>(favoritePage);
    }

    /**
     * 用户删除某个收藏商品
     *
     * @author 何明祥 24320182203193
     * @param id 收藏商品id
     * @return ReturnObject<Object>
     * createBy 何明祥 2020/12/1 10:02
     */
    public ReturnObject<Object> deleteFavorite(Long id,Long customerId) {
        ReturnObject<Object> retObj;
        FavoritePo favoritePo=favoritePoMapper.selectByPrimaryKey(id);
        if(favoritePo==null)
        {
            logger.info("收藏不存在或已被删除,id:"+id);
           return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(!favoritePo.getCustomerId().equals(customerId))
        {
            logger.info("该收藏不属于该用户,id:"+id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        int ret = favoritePoMapper.deleteByPrimaryKey(id);
        if (ret == 0) {
            logger.info("收藏不存在或已被删除,id:"+id);
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("收藏"+id+"已被永久删除");
            retObj = new ReturnObject<>();
        }
        return retObj;
    }


}
