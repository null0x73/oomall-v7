package cn.edu.xmu.other.dao;


import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.BeSharePoMapper;
import cn.edu.xmu.other.model.bo.BeShared;
import cn.edu.xmu.other.model.bo.Share;
import cn.edu.xmu.other.model.po.BeSharePo;
import cn.edu.xmu.other.model.po.BeSharePoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BeSharedPoDao {
    private static final Logger logger = LoggerFactory.getLogger(BeSharedPoDao.class);

    @Autowired
    private BeSharePoMapper beSharePoMapper;

    @Autowired
    private ShareActivityDao shareActivityDao;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    private IGoodsService goodsService;

    private void setShareSku(BeShared beshared){
        SkuDTO skuDTO=goodsService.getSku(beshared.getGoodsSkuId());
        beshared.getSku().setDisable(skuDTO.getDisable()!=null&&skuDTO.getDisable()==1);
        beshared.getSku().setId(skuDTO.getId());
        beshared.getSku().setImageUrl(skuDTO.getImageUrl());
        beshared.getSku().setInventory(skuDTO.getInventory());
        beshared.getSku().setName(skuDTO.getName());
        beshared.getSku().setOriginalPrice(skuDTO.getOriginalPrice());
        beshared.getSku().setPrice(skuDTO.getPrice());
        beshared.getSku().setSkuSn(skuDTO.getSkuSn());
    }

    /**
     * 用户查找被分享成功记录
     *
     * @author 潘登 24320182203249
     * @param userId cutomerId 点击分享链接的人的id
     * @param skuId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> searchByUser(Long userId, Long skuId, LocalDateTime beginTime,
                                                         LocalDateTime endTime, Integer page, Integer pageSize){
        PageHelper.startPage(page, pageSize);
        logger.debug("page = " + page + "pageSize = " + pageSize);
        BeSharePoExample example=new BeSharePoExample();
        BeSharePoExample.Criteria criteria=example.createCriteria();
        criteria.andSharerIdEqualTo(userId);
        if(skuId!=null)criteria.andGoodsSkuIdEqualTo(skuId);
        if(beginTime!=null)criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        if(endTime!=null)criteria.andGmtCreateLessThanOrEqualTo(endTime);
        List<BeSharePo> beSharePos=null;
        try{
            beSharePos=beSharePoMapper.selectByExample(example);
        }catch (DataAccessException e){
            logger.error("selectShare: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e){
            logger.error("selectShare: Unknow Exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("其他错误 ：%s", e.getMessage()));
        }
        List<VoObject> ret=new ArrayList<>(beSharePos.size());
        for(BeSharePo po:beSharePos){
            BeShared beShared=new BeShared(po);
            setShareSku(beShared);
            ret.add(beShared);
        }
        PageInfo<VoObject> beSharePages=PageInfo.of(ret);
        return new ReturnObject<>(beSharePages);
    }

    /**
     * 管理员查询被分享成功记录
     *
     * @author 潘登
     * @param did
     * @param skuId
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> searchByAdmin(Long did,
                                                          Long skuId,
                                                          LocalDateTime beginTime,
                                                          LocalDateTime endTime,
                                                          Integer page,
                                                          Integer pageSize){
        PageHelper.startPage(page, pageSize);
        //TODO: 改造
        logger.debug("page = " + page + "pageSize = " + pageSize);
        //List<Long> shareActivityIds=shareActivityDao.getShareActivityIdsByShopId(did,skuId);
        BeSharePoExample example=new BeSharePoExample();
        List<BeSharePo> beSharePos=null;
        List<VoObject> ret=new ArrayList<>();
        try{

            BeSharePoExample.Criteria criteria=example.createCriteria();
            //criteria.andShareActivityIdIn(shareActivityIds);
            criteria.andGoodsSkuIdEqualTo(skuId);
            if(beginTime!=null) criteria.andGmtCreateGreaterThan(beginTime);
            if(endTime!=null) criteria.andGmtCreateLessThanOrEqualTo(endTime);
            beSharePos=beSharePoMapper.selectByExample(example);
            for(BeSharePo po:beSharePos){
                BeShared beShared=new BeShared(po);
                setShareSku(beShared);
                ret.add(beShared);
            }


        }catch (DataAccessException e){
            logger.error("selectShare: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e){
            logger.error("selectShare: Unknow Exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("其他错误 ：%s", e.getMessage()));
        }
        PageInfo<VoObject> beSharePage=PageInfo.of(ret);
        /*beSharePage.setPageNum(page);
        beSharePage.setPageSize(pageSize);*/
        return new ReturnObject<>(beSharePage);
    }
}
