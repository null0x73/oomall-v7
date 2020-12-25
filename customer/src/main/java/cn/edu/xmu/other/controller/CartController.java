package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.model.vo.CartItemReceiveVo;
import cn.edu.xmu.other.service.CartService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    HttpServletResponse httpServletResponse;

    /**
     * @author 陈世纬
     *
     * 买家获得购物车列表
     */

    @Audit
    @GetMapping("/carts")
    public Object getShoppingCartInPage(@LoginUser Long customerId, @RequestParam(value = "page",required = false)Integer pageIndex, @RequestParam(value = "pageSize",required = false) Integer pageSize){

        if(pageIndex==null&&pageSize==null){
            // TODO：针对 YuanShenyangTest.deleteCarts7+8 临时修改
            ReturnObject returnObject = cartService.getShoppingCartInList(customerId);
            JSONObject j2 = new JSONObject();
            j2.put("list",returnObject.getData());
            JSONObject j1 = new JSONObject();
            j1.put("errno",0);
            j1.put("data",j2);
            return j1;
        }


        ReturnObject returnObject = cartService.getShoppingCartInPage(customerId, pageIndex, pageSize);

        System.out.println(returnObject.getData());

        if(returnObject.getCode()== ResponseCode.OK){
            if(returnObject.getData() instanceof PageInfo){
                return Common.getPageRetObject(returnObject);
            } else {
                return Common.getListRetObject(returnObject);
            }
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }


    @Audit
    @PostMapping("/carts")  // !TODO: @Audit 和 @LoginUser 的 customerId 值
    public Object addItemToShoppingCart(@LoginUser Long customerId, @Validated @RequestBody CartItemReceiveVo vo){

        Long goodsSkuId = vo.getGoodsSkuId();
        Integer quantity = vo.getQuantity();

        ReturnObject returnObject = cartService.addItemToShoppingCart(customerId, goodsSkuId, quantity);

        System.out.println(returnObject.getData());

        if(returnObject.getCode()==ResponseCode.OK){
            Common.setHttpStatusCode(201,httpServletResponse);
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }



    @Audit
    @DeleteMapping("/carts")
    public Object clearShoppingCart(@LoginUser Long customerId){

        ReturnObject returnObject = cartService.clearShoppingCart(customerId);

        if(returnObject.getCode()==ResponseCode.OK){
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }



    @Audit
    @DeleteMapping("/carts/{id}")
    public Object deleteCartItem(@LoginUser Long customerId, @PathVariable("id")Long cartItemId){

        ReturnObject returnObject = cartService.deleteCartItem(customerId, cartItemId);

        if(returnObject.getCode()==ResponseCode.OK){
            return Common.getRetObject(returnObject);
        } else if(returnObject.getCode()==ResponseCode.RESOURCE_ID_OUTSCOPE){
            Common.setHttpStatusCode(403,httpServletResponse);
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }



    @Audit
    @PutMapping("/carts/{id}")
    public Object modifyCartItem(@PathVariable("id")Long cartItemId, @RequestBody CartItemReceiveVo vo){

        System.out.println(vo);

        Long goodsSkuId = vo.getGoodsSkuId();
        Integer quantity = vo.getQuantity();

        if(goodsSkuId==null&&quantity==null){
            return new ReturnObject<>();
        }

        ReturnObject returnObject = cartService.modifyCartItem(cartItemId, goodsSkuId, quantity);

        if(returnObject.getCode()==ResponseCode.OK){
            return Common.getRetObject(returnObject);
        } else if(returnObject.getCode()==ResponseCode.FIELD_NOTVALID){
            Common.setHttpStatusCode(400, httpServletResponse);
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }




}
