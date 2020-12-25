package cn.edu.xmu.other.service;

import cn.edu.xmu.goods.client.IShopService;
import cn.edu.xmu.goods.client.dubbo.ShopDTO;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.FootprintPoDao;
import cn.edu.xmu.other.model.bo.Footprint;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务
 * @author Deng Pan
 * Modified at 2020/11/21 10:39
 **/
@Service
public class FootprintService {

    private Logger logger = LoggerFactory.getLogger(FootprintService.class);


    @Autowired
    private FootprintPoDao footprintPoDao;

    /*@DubboReference(version = "0.0.1-SNAPSHOT")
    IShopService shopService;*/


    @Transactional
    public ReturnObject<PageInfo<VoObject>> selectFootprint(Long did,Long userId,LocalDateTime beginTime,LocalDateTime endTime,Integer pageNum,Integer pageSize){

        //TODO: 判断店铺是否存在
        /*ShopDTO shopDTO=shopService.getShopById(did);
        if(shopDTO==null)
        {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }*/
        return footprintPoDao.search(did, userId,beginTime,endTime,pageNum,pageSize);
    }
}
