package cn.edu.xmu.other.service;

import cn.edu.xmu.goods.client.IActivityService;
import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.dubbo.CouponActivityDTO;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.goods.client.dubbo.SpuDTO;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.CartItemDao;
import cn.edu.xmu.other.model.bo.CartItem;
import cn.edu.xmu.other.model.po.CartItemPo;
import cn.edu.xmu.other.model.vo.CartItemReturnVo;
import cn.edu.xmu.other.model.vo.CouponActivityReturnVo;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    CartItemDao cartItemDao;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    IGoodsService goodsService;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    IActivityService activityService;




    public ReturnObject getShoppingCartInList(Long customerId){

        List<CartItemPo> poList = cartItemDao.getShoppingCartInListByCustomerId(customerId);
        List<CartItem> boList = new ArrayList<>(poList.size());
        for(CartItemPo po:poList){
            CartItem bo = new CartItem();           // TODO: Check RPC here
            Long skuId = po.getGoodsSkuId();
            SkuDTO skuDto = goodsService.getSku(skuId);
            Long spuId = skuDto.getGoodsSpuId();
            SpuDTO spuDTO = goodsService.getSimpleSpuById(spuId);
            String skuName = skuDto.getName();
            bo.setId(po.getId());
            bo.setGoodsSkuId(po.getGoodsSkuId());
            bo.setPrice(po.getPrice());
            bo.setQuantity(po.getQuantity());
            bo.setSkuName(skuName);
            bo.setGmtCreate(po.getGmtCreate());
            bo.setGmtModified(po.getGmtModified());
            bo.setCouponActivity(activityService.getSkuCouponActivity(skuId));    // TODO: RPC check
            boList.add(bo);
        }

        System.out.println(boList);

        return new ReturnObject(boList);

    }





    public ReturnObject getShoppingCartInPage(Long customerId, Integer pageIndex, Integer pageSize) {

        PageInfo<CartItemPo> poPage = cartItemDao.getShoppingCartInPageByCustomerId(customerId, pageIndex, pageSize);

        List<CartItemPo> poList = poPage.getList();
        List<CartItem> boList = new ArrayList<>(poList.size());
        for(CartItemPo po:poList){
            CartItem bo = new CartItem();           // TODO: Check RPC here
            Long skuId = po.getGoodsSkuId();
            SkuDTO skuDto = goodsService.getSku(skuId);
            Long spuId = skuDto.getGoodsSpuId();
            SpuDTO spuDTO = goodsService.getSimpleSpuById(spuId);
            String skuName = skuDto.getName();
//            String skuName = "SKUNAME";
            bo.setId(po.getId());
            bo.setGoodsSkuId(po.getGoodsSkuId());
            bo.setPrice(po.getPrice());
            bo.setQuantity(po.getQuantity());
            bo.setSkuName(skuName);
            bo.setGmtCreate(po.getGmtCreate());
            bo.setGmtModified(po.getGmtModified());
            bo.setCouponActivity(activityService.getSkuCouponActivity(skuId));    // TODO: RPC check
            boList.add(bo);
        }

        PageInfo<CartItem> boPage = new PageInfo<>();
        boPage.setPageNum(poPage.getPageNum());
        boPage.setPageSize(poPage.getPageSize());
        boPage.setTotal(poPage.getTotal());
        boPage.setPages(poPage.getPages());
        boPage.setList(boList);

        return new ReturnObject(boPage);

    }



    public ReturnObject<CartItemReturnVo> addItemToShoppingCart(Long customerId, Long goodsSkuId, Integer quantity) {

        // 向商品模块查询单价
        Long singlePrice = goodsService.getSku(goodsSkuId).getPrice();      // TODO: Check RPC here
//        Long singlePrice = 100L;

        // 创建 PO 并向 DAO 查询
        CartItemPo createPo = cartItemDao.addItemToShoppingCart(customerId, goodsSkuId, quantity, singlePrice);

        // 向商品模块查询 skuName
        String skuName = goodsService.getSku(goodsSkuId).getName();       // TODO: Check RPC here

        // 向商品模块查询优惠卷列表
        List<CouponActivityDTO> skuCouponActivityList = activityService.getSkuCouponActivity(goodsSkuId);   // !TODO: Check RPC here

        // 创建 BO
        CartItemReturnVo returnVo = new CartItemReturnVo();
        returnVo.setId(createPo.getId());
        returnVo.setGoodsSkuId(createPo.getGoodsSkuId());
        returnVo.setQuantity(createPo.getQuantity());
        returnVo.setPrice(createPo.getPrice());
        returnVo.setGmtCreate(createPo.getGmtCreate());
        returnVo.setGmtModified(createPo.getGmtModified());
        returnVo.setSkuName(skuName);
        returnVo.setCouponActivity(skuCouponActivityList);

        return new ReturnObject(returnVo);

    }




    public ReturnObject clearShoppingCart(Long customerId) {
        cartItemDao.clearShoppingCart(customerId);
        return new ReturnObject();
    }




    public ReturnObject deleteCartItem(Long customerId, Long cartItemId) {

        if(!cartItemDao.isCartItemExistByCartItemId(cartItemId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        if(!cartItemDao.isCartItemBelongToCustomer(customerId, cartItemId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }

        cartItemDao.deleteCartItem(customerId, cartItemId);
        return new ReturnObject();

    }



    public ReturnObject modifyCartItem(Long cartItemId, Long modifySkuId, Integer quantity) {


        // 检查新 sku 是否存在       // TODO: Chech RPC here
        SkuDTO modifySkuDTO = goodsService.getSku(modifySkuId);
        if(modifySkuDTO.getId()==null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);     // 新的 skuid 不存在
        }

        // 检查此 sku 是否与原 sku 属于同一个 spu
        Long originalSkuId = cartItemDao.getCartItemByCartItemId(cartItemId).getGoodsSkuId();
        Long originalSpuId = goodsService.getSku(originalSkuId).getGoodsSpuId();
        Long modifySpuId = modifySkuDTO.getGoodsSpuId();

        if(!modifySpuId.equals(originalSpuId)){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }


        // 查询新价格
        Long newPrice = goodsService.getSku(modifySkuId).getPrice();

        // 执行更新
        Integer result = cartItemDao.modifyCartItem(cartItemId, modifySkuId, quantity, newPrice);

        // 判断结果并返回
        if(result==1){  // 成功更新1条
            return new ReturnObject();
        } else {        // 不存在该购物车项
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

    }

}
