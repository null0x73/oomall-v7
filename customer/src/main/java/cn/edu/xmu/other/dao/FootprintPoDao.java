package cn.edu.xmu.other.dao;

import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.IShopService;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.FootprintPoMapper;
import cn.edu.xmu.other.model.bo.Footprint;
import cn.edu.xmu.other.model.po.FootprintPo;
import cn.edu.xmu.other.model.po.FootprintPoExample;
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

@Repository
public class FootprintPoDao {
    private static final Logger logger = LoggerFactory.getLogger(FootprintPoDao.class);

    @Autowired
    private FootprintPoMapper footprintPoMapper;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    IGoodsService goodsService;


    /**
     * 查找符合条件的信息
     *
     * author 潘登 24320182203249
     * @param pageNum 页号
     * @param pageSize 页大小
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param userId 用户id
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> search(Long did,Long userId, LocalDateTime beginTime, LocalDateTime endTime, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        FootprintPoExample example=new FootprintPoExample();
        FootprintPoExample.Criteria criteria =example.createCriteria();
        //PageHelper.startPage(pageNum, pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        List<FootprintPo> footprintPos= null;
        //criteria.andCustomerIdEqualTo(userId).andGmtCreatedBetween(beginTime,endTime);
        if(userId!=null)criteria.andCustomerIdEqualTo(userId);
        if(beginTime!=null)criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        if(endTime!=null)criteria.andGmtCreateLessThanOrEqualTo(endTime);
        try{
            footprintPos=footprintPoMapper.selectByExample(example);
        }catch (DataAccessException e){
            logger.error("selectFootprint: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        List<VoObject> ret = new ArrayList<>(footprintPos.size());
        for(FootprintPo po:footprintPos){
            //TODO: 集成商品模块，根据判断是否属于店铺，属于才插入


                Footprint ft = new Footprint(po);
                SkuDTO skuDTO=goodsService.getSku(po.getGoodsSkuId());
                if(skuDTO!=null) {
                    ft.getSku().setId(skuDTO.getId());
                    ft.getSku().setDisable(skuDTO.getDisable() != null && skuDTO.getDisable().intValue() != 0);
                    ft.getSku().setImageUrl(skuDTO.getImageUrl());
                    ft.getSku().setInventory(skuDTO.getInventory());
                    ft.getSku().setName(skuDTO.getName());
                    ft.getSku().setOriginalPrice(skuDTO.getOriginalPrice());
                    ft.getSku().setPrice(skuDTO.getPrice());
                    ft.getSku().setSkuSn(skuDTO.getSkuSn());
                    ret.add(ft);
                }
        }

        PageInfo<VoObject> footprintPage=PageInfo.of(ret);
        return new ReturnObject<>(footprintPage);
    }

    /**
     * @author 潘登 24320182203249
     * 仅仅用于测试
     *
     * @param id
     * @return
     */
    public Footprint selectByPrimaryKy(Long id){
        Footprint ft=new Footprint(footprintPoMapper.selectByPrimaryKey(id));
        return ft;
    }


}
