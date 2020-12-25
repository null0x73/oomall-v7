package cn.edu.xmu.other.dao;

import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.SharePoMapper;
import cn.edu.xmu.other.model.bo.Share;
import cn.edu.xmu.other.model.bo.ShareActivity;
import cn.edu.xmu.other.model.po.SharePo;
import cn.edu.xmu.other.model.po.SharePoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 潘登 24320182203249
 */
@Repository
public class SharePoDao {
    private static final Logger logger = LoggerFactory.getLogger(SharePoDao.class);

    @Autowired
    private SharePoMapper sharePoMapper;

    @Autowired
    private ShareActivityDao shareActivityDao;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    private IGoodsService goodsService;


    private void setShareSku(Share share){
        //TODO: 集成商品模块
        SkuDTO skuDTO=goodsService.getSku(share.getGoodsSkuId());
        share.getSku().setDisable(skuDTO.getDisable()!=null&&skuDTO.getDisable()==1);
        share.getSku().setId(skuDTO.getId());
        share.getSku().setImageUrl(skuDTO.getImageUrl());
        share.getSku().setInventory(skuDTO.getInventory());
        share.getSku().setName(skuDTO.getName());
        share.getSku().setOriginalPrice(skuDTO.getOriginalPrice());
        share.getSku().setPrice(skuDTO.getPrice());
        share.getSku().setSkuSn(skuDTO.getSkuSn());
    }
    /**
     * 插入分享记录
     *
     * @author 潘登
     * @param id skuId
     * @parm userId 用户id
     * @return
     */
    //TODO: 改成返回VoObject，Controller层用用getObj替换
    public ReturnObject<VoObject> insertShare(Long userId,Long id){
        ReturnObject<VoObject> retObj=null;
        ShareActivity shareActivity=shareActivityDao.getCurrentShareActivityById(id);
        if(shareActivity==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"当前无分享活动可用");
        Share share=new Share();
        share.setGoodsSkuId(id);
        share.setSharerId(userId);
        share.setGmtCreate(LocalDateTime.now());
        share.setQuantity(0);
        share.setShareActivityId(shareActivity.getId());
        try{
            SharePo sharePo=share.gotSharePo();
            int ret=sharePoMapper.insertSelective(sharePo);
            if(ret==0){
                logger.debug("insert share falied ");
                retObj=new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("新增失败"+sharePo.getId()));
            }
            else{
                logger.debug("insert share "+sharePo.toString());
                share=new Share(sharePo);
                setShareSku(share);
                retObj=new ReturnObject<>(share);
            }
        }catch(DataAccessException e){
            logger.debug("insert share falied, data access erro exits "+share.toString());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 买家查询分享记录
     *
     * @author 潘登 24320182203249
     * @param userId 用户id，此项必须不为空
     * @param skuId 商品id
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param page 页号
     * @param pageSize 页面大小
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> searchByUser(Long userId,Long skuId,LocalDateTime beginTime,
                                                             LocalDateTime endTime,Integer page,Integer pageSize){
        PageHelper.startPage(page, pageSize);
        logger.debug("page = " + page + "pageSize = " + pageSize);
        SharePoExample example=new SharePoExample();
        SharePoExample.Criteria criteria=example.createCriteria();
        criteria.andSharerIdEqualTo(userId);
        if(skuId!=null)criteria.andGoodsSkuIdEqualTo(skuId);
        if(beginTime!=null)criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        if(endTime!=null)criteria.andGmtCreateLessThanOrEqualTo(endTime);
        List<SharePo> sharePos=null;
        try {
            sharePos = sharePoMapper.selectByExample(example);
        }catch (DataAccessException e){
            logger.error("selectShare: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e){
            logger.error("selectShare: Unknow Exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("其他错误 ：%s", e.getMessage()));
        }
        List<VoObject> ret=new ArrayList<>(sharePos.size());
        for(SharePo sharePo:sharePos){
            Share share=new Share(sharePo);
            setShareSku(share);
            ret.add(share);
        }
        PageInfo<VoObject> sharePages=PageInfo.of(ret);
        return new ReturnObject<>(sharePages);
    }

    /**
     * 管理员查找分享记录
     *
     * @author 潘登
     * @param did shopId(商店id)
     * @param skuId
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> searchByAdmin(Long did,
                                                         Long skuId,
                                                         Integer page,
                                                         Integer pageSize){
        PageHelper.startPage(page, pageSize);
        logger.debug("page = " + page + "pageSize = " + pageSize);
        //List<Long> shareActivityIds=shareActivityDao.getShareActivityIdsByShopId(id,skuId);
        SharePoExample example=new SharePoExample();
        SharePoExample.Criteria criteria=example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(skuId);
        //criteria.andShareActivityIdIn(shareActivityIds);

        List<SharePo> sharePos=null;
        List<VoObject> ret=new ArrayList<>();
        sharePos=sharePoMapper.selectByExample(example);

        for(SharePo sharePo:sharePos){
            Share share=new Share(sharePo);
            setShareSku(share);
            ret.add(share);
        }
        PageInfo<VoObject> sharePages=PageInfo.of(ret);
        return new ReturnObject<>(sharePages);
    }
}
