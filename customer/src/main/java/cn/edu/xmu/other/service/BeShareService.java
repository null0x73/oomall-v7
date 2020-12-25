package cn.edu.xmu.other.service;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.BeSharedPoDao;
import com.github.pagehelper.PageInfo;
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
public class BeShareService {
    private Logger logger = LoggerFactory.getLogger(BeShareService.class);

    @Autowired
    private BeSharedPoDao beSharedPoDao;

    /**
     * 用户查询被分享成功记录
     *
     * @author 潘登 24320182203249
     *
     * @param userId 被分享者id
     * @param skuId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional
    public ReturnObject<PageInfo<VoObject>> searchByUser(Long userId, Long skuId, LocalDateTime beginTime,
                                                         LocalDateTime endTime, Integer page, Integer pageSize){
        return beSharedPoDao.searchByUser(userId, skuId, beginTime, endTime, page, pageSize);
    }

    @Transactional
    public ReturnObject<PageInfo<VoObject>> searchByAdmin(Long did,
                                                          Long skuId,
                                                          LocalDateTime beginTime,
                                                          LocalDateTime endTime,
                                                          Integer page,
                                                          Integer pageSize){
        //TODO: 校验商店和sku是否对的上
        return beSharedPoDao.searchByAdmin(did, skuId, beginTime,endTime,page, pageSize);
    }
}
