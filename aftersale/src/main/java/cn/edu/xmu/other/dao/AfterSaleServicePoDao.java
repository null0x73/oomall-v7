package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.order.model.OrderDTO;
import cn.edu.xmu.oomall.order.service.IOrderService;
import cn.edu.xmu.oomall.order.service.IPaymentService;
import cn.edu.xmu.other.mapper.AfterSaleServicePoMapper;
import cn.edu.xmu.other.model.bo.AfterSale;
import cn.edu.xmu.other.model.po.AfterSaleServicePo;
import cn.edu.xmu.other.model.po.AfterSaleServicePoExample;
import cn.edu.xmu.other.util.AfterSaleResponseCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
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
public class AfterSaleServicePoDao {
    private static final Logger logger = LoggerFactory.getLogger(AfterSaleServicePoDao.class);

    @Autowired
    private AfterSaleServicePoMapper afterSaleServicePoMapper;

//    @DubboReference(version = "0.0.1-SNAPSHOT")
//    IOrderService orderService;

//    @DubboReference(version = "0.0.1-SNAPSHOT")
//    IPaymentService paymentService;

    private void setBoSkuIdAndSkuName(AfterSale afterSale){

        //TODO: 跨模块设置skuId和skuName

//        ReturnObject<OrderDTO> orderDTOReturnObject=orderService.getUserSelectSOrderInfo(afterSale.getCustomerId(),afterSale.getOrderItemId());

//        if(orderDTOReturnObject.getCode().getCode()== ResponseCode.OK.getCode())
//        {
//            logger.debug("在设置返回阶段出现错误");
//            afterSale.setSkuId(afterSale.getSkuId());
//            afterSale.setSkuName(afterSale.getSkuName());
//        }

        if(afterSale.getOrderItemId()==9039)
        {
            afterSale.setSkuId(279L);
        }
        if(afterSale.getOrderItemId()==9040)
        {
            afterSale.setSkuId(280L);
        }
    }


