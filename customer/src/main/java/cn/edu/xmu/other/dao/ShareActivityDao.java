package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.ShareActivityPoMapper;
import cn.edu.xmu.other.model.bo.ShareActivity;
import cn.edu.xmu.other.model.po.ShareActivityPo;
import cn.edu.xmu.other.model.po.ShareActivityPoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ShareActivityDao {
    private static final Logger logger = LoggerFactory.getLogger(ShareActivityDao.class);

    @Autowired
    private ShareActivityPoMapper shareActivityPoMapper;

    //@Autowired
    //private RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 根据主键查找对应po
     * @author 潘登 24320182203249
     * @param id
     * @return
     */
    public ShareActivityPo selectByPrimaryKey(Long id){
        ShareActivityPo po=null;
        po=shareActivityPoMapper.selectByPrimaryKey(id);
        return po;
    }

    /**
     * 通用的跟新
     * @author 潘登
     *
     * @param po
     * @return
     */
    private ReturnObject<Object> update(ShareActivityPo po){
        try{
            int res=shareActivityPoMapper.updateByPrimaryKeySelective(po);
            if(res==0){
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,"更新分享活动失败，原因未知");
            }
            else{
                return new ReturnObject<>();
            }
        }catch (DataAccessException e){
            logger.error("updateShareActivities: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e){
            logger.error("updateShareActivities: Unknow Exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("其他错误 ：%s", e.getMessage()));
        }
    }

    /**
     * 新建一个分享活动
     * @author 潘登 24320182203249
     *
     * @param shareActivity 分享活动bo
     */
    public ReturnObject<VoObject> insertShareActivity(ShareActivity shareActivity){
        ShareActivityPo shareActivityPo=shareActivity.gotShareActivityPo();
        ReturnObject<VoObject> retObj=null;
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andBeginTimeLessThanOrEqualTo(shareActivityPo.getBeginTime())
                .andEndTimeGreaterThanOrEqualTo(shareActivityPo.getBeginTime())
                .andGoodsSkuIdEqualTo(shareActivityPo.getGoodsSkuId());
        //example.or(criteria);
        /*ShareActivityPoExample.Criteria criteria2=example.createCriteria();
        criteria2.andBeginTimeLessThanOrEqualTo(shareActivityPo.getEndTime())
                .andEndTimeGreaterThanOrEqualTo(shareActivityPo.getEndTime())
                .andGoodsSkuIdEqualTo(shareActivityPo.getGoodsSkuId());
        example.or(criteria2);*/
        example.or()
                .andBeginTimeLessThanOrEqualTo(shareActivityPo.getEndTime())
                .andEndTimeGreaterThanOrEqualTo(shareActivityPo.getEndTime())
                .andGoodsSkuIdEqualTo(shareActivityPo.getGoodsSkuId());
        List<ShareActivityPo> pos=shareActivityPoMapper.selectByExample(example);
        if(pos.size()!=0)
        {
            retObj=new ReturnObject<>(ResponseCode.SHAREACT_CONFLICT,"分享活动时段冲突");
            return retObj;
        }
        try{
            int ret=shareActivityPoMapper.insertSelective(shareActivityPo);
            if(ret==0){
                logger.debug("insert shareActivity falied "+shareActivityPo.toString());
                retObj=new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("新增失败"+shareActivityPo.getId()));
            }
            else{
                logger.debug("insert shareActivity "+shareActivityPo.toString());
                shareActivity=new ShareActivity(shareActivityPo);
                retObj=new ReturnObject<>(shareActivity);
            }
        }catch(DataAccessException e){
            logger.debug("insert shareActivity falied, data access erro exits "+shareActivityPo.toString());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 按照条件查询分享活动
     * @author 潘登 24320182203249
     *
     * @param shopId 商店id
     * @param skuId 商品spuId
     * @param pageNum 页数
     * @param pageSize 页大小
     * @return
     * 2020/12/2 17:56
     */
    public ReturnObject<PageInfo<VoObject>> searchShareActivities(Long shopId,Long skuId,Integer pageNum,Integer pageSize){
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria =example.createCriteria();
        PageHelper.startPage(pageNum, pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);

        if(shopId!=null)criteria.andShopIdEqualTo(shopId);
        if(skuId!=null)criteria.andGoodsSkuIdEqualTo(skuId);

        List<ShareActivityPo> shareActivityPos=null;
        try{
            shareActivityPos=shareActivityPoMapper.selectByExample(example);
        }catch (DataAccessException e){
            logger.error("selectShareActivities: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e){
            logger.error("selectShareActivities: Unknow Exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("其他错误 ：%s", e.getMessage()));
        }
        List<VoObject> ret=new ArrayList<>(shareActivityPos.size());
        for(ShareActivityPo po:shareActivityPos){
            ret.add(new ShareActivity(po));
        }
        PageInfo<VoObject> shareActivityPage=PageInfo.of(ret);
        shareActivityPage.setPageNum(pageNum);
        return new ReturnObject<>(shareActivityPage);
    }

    /**
     * 跟新分享活动
     * @author 潘登 24320182203249
     * @param shareActivity 分享活动
     * @return 返回值
     */
    public ReturnObject<Object> updateShareActivity(ShareActivity shareActivity){

        ShareActivityPo po=selectByPrimaryKey(shareActivity.getId());
        if(po==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"没有这个id对应的分享活动资源");
        if(!po.getShopId().equals(shareActivity.getShopId()))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,"操作的分享活动不是当前店铺的");

        //TODO: 检查是否处在上线状态，应该没有用例会测

        //检查时间是否冲突
        ReturnObject<Object> retObj=null;
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andBeginTimeLessThanOrEqualTo(shareActivity.getBeginTime())
                .andEndTimeGreaterThanOrEqualTo(shareActivity.getBeginTime())
                .andGoodsSkuIdEqualTo(po.getGoodsSkuId());
        //example.or(criteria);
        /*ShareActivityPoExample.Criteria criteria2=example.createCriteria();
        criteria2.andBeginTimeLessThanOrEqualTo(shareActivityPo.getEndTime())
                .andEndTimeGreaterThanOrEqualTo(shareActivityPo.getEndTime())
                .andGoodsSkuIdEqualTo(shareActivityPo.getGoodsSkuId());
        example.or(criteria2);*/
        example.or()
                .andBeginTimeLessThanOrEqualTo(shareActivity.getEndTime())
                .andEndTimeGreaterThanOrEqualTo(shareActivity.getEndTime())
                .andGoodsSkuIdEqualTo(po.getGoodsSkuId());
        List<ShareActivityPo> pos=shareActivityPoMapper.selectByExample(example);
        if(pos.size()!=0)
        {
            retObj=new ReturnObject<>(ResponseCode.SHAREACT_CONFLICT,"分享活动时段冲突");
            return retObj;
        }


        //下面的状态码需要更新
        if(po.getState()!=null&&po.getState().intValue()!=0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,"活动处在上线状态");
        po.setBeginTime(shareActivity.getBeginTime());
        po.setEndTime(shareActivity.getEndTime());
        po.setStrategy(shareActivity.getStrategy());
        po.setGmtModified(LocalDateTime.now());
        return update(po);
    }

    /**
     * 下线活动
     * @author 潘登 24320182203249
     * @param shopId 商店id
     * @param id 分享活动id
     * @return 返回值
     */
    public ReturnObject<Object> offlineShareActivity(Long shopId,Long id){
        ShareActivityPo po=selectByPrimaryKey(id);
        if(po==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"没有这个id对应的分享活动资源");
        if(!po.getShopId().equals(shopId))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,"操作的分享活动不是当前店铺的");
        po.setState((byte) 0);
        po.setGmtModified(LocalDateTime.now());
        return update(po);
    }

    /**
     * 上线活动
     * @author 潘登 24320182203249
     * @param shopId 商店id
     * @param id 分享活动id
     * @return
     */
    public ReturnObject<Object> onlineShareActivity(Long shopId,Long id){
        ShareActivityPo po=selectByPrimaryKey(id);
        if(po==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"没有这个id对应的分享活动资源");
        if(!po.getShopId().equals(shopId))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,"操作的分享活动不是当前店铺的");
        po.setGmtModified(LocalDateTime.now());
        po.setState((byte) 1);
        return update(po);
    }

    /**
     * 根据skuId得到当前正在进行的活动
     *
     * @author 潘登
     * @param skuId
     * @return 对应的分享活动
     */
    public ShareActivity getCurrentShareActivityById(Long skuId){
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(skuId)
                .andBeginTimeLessThanOrEqualTo(LocalDateTime.now())
                .andEndTimeGreaterThanOrEqualTo(LocalDateTime.now());
        List<ShareActivityPo> shareActivityPos=shareActivityPoMapper.selectByExample(example);
        if(shareActivityPos==null||shareActivityPos.size()==0)
            return null;
        return new ShareActivity(shareActivityPos.get(0));
    }

    /**
     * @auhtor 潘登
     *
     * @param shopId
     * @param skuId
     * @return
     */
    public List<Long> getShareActivityIdsByShopId(Long shopId,Long skuId){
        List<Long> ret=null;
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        if(skuId!=null)criteria.andGoodsSkuIdEqualTo(skuId);
        List<ShareActivityPo> shareActivityPos=shareActivityPoMapper.selectByExample(example);
        ret=new ArrayList<>(shareActivityPos.size());
        for(ShareActivityPo po:shareActivityPos){
            ret.add(po.getId());
        }
        return ret;
    }
}
