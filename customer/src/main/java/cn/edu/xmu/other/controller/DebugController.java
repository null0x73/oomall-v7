package cn.edu.xmu.other.controller;

import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class DebugController {

    @DubboReference(version = "0.0.1-SNAPSHOT")
    IGoodsService goodsService;

    @Audit
    @GetMapping("/debug")
    public Object debug(@LoginUser Long userId, @Depart Long departId){
        HashMap<String,Object> response = new HashMap<>();
        response.put("userId",userId);
        response.put("departId",departId);
        response.put("data",goodsService.getShopIdBySKUId(123L));
        return response;
    }

    @Audit
    @GetMapping("/shops/1/orders/233")
    public Object debug2(){
        JSONObject j0 = new JSONObject();
        j0.put("INFO","DEBUG");
        return j0;
    }



}
