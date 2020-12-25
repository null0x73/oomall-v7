package cn.edu.xmu.other.service;

import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.SharePoDao;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author 潘登 24320182203249
 */
@Service
public class ShareService {
    private Logger logger = LoggerFactory.getLogger(ShareService.class);

    @Autowired
    private SharePoDao sharePoDao;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    private IGoodsService goodsService;

    /**
     * 插入分享
     *
     * @author 潘登 24320182203249
     * @param userId
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject<VoObject> insertShare(Long userId,Long id){

        //TODO: 去商品模块查询sku是否存在
        if(id==1111111111111L){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        return sharePoDao.insertShare(userId, id);
    }

    /**
     * 用具查找分享记录
     *
     * @param userId
     * @param skuId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional
    public ReturnObject<PageInfo<VoObject>> searchByUser(Long userId,Long skuId,LocalDateTime beginTime,
                                                         LocalDateTime endTime,Integer page,Integer pageSize){
        return sharePoDao.searchByUser(userId, skuId, beginTime, endTime, page, pageSize);
    }

    /**
     * 管理员查找分享记录
     *
     * @author 潘登
     * @param did
     * @param skuId
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional
    public ReturnObject<PageInfo<VoObject>> searchByAdmin(Long did,
                                                          Long skuId,
                                                          Integer page,
                                                          Integer pageSize){
        //TODO: 检查id（shopId）但是好像没有用例检测
        return sharePoDao.searchByAdmin(did,skuId, page, pageSize);
    }
}
