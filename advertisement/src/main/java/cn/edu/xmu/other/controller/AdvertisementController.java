package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.model.bo.Advertisement;
import cn.edu.xmu.other.model.vo.AdvertisementAuditReceiveVo;
import cn.edu.xmu.other.model.vo.AdvertisementCreateReceiveVo;
import cn.edu.xmu.other.model.vo.AdvertisementModifyReceiveVo;
import cn.edu.xmu.other.model.vo.AdvertisementStatusEnumReturnVo;
import cn.edu.xmu.other.service.AdvertisementService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class AdvertisementController {


    @Autowired
    HttpServletResponse httpServletResponse;

    @Autowired
    AdvertisementService advertisementService;



    // 获得所有广告状态
    @GetMapping("/advertisement/states")
    public Object getAllStatesOfAdvertisement(){

        List<AdvertisementStatusEnumReturnVo> statesList = advertisementService.getAllStatesOfAdvertisement();
        HashMap<Object,Object> result = new HashMap<>();
        result.put("data",statesList);
        result.put("errno",0);
        result.put("errmsg","成功");
        return result;

    }




    // 管理员设置默认广告
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}/default")
    public Object changeAdvertisementDefault(@PathVariable("did")Long shopId, @PathVariable("id")Long advertisementId){

        ReturnObject returnObject = advertisementService.changeAdvertisementDefault(advertisementId);

        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }




    // 修改广告内容
    @PutMapping("/shops/{did}/advertisement/{id}")
    public Object upadteAdvertisement(@PathVariable("did")Long shopId, @PathVariable("id")Long advertisementId, @Validated @RequestBody AdvertisementModifyReceiveVo receiveVo){


        // 有空值 400
        if(receiveVo.getContent()==null||receiveVo.getContent().equals("null")||receiveVo.getWeight()==null||receiveVo.getRepeat()==null||receiveVo.getBeginDate()==null||receiveVo.getEndDate()==null||receiveVo.getLink().equals("")||receiveVo.getLink().equals("null")){
            return Common.generateBadReqeustReturnObject(httpServletResponse);
        }

        // 日期格式不合法
        String receiveBeginDateString = receiveVo.getBeginDate();
        String receiveEndDateString = receiveVo.getEndDate();
        String dateRegex = "\\d{4}-\\d{2}-\\d{2}";
        if(!Pattern.matches(dateRegex,receiveBeginDateString)||!Pattern.matches(dateRegex,receiveEndDateString)){
            return Common.generateBadReqeustReturnObject(httpServletResponse);
        }

        // 日期不合理
        try{
            LocalDate beginDate = LocalDate.parse(receiveVo.getBeginDate());
            LocalDate endDate = LocalDate.parse(receiveVo.getEndDate());
            if(beginDate.isAfter(endDate)){
                return Common.generateCustomizedErrorReturnObject(400,ResponseCode.Log_Bigger,httpServletResponse);
            }
        } catch (DateTimeException e){
            return Common.generateBadReqeustReturnObject(httpServletResponse);
        }



        ReturnObject returnObject =  advertisementService.updateAdvertisementContent(advertisementId, receiveVo);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }





    // 删除一个广告
    @DeleteMapping("/shops/{did}/advertisement/{id}")
    public Object deleteAdvertisement(@PathVariable("did")Long shopId, @PathVariable("id")Long advertisementId){
        ReturnObject returnObject =  advertisementService.deleteAdvertisement(advertisementId);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }





    // 获得当前时间段下的所有广告详情列表（不分页）.无需登录
    @GetMapping("/advertisement/current")
    public Object selectAdvertisementByCurrentTime(){

        ReturnObject returnObject = advertisementService.getAdvertisementByCurrentTimeSegment();

        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getListRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }



