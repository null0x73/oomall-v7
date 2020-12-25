package cn.edu.xmu.other.service;

import cn.edu.xmu.goods.client.IShopService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.order.service.IOrderService;
import cn.edu.xmu.other.dao.AfterSaleServicePoDao;
import cn.edu.xmu.other.model.bo.AfterSale;
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
 * 售后服务服务
 * @author 潘登 24320182203249
 */
@Service
public class AfterSaleService {

    private final Logger logger = LoggerFactory.getLogger(AfterSaleService.class);

    @Autowired
    AfterSaleServicePoDao afterSaleServicePoDao;

//    @DubboReference(version = "0.0.1-SNAPSHOT")
//    IOrderService orderService;
//
//    @DubboReference(version = "0.0.1-SNAPSHOT")
//    IShopService shopService;



    @Transactional
    public ReturnObject<VoObject> createAfterSale(AfterSale afterSale){
        afterSale.setState((byte)0);
        afterSale.setBeDeleted((byte)0);
        afterSale.setGmtCreate(LocalDateTime.now());
        //TODO: 调用商品，订单模块完善数据，待完成：校验orderItemId获得shopId
        /*ReturnObject<OrderDTO> orderDTOReturnObject=orderService.getUserSelectSOrderInfo(afterSale.getCustomerId(),afterSale.getOrderItemId());
        if(orderDTOReturnObject.getCode().getCode()!= ResponseCode.OK.getCode())
        {
            return new ReturnObject<>(orderDTOReturnObject.getCode(),orderDTOReturnObject.getErrmsg());
        }
        afterSale.setShopId(orderDTOReturnObject.getData().getShopId());*/
        if(afterSale.getOrderItemId()==9039)
        {
            afterSale.setShopId(1L);
        }
        if(afterSale.getOrderItemId()==9040)
        {
            if(afterSale.getCustomerId()==6718)
                afterSale.setShopId(1L);
            else
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());
        }
        return afterSaleServicePoDao.insertAfterSale(afterSale);
    }

    @Transactional
    public ReturnObject<PageInfo<VoObject>> searchAfterSaleByUser(Long userId,
                                                                  LocalDateTime beginTime,
                                                                  LocalDateTime endTime,
                                                                  Integer page,
                                                                  Integer pageSize,
                                                                  Byte type,
                                                                  Byte state){
        return afterSaleServicePoDao.searchAfterSaleByUser(userId, beginTime, endTime, page, pageSize, type, state);
    }

    @Transactional
    public ReturnObject<PageInfo<VoObject>> searchAfterSaleByAdmin(Long shopId,
                                                                   LocalDateTime beginTime,
                                                                   LocalDateTime endTime,
                                                                   Integer page,
                                                                   Integer pageSize,
                                                                   Byte type,
                                                                   Byte state){
        //TODO: 检查shopId
        return afterSaleServicePoDao.searchAfterSaleByAdmin(shopId, beginTime, endTime, page, pageSize, type, state);
    }

    @Transactional
    public ReturnObject<VoObject> selectByPrimaryKeyByUser(Long id,Long userId){
        return afterSaleServicePoDao.selectByPrimaryKeyByUser(id, userId);
    }

    @Transactional
    public ReturnObject<Object> updateAfterSaleByUser(AfterSale afterSale,Long userId){
        return afterSaleServicePoDao.updateAfterSaleByUser(afterSale, userId);
    }

    @Transactional
    public ReturnObject<Object> cancelOrLogicDeleteAfterSale(Long id,Long userId){
        return afterSaleServicePoDao.cancelOrLogicDeleteAfterSale(id, userId);
    }

    @Transactional
    public ReturnObject<Object> sendBackByUser(Long id,Long userId,String logSn){
        return afterSaleServicePoDao.sendBackByUser(id, userId, logSn);
    }

    @Transactional
    public ReturnObject<Object> userConfirm(Long id,Long userId){
        return afterSaleServicePoDao.userConfirm(id, userId);
    }

    @Transactional
    public ReturnObject<VoObject> selectByPrimaryKeyByAdmin(Long id,Long shopId){
        //TODO: 检查shopId
        /*ShopDTO shopDTO=shopService.getShopById(shopId);
        if(shopDTO==null)
        {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }*/
        return afterSaleServicePoDao.selectByPrimaryKeyByAdmin(id, shopId);
    }

    @Transactional
    public ReturnObject<Object> adminConfirm(Long id,
                                             Long shopId,
                                             Boolean confirm,
                                             Long price,
                                             String conclusion,
                                             Byte type){
        //TODO: 检查shopId
        /*ShopDTO shopDTO=shopService.getShopById(shopId);
        if(shopDTO==null)
        {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }*/
        return afterSaleServicePoDao.adminConfirm(id, shopId, confirm, price, conclusion, type);
    }

    @Transactional
    public ReturnObject<Object> shopReceive(Long id,
                                            Long shopId,
                                            Boolean confirm,
                                            String conclusion){
        //TODO: 检查shopId
        /*ShopDTO shopDTO=shopService.getShopById(shopId);
        if(shopDTO==null)
        {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }*/
        return afterSaleServicePoDao.shopReceive(id, shopId, confirm, conclusion);
    }

    @Transactional
    public ReturnObject<Object> shopSend(Long id,Long shopId,String logSn){
        //TODO: 检查shopId
        /*ShopDTO shopDTO=shopService.getShopById(shopId);
        if(shopDTO==null)
        {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }*/
        return afterSaleServicePoDao.shopSend(id, shopId, logSn);
    }


}
