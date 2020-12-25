package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.other.model.bo.TimeSegment;
import cn.edu.xmu.other.model.vo.TimeSegmentReceiveVo;
import cn.edu.xmu.other.service.TimeSegmentService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 陈世纬
 */

@RestController
public class TimeSegmentController {

    @Autowired
    TimeSegmentService timeSegmentService;

    @Autowired
    HttpServletResponse httpServletResponse;



    /**
     * 平台管理员  新增广告时段
     * @param receiveVo
     * @return
     */
    @PostMapping("/shops/{did}/advertisement/timesegments")
    public Object createAdvertisementTimeSegment(@PathVariable("did")Long shopId, @RequestBody TimeSegmentReceiveVo receiveVo){

        // 开始时间为空
        if(receiveVo.getBeginTime()==null){
            Common.setHttpStatusCode(400,httpServletResponse);
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.Log_BEGIN_NULL));
        }

        // 结束时间为空
        if(receiveVo.getEndTime()==null){
            Common.setHttpStatusCode(400,httpServletResponse);
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.Log_END_NULL));
        }

        // 开始时间大于结束时间
        if(receiveVo.getBeginTime().isAfter(receiveVo.getEndTime())){
            Common.setHttpStatusCode(400,httpServletResponse);
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.Log_Bigger));
        }

        // 正常查询
        ReturnObject returnObject = timeSegmentService.createAdvertisementTimeSegment(receiveVo);


        // 返回处理
        if(returnObject.getCode().equals(ResponseCode.OK)){
            Common.setHttpStatusCode(201,httpServletResponse);
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }




    /**
     * 管理员  获取广告时段（列表）
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/shops/{did}/advertisement/timesegments")
    public Object getAdvertisementTimeSegmentInPage(@PathVariable("did")Long shopId, @RequestParam(value = "page",required = false)Integer pageIndex, @RequestParam(value = "pageSize",required = false)Integer pageSize){// 获取封装了分页信息的结果集

        if(pageIndex==null&&pageSize==null){
            JSONObject j2 = new JSONObject();
            JSONObject j1 = new JSONObject();
            j2.put("list",timeSegmentService.getAdvertisementTimeSegmentInListByAllTime().getData());
            j1.put("errno",0);
            j1.put("data",j2);
            return j1;
        }

        ReturnObject returnObject = timeSegmentService.getAdvertisementTimeSegmentInPage(pageIndex!=null?pageIndex:1,pageSize);

        if(returnObject.getCode().equals(ResponseCode.OK)){
            return Common.getPageRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }




    /**
     * 平台管理员  新增秒杀时段
     * @return
     */
    @PostMapping("/shops/{did}/flashsale/timesegments")
    public Object createFlashSaleTimeSegment(@PathVariable("did")Long shopId, @Validated @RequestBody TimeSegmentReceiveVo receiveVo){


        // 开始时间为空
        if(receiveVo.getBeginTime()==null){
            Common.setHttpStatusCode(400,httpServletResponse);
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.Log_BEGIN_NULL));
        }

        // 结束时间为空
        if(receiveVo.getEndTime()==null){
            Common.setHttpStatusCode(400,httpServletResponse);
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.Log_END_NULL));
        }

        // 开始时间大于结束时间
        if(receiveVo.getBeginTime().isAfter(receiveVo.getEndTime())){
            Common.setHttpStatusCode(400,httpServletResponse);
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.Log_Bigger));
        }

        ReturnObject returnObject = timeSegmentService.createFlashSaleTimeSegment(receiveVo);

        if(returnObject.getCode().equals(ResponseCode.OK)){
            Common.setHttpStatusCode(201,httpServletResponse);
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }




    /**
     * 管理员  获取秒杀时段（列表）
     * @return
     */
    @GetMapping("/shops/{did}/flashsale/timesegments")
    public Object getFlashSaleTimeSegmentInPage(@PathVariable("did")Long shopId, @RequestParam(value = "page",required = false)Integer pageIndex, @RequestParam(value = "pageSize",required = false)Integer pageSize){

        if(pageIndex==null&&pageSize==null){
            JSONObject j2 = new JSONObject();
            JSONObject j1 = new JSONObject();
            j2.put("list",timeSegmentService.getFlashSaleTimeSegmentInList());
            j1.put("errno",0);
            j1.put("data",j2);
            return j1;
        }

        ReturnObject returnObject = timeSegmentService.getFlashSaleTimeSegmentInPage(pageIndex,pageSize);

        if(returnObject.getCode().equals(ResponseCode.OK)){
            return Common.getPageRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }


    /**
     * 平台管理员  删除广告时间段
     * !TODO:删除某个时段后，该时段下相应的秒杀和广告的时段 id 置为 0，即成为无时段的秒杀和广告
     */
    @DeleteMapping("/shops/{did}/advertisement/timesegments/{id}")
    public Object deleteAdvertisementTimeSegment(@PathVariable("did")Long shopId, @PathVariable("id")Long timeSegmentId){

        ReturnObject returnObject = timeSegmentService.deleteAdvertisementTimeSegment(timeSegmentId);

        if(returnObject.getCode().equals(ResponseCode.OK)){
            return Common.getPageRetObject(returnObject);
        } else if(returnObject.getCode().equals(ResponseCode.RESOURCE_ID_NOTEXIST)){
            Common.setHttpStatusCode(404,httpServletResponse);
            return Common.getRetObject(returnObject);
        } else if(returnObject.getCode().equals(ResponseCode.RESOURCE_ID_OUTSCOPE)){
            Common.setHttpStatusCode(403,httpServletResponse);
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }


    /**
     * 平台管理员  删除秒杀时间段
     * !TODO:删除某个时段后，该时段下相应的秒杀和广告的时段 id 置为 0，即成为无时段的秒杀和广告
     */
    @DeleteMapping("/shops/{did}/flashsale/timesegments/{id}")
    public Object deleteFlashSaleTimeSegment(@PathVariable("did")Long shopId, @PathVariable("id")Long timeSegmentID) {

        ReturnObject returnObject = timeSegmentService.deleteFlashSaleTimeSegment(timeSegmentID);

        if(returnObject.getCode().equals(ResponseCode.OK)){
            return Common.getRetObject(returnObject);
        } else if(returnObject.getCode().equals(ResponseCode.RESOURCE_ID_OUTSCOPE)){
            Common.setHttpStatusCode(403,httpServletResponse);
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }

}