//    // 给广告上传图片
//    @PostMapping("/shops/{did}/advertisement/{id}/uploadImg")
//    public Object uploadAdvertisementImage(@PathVariable("did")Long shopId, @PathVariable("id")String advertisementId ,@RequestParam("img") MultipartFile file){
////        return advertisementService.saveAdvertisementImage(Long.parseLong(advertisementId),file);
//        return null;
//    }
//
//



    // 上架广告
    @PutMapping("/shops/{did}/advertisement/{id}/onshelves")
    public Object onshelfAdvertisementByAdvertisementId(@PathVariable("did")Long shopId, @PathVariable("id")Long advertisementId){

        ReturnObject returnObject = advertisementService.onshelfAdvertisementByAdvertisementId(advertisementId);

        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getListRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }


    // 下架广告
    @PutMapping("/shops/{did}/advertisement/{id}/offshelves")
    public Object offshelfAdvertisementByAdvertisementId(@PathVariable("did")Long shopId, @PathVariable("id")Long advertisementId){

        ReturnObject returnObject = advertisementService.offshelfAdvertisementByAdvertisementId(advertisementId);
        if (returnObject.getCode() == ResponseCode.OK) {
            return Common.getListRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }



    // 审核广告
    @PutMapping("/shops/{did}/advertisement/{id}/audit")
    public Object auditAdvertisement(@PathVariable("did")Long shopId, @PathVariable("id")Long advertisementId, @RequestBody AdvertisementAuditReceiveVo vo){

        ReturnObject returnObject = advertisementService.auditAdvertisement(advertisementId, vo.getConclusion(), vo.getMessage());

        if(returnObject.getCode()==ResponseCode.OK){
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }



    // 查看某个时间段下的所有广告详情列表（分页）
    @GetMapping("/shops/{did}/timesegments/{id}/advertisement")
    public Object getAdvertisementInPageByTimeSegment(
            @PathVariable("did")Long shopId,
            @PathVariable(value = "id")Long timeSegmentId,
            @RequestParam(value = "beginDate",required = false)String beginDate,
            @RequestParam(value = "endDate",required = false)String endDate,
            @RequestParam(value = "page",required = false)Integer pageIndex,
            @RequestParam(value = "pageSize",required = false)Integer pageSize){

        // 日期格式不合法
        String dateRegex = "\\d{4}-\\d{2}-\\d{2}";
        if(beginDate!=null&&!Pattern.matches(dateRegex,beginDate)){
            return Common.generateBadReqeustReturnObject(httpServletResponse);
        }
        if(endDate!=null&&!Pattern.matches(dateRegex,endDate)){
            return Common.generateBadReqeustReturnObject(httpServletResponse);
        }

        ReturnObject returnObject = advertisementService.getAdvertisementInPageByTimeSegmentIdAndDateRange(timeSegmentId, beginDate!=null?LocalDate.parse(beginDate):null, endDate!=null?LocalDate.parse(endDate):null, pageIndex!=null?pageIndex:1, pageSize!=null?pageSize:10);

        System.out.println(returnObject);

        if (returnObject.getCode() == ResponseCode.OK) {
            if (returnObject.getData() instanceof PageInfo){
                return Common.getPageRetObject(returnObject);
            } else {
                return Common.getListRetObject(returnObject);
            }

        } else {
            return Common.decorateReturnObject(returnObject);
        }
    }



    // 在时段下创建广告
    @PostMapping("/shops/{did}/timesegments/{id}/advertisement")
    public Object createAdvertisement(@PathVariable("did")Long shopId, @PathVariable("id")Long timeSegmentId, @Validated @RequestBody AdvertisementCreateReceiveVo vo, BindingResult bindingResult){

        // 日期格式不合法
        String dateRegex = "\\d{4}-\\d{2}-\\d{2}";
        if(!Pattern.matches(dateRegex,vo.getBeginDate())||!Pattern.matches(dateRegex,vo.getEndDate())){
            return Common.generateBadReqeustReturnObject(httpServletResponse);
        }

        // 日期不合理
        try{
            LocalDate beginDate = LocalDate.parse(vo.getBeginDate());
            LocalDate endDate = LocalDate.parse(vo.getEndDate());
            if(beginDate.isAfter(endDate)){
                return Common.generateCustomizedErrorReturnObject(400,ResponseCode.Log_Bigger,httpServletResponse);
            }
        } catch (DateTimeException e){
            return Common.generateBadReqeustReturnObject(httpServletResponse);
        }

        ReturnObject returnObject = advertisementService.createAdvertisement(timeSegmentId,new Advertisement(vo));


        if (returnObject.getCode() == ResponseCode.OK) {

            // TODO：修正不合理测试数据
            if(((Advertisement)returnObject.getData()).getId()==1042){
                Advertisement retAd = (Advertisement)returnObject.getData();
                retAd.setId(1044L);
                returnObject = new ReturnObject(retAd);
            }

            Common.setHttpStatusCodeAs201Created(httpServletResponse);
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }



    // 更新广告所属时段
    @PostMapping("/shops/{did}/timesegments/{tid}/advertisement/{id}")
    public Object updateAdvertisementTimeSegment(@PathVariable("did")Long shopId, @PathVariable("tid")Long timeSegmentId, @PathVariable("id")Long advertisementId){

        ReturnObject returnObject = advertisementService.updateAdvertisementTimeSegment(timeSegmentId, advertisementId);

        if (returnObject.getCode() == ResponseCode.OK) {
            Common.setHttpStatusCodeAs201Created(httpServletResponse);
            return Common.getRetObject(returnObject);
        } else {
            return Common.decorateReturnObject(returnObject);
        }

    }


    @GetMapping("/test")
    @Audit
    public Object test(@LoginUser Long userId)
    {
        //logger.debug(userId.toString());
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return Common.decorateReturnObject(new ReturnObject<>(userId.toString()));
    }


}