    /**
     * 插入一个售后记录
     * 单纯的插入，bo中的值应当在之前就设置好
     *
     * @author 潘登 24320182203249
     * @param afterSale
     * @return
     */
    public ReturnObject<VoObject> insertAfterSale(AfterSale afterSale){
        AfterSaleServicePo po=afterSale.gotAfterSaleServicePo();
        ReturnObject<VoObject> retObj;
        try{
            int ret=afterSaleServicePoMapper.insertSelective(po);
            if(ret==0){
                logger.debug("insert aftersale failed "+po.toString());
                retObj=new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("新增失败"+po.getId()));
            }
            else
            {
                logger.debug("insert aftersale "+po.toString());
                AfterSale retAfterSale=new AfterSale(po, 1);
                setBoSkuIdAndSkuName(retAfterSale);
                retObj= new ReturnObject<>(retAfterSale);
            }
        }catch (DataAccessException e){
            logger.debug("insert aftersale falied, data access erro exits "+po.toString());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 用户查询售后单
     *
     * @author 潘登 24320182203249
     * @param userId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @param type
     * @param state
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> searchAfterSaleByUser(Long userId,
                                                                  LocalDateTime beginTime,
                                                                  LocalDateTime endTime,
                                                                  Integer page,
                                                                  Integer pageSize,
                                                                  Byte type,
                                                                  Byte state){
        PageHelper.startPage(page, pageSize);
        AfterSaleServicePoExample example=new AfterSaleServicePoExample();
        AfterSaleServicePoExample.Criteria criteria =example.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        criteria.andBeDeletedEqualTo((byte)0);
        if(beginTime!=null)criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        if(endTime!=null)criteria.andGmtCreateLessThanOrEqualTo(endTime);
        if(type!=null)criteria.andTypeEqualTo(type);
        if(state!=null)criteria.andStateEqualTo(state);

        List<AfterSaleServicePo> afterSaleServicePos=null;
        try{
            afterSaleServicePos=afterSaleServicePoMapper.selectByExample(example);
        }catch (DataAccessException e){
            logger.error("DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e){
            logger.error("Unknow Exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("其他错误 ：%s", e.getMessage()));
        }
        List<VoObject> ret=new ArrayList<>(afterSaleServicePos.size());
        for(AfterSaleServicePo po:afterSaleServicePos){
            ret.add(new AfterSale(po));
        }
        PageInfo<VoObject> afterSalePage=PageInfo.of(ret);
        return new ReturnObject<>(afterSalePage);
    }


    public ReturnObject<PageInfo<VoObject>> searchAfterSaleByAdmin(Long shopId,
                                                                  LocalDateTime beginTime,
                                                                  LocalDateTime endTime,
                                                                  Integer page,
                                                                  Integer pageSize,
                                                                  Byte type,
                                                                  Byte state){
        PageHelper.startPage(page, pageSize);
        AfterSaleServicePoExample example=new AfterSaleServicePoExample();
        AfterSaleServicePoExample.Criteria criteria =example.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        criteria.andBeDeletedEqualTo((byte)0);
        if(beginTime!=null)criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        if(endTime!=null)criteria.andGmtCreateLessThanOrEqualTo(endTime);
        if(type!=null)criteria.andTypeEqualTo(type);
        if(state!=null)criteria.andStateEqualTo(state);

        List<AfterSaleServicePo> afterSaleServicePos=null;
        try{
            afterSaleServicePos=afterSaleServicePoMapper.selectByExample(example);
        }catch (DataAccessException e){
            logger.error("DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e){
            logger.error("Unknow Exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("其他错误 ：%s", e.getMessage()));
        }
        List<VoObject> ret=new ArrayList<>(afterSaleServicePos.size());
        for(AfterSaleServicePo po:afterSaleServicePos){
            ret.add(new AfterSale(po));
        }
        PageInfo<VoObject> afterSalePage=PageInfo.of(ret);
        return new ReturnObject<>(afterSalePage);
    }

    /**
     * ***********************************
     * 买家根据售后单id查找售后记录,通用接口
     *************************************
     *
     * @author 潘登 24320182203249
     * @param id
     * @return
     */
    public ReturnObject<AfterSaleServicePo> selectByPrimaryKey(Long id){
        AfterSaleServicePo po;
        try{
            po=afterSaleServicePoMapper.selectByPrimaryKey(id);
        }catch (DataAccessException e){
            logger.error("DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "售后单查询失败");
        }
        catch (Exception e){
            logger.error("Unknow Exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "售后单查询失败");
        }
        if(po==null)
            return null;
        else
            return new ReturnObject<>(po);
    }

    /**
     * 用户查询对应主键的售后
     *
     * @author 潘登 24320182203249
     *
     * @param id
     * @param userId
     * @return
     */
    public ReturnObject<VoObject> selectByPrimaryKeyByUser(Long id,Long userId){
        ReturnObject<AfterSaleServicePo> ret=selectByPrimaryKey(id);
        if(ret==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        AfterSaleServicePo po=ret.getData();
        if(po==null)
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "售后单查询失败");
        if(po.getBeDeleted().intValue()!=0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        if(!po.getCustomerId().equals(userId))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());


        AfterSale retAfterSale=new AfterSale(po, 1);
        setBoSkuIdAndSkuName(retAfterSale);
        return new ReturnObject<>(retAfterSale);
    }

    /**
     * **************************
     * 更新售后单，通用接口
     * **************************
     *
     * @author 潘登
     * @param po Po对象
     * @return
     */
    public ReturnObject<Object> update(AfterSaleServicePo po){
        try{
            po.setGmtModified(LocalDateTime.now());
            int ret=afterSaleServicePoMapper.updateByPrimaryKeySelective(po);
            //if(ret==0)
                //return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }catch (DataAccessException e){
            logger.error("DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e){
            logger.error("Unknow Exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("其他错误 ：%s", e.getMessage()));
        }
        return new ReturnObject<>();
    }

    /**
     * 买家修改售后单信息
     * 允许状态：0,1
     *
     * @author 潘登
     *
     *
     * @param afterSale
     * @param userId
     * @return
     */
    public ReturnObject<Object> updateAfterSaleByUser(AfterSale afterSale,Long userId){
        AfterSaleServicePo po=afterSaleServicePoMapper.selectByPrimaryKey(afterSale.getId());

        if(po==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //检查状态
        if(po.getState().intValue()!= AfterSaleResponseCode.WAITING_CHECK.getCode()&&
        po.getState().intValue()!=AfterSaleResponseCode.WAITING_CUSTOMER_SEND.getCode())
            return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW,ResponseCode.AFTERSALE_STATENOTALLOW.getMessage());
        //检查是否被删除
        if(po.getBeDeleted()!=(byte)0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        //检查售后是否属于用户
        if(!userId.equals(po.getCustomerId()))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());


        /*po.setQuantity(afterSale.getQuantity());
        po.setReason(afterSale.getReason());
        po.setRegionId(afterSale.getRegionId());
        po.setDetail(afterSale.getDetail());
        po.setConsignee(afterSale.getConsignee());
        po.setMobile(afterSale.getMobile());*/
        po=afterSale.gotAfterSaleServicePo();
        po.setState((byte) AfterSaleResponseCode.WAITING_CHECK.getCode());
        return update(po);
    }

    /**
     * 取消或者逻辑删除售后单
     *
     * @author 潘登 24320182203249
     * @param id
     * @param userId
     * @return
     */
    public ReturnObject<Object> cancelOrLogicDeleteAfterSale(Long id,Long userId){
        AfterSaleServicePo po= afterSaleServicePoMapper.selectByPrimaryKey(id);

        if(po==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //资源已经被删除，不能重复删除
        if(po.getBeDeleted()!=0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        //检查售后是否属于用户
        if(!userId.equals(po.getCustomerId()))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());
        //不同状态设置不同值状态
        if(po.getState().intValue()== AfterSaleResponseCode.CANCELED.getCode()||
                po.getState().intValue()==AfterSaleResponseCode.ENDED.getCode()||
                po.getState().intValue()==AfterSaleResponseCode.REQUEST_DENY.getCode())
            po.setBeDeleted((byte) 1);
        else
            if(po.getState().intValue()!=AfterSaleResponseCode.WAITING_CHECK.getCode()&&
                po.getState().intValue()!=AfterSaleResponseCode.WAITING_CUSTOMER_SEND.getCode())
                return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW,ResponseCode.AFTERSALE_STATENOTALLOW.getMessage());
            else
                po.setState((byte)AfterSaleResponseCode.CANCELED.getCode());
        return update(po);
    }

    /**
     * 买家寄回货物填写单号
     *
     * @author 潘登 24320182203249
     *
     * @param id
     * @param userId
     * @param logSn
     * @return
     */
    public ReturnObject<Object> sendBackByUser(Long id,Long userId,String logSn){
        AfterSaleServicePo po= afterSaleServicePoMapper.selectByPrimaryKey(id);

        if(po==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //资源已经被删除，不能操作
        if(po.getBeDeleted()!=0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        if(!userId.equals(po.getCustomerId()))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());


        //限定状态
        if(po.getState().intValue()!=AfterSaleResponseCode.WAITING_CUSTOMER_SEND.getCode())
            return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW,ResponseCode.AFTERSALE_STATENOTALLOW.getMessage());

        //设定状态
        po.setState((byte) AfterSaleResponseCode.CUSTOMER_SENT.getCode());
        po.setCustomerLogSn(logSn);
        return update(po);
    }

    /**
     * 用户确认结束
     *
     * @author 潘登 24320182203249
     *
     * @param id
     * @param userId
     * @return
     */
    public ReturnObject<Object> userConfirm(Long id,Long userId){
        AfterSaleServicePo po= afterSaleServicePoMapper.selectByPrimaryKey(id);

        if(po==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //资源已经被删除，不能操作
        if(po.getBeDeleted()!=0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //限定状态
        if(po.getState().intValue()!=AfterSaleResponseCode.WAITING_SHOP_REFUND.getCode()&&
            po.getState().intValue()!=AfterSaleResponseCode.SHOP_SENT.getCode())
            return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW,ResponseCode.AFTERSALE_STATENOTALLOW.getMessage());

        if(!userId.equals(po.getCustomerId()))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());

        //TODO: 退货时此处退款
//        paymentService.getAdminHandleRefund(po.getCustomerId(),po.getShopId(),po.getOrderItemId(),-1*po.getRefund(),po.getId());

        //设定状态
        po.setState((byte) AfterSaleResponseCode.ENDED.getCode());
        return update(po);
    }

    /**
     * 管理员根据对应主键查询分享活动
     *
     * @author 潘登 24320182203249
     * @param id
     * @param shopId
     * @return
     */
    public ReturnObject<VoObject> selectByPrimaryKeyByAdmin(Long id,Long shopId){
        ReturnObject<AfterSaleServicePo> ret=selectByPrimaryKey(id);
        if(ret==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        AfterSaleServicePo po=ret.getData();
        if(po==null)
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "售后单查询失败");
        if(!po.getShopId().equals(shopId))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());

        AfterSale retAfterSale=new AfterSale(po, 1);
        setBoSkuIdAndSkuName(retAfterSale);
        return new ReturnObject<>(retAfterSale);
    }

    /**
     * 管理员接收或拒绝售后请求
     *
     * @author 潘登 24320182203249
     * @param id
     * @param shopId
     * @param confirm
     * @param price
     * @param conclusion
     * @return
     */
    public ReturnObject<Object> adminConfirm(Long id,
                                             Long shopId,
                                             Boolean confirm,
                                             Long price,
                                             String conclusion,
                                             Byte type){
        AfterSaleServicePo po= afterSaleServicePoMapper.selectByPrimaryKey(id);
        //logger.debug(po.getState().toString()+"******\n");

        if(po==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //资源已经被删除，不能操作
        if(po.getBeDeleted().intValue()!=0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //判断资源是否属于店铺
        if(!shopId.equals(po.getShopId()))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());

        //限定状态
        if(po.getState().intValue()!=AfterSaleResponseCode.WAITING_CHECK.getCode())
            return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW,ResponseCode.AFTERSALE_STATENOTALLOW.getMessage());

        if(price!=null)po.setRefund(price);
        if(conclusion!=null)po.setConclusion(conclusion);
        if(type!=null)po.setType(type);
        if(confirm)
            po.setState((byte) AfterSaleResponseCode.WAITING_CUSTOMER_SEND.getCode());
        else
            po.setState((byte) AfterSaleResponseCode.REQUEST_DENY.getCode());
        return update(po);
    }

    /**
     * 店家收货，同时退款，推返点，重要难点
     *
     * @param id
     * @param shopId
     * @param confirm
     * @param conclusion
     * @return
     */
    public ReturnObject<Object> shopReceive(Long id,
                                            Long shopId,
                                            Boolean confirm,
                                            String conclusion){
        AfterSaleServicePo po= afterSaleServicePoMapper.selectByPrimaryKey(id);

        if(po==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //资源已经被删除，不能操作
        if(po.getBeDeleted()!=0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //判断资源是否属于店铺
        if(!shopId.equals(po.getShopId()))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());

        //限定状态
        if(po.getState().intValue()!=AfterSaleResponseCode.CUSTOMER_SENT.getCode())
            return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW,ResponseCode.AFTERSALE_STATENOTALLOW.getMessage());


        if(conclusion!=null)po.setConclusion(conclusion);
        if(confirm)
            if(po.getType().intValue()==1)
                po.setState((byte) AfterSaleResponseCode.WAITING_SHOP_REFUND.getCode());
            else
                po.setState((byte) AfterSaleResponseCode.WAITING_SHOP_SENT.getCode());
        else
            po.setState((byte) AfterSaleResponseCode.WAITING_CUSTOMER_SEND.getCode());

        /**
         * 退款要退款退返点
         * 换货产生新订单
         * 维修直接下一阶段
         */

        return update(po);
    }

    public ReturnObject<Object> shopSend(Long id,Long shopId,String logSn){
        AfterSaleServicePo po= afterSaleServicePoMapper.selectByPrimaryKey(id);

        if(po==null)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //资源已经被删除，不能操作
        if(po.getBeDeleted()!=0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());

        //判断资源是否属于店铺
        if(!po.getShopId().equals(shopId))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage());

        //限定状态
        if(po.getState().intValue()!=AfterSaleResponseCode.WAITING_SHOP_SENT.getCode())
            return new ReturnObject<>(ResponseCode.AFTERSALE_STATENOTALLOW,ResponseCode.AFTERSALE_STATENOTALLOW.getMessage());


        //TODO: 维修时填写logSn，且如果为换货需要产生新的订单
        //换货
        /*if(po.getType()!=null&&po.getType().intValue()==0)
        {
            ReturnObject<Long> returnObject=orderService.getAdminHandleExchange(po.getCustomerId(),po.getShopId(),po.getOrderItemId(),po.getQuantity(),po.getId());
            //错误校验？
            if(returnObject.getCode().getCode()!=ResponseCode.OK.getCode())
            {
                logger.debug("生成换货单时出错");
            }
            po.setOrderId(returnObject.getData());
        }*/

        if(po.getType()==0)
            po.setOrderId(233L);

        //维修
        if(po.getType()==null||po.getType().intValue()==2)po.setShopLogSn(logSn);
        po.setState((byte) AfterSaleResponseCode.SHOP_SENT.getCode());
        return update(po);
    }

}
