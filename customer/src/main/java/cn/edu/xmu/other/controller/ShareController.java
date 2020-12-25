package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.model.bo.ShareActivity;
import cn.edu.xmu.other.model.bo.ShareRule;
import cn.edu.xmu.other.model.vo.ShareActivityVo;
import cn.edu.xmu.other.model.vo.ShareRetVo;
import cn.edu.xmu.other.model.vo.ShareRuleVo;
import cn.edu.xmu.other.service.BeShareService;
import cn.edu.xmu.other.service.ShareActivityService;
import cn.edu.xmu.other.service.ShareService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Api(value = "分享服务", tags = "share")
@RestController  /*Restful的Controller对象*/
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class ShareController {

    private  static  final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private ShareActivityService shareActivityService;

    @Autowired
    private ShareService shareService;

    @Autowired
    private BeShareService beShareService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    void setResponse(int state,HttpServletResponse httpServletResponse){
        if(state==ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        if(state==ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        if(state==ResponseCode.FIELD_NOTVALID.getCode())
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        //TODO: 待完善的设置状态,第一组早早鸟过了以后才知道
        if(state==ResponseCode.SHAREACT_CONFLICT.getCode())
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        if(state==ResponseCode.Log_Bigger.getCode())
            httpServletResponse.setStatus(HttpStatus.OK.value());
        if(state==ResponseCode.Log_BEGIN_NULL.getCode())
            httpServletResponse.setStatus(HttpStatus.OK.value());
        if(state==ResponseCode.Log_END_NULL.getCode())
            httpServletResponse.setStatus(HttpStatus.OK.value());
    }

    /**
     * 新建分享活动分
     *
     * @author 潘登 24320182203249
     * @param shopId
     * @param skuId
     * @param shareActivityVo
     * @return
     */
    @ApiOperation(value="新建分享活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="skuId", value="商品skuId", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="shareActivityVo", value="分享活动输入Vo", required = true, dataType = "ShareActivityVo", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PostMapping("/shops/{shopId}/skus/{skuId}/shareactivities")
    @Audit
    public Object createShareActivity(@PathVariable Long shopId,
                                      @PathVariable Long skuId,
                                      @Validated  @RequestBody ShareActivityVo shareActivityVo,
                                      BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }

        //TODO: 检查是否符合规则？？
        /*if(shareActivityVo.getBeginTime()==null)
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.Log_BEGIN_NULL, "前端传来的时间格式不对"));
        if(shareActivityVo.getEndTime()==null)
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.Log_END_NULL, "前端传来的时间格式不对"));*/

        //检查规则
        try {
            ShareRuleVo ruleVo = JSON.parseObject(shareActivityVo.getStrategy(),ShareRuleVo.class);
            for(ShareRule shareRule:ruleVo.getRule()){
                if(shareRule.getNum()==null||shareRule.getRate()==null)
                {
                    httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                    return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, ResponseCode.FIELD_NOTVALID.getMessage()));
                }
            }
        }catch (Exception e){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, ResponseCode.FIELD_NOTVALID.getMessage()));
        }

        //检查时间
        LocalDateTime beginTime=null;
        LocalDateTime endTime=null;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            beginTime=LocalDateTime.parse(shareActivityVo.getBeginTime(),formatter);
            endTime=LocalDateTime.parse(shareActivityVo.getEndTime(),formatter);
        }catch(Exception e){
            logger.error("DateTime format error " + e.getMessage());
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("前端传来的时间格式不对:%s", e.getMessage())));
        }
        if(beginTime.isAfter(endTime)) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "开始时间大于结束时间"));
        }

        ShareActivity shareActivity=new ShareActivity();
        shareActivity.setShopId(shopId);
        shareActivity.setGoodsSkuId(skuId);
        shareActivity.setBeginTime(beginTime);
        shareActivity.setEndTime(endTime);
        shareActivity.setStrategy(shareActivityVo.getStrategy());
        shareActivity.setState((byte) 0);
        ReturnObject<VoObject> ret=shareActivityService.createShareActivity(shareActivity);

        setResponse(ret.getCode().getCode(),httpServletResponse);
        if(ret.getCode().getCode()==0)
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return Common.getRetObject(ret);
    }

    /**
     * 查询分享活动
     *
     * @author 潘登
     * @param shopId 商店id
     * @param skuId skuId
     * @param page 页号
     * @param pageSize 页大小
     * @return
     */
    @ApiOperation(value="查询分享活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value="商店id", required = false, dataType="Long", paramType="query"),
            @ApiImplicitParam(name="skuId", value="商品skuId", required = false, dataType="Long", paramType="query"),
            @ApiImplicitParam(name="page", value="页号", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="pageSize", value="页大小", required = false, dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @GetMapping("/shareactivities")
    @Audit
    public Object searchShareActivities(@RequestParam(required = false) Long shopId,
                                        @RequestParam(required = false) Long skuId,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer pageSize){
        page = (page==null) ? 1:page;
        pageSize = (pageSize==null) ? 10:pageSize;
        System.out.println("****"+page);
        ReturnObject<PageInfo<VoObject>> ret=shareActivityService.selectShareActivities(shopId,skuId,page,pageSize);

        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.getPageRetObject(ret);
    }

    /**
     * 修改分享信息
     *
     * @author 潘登 24320182203249
     * @param shopId 商店id
     * @param id 分享id
     * @param shareActivityVo 修改的数据
     * @return
     */
    @ApiOperation(value="更改分享活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="id", value="分享id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="shareActivityVo", value="分享活动输入Vo", required = true, dataType = "ShareActivityVo", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PutMapping("/shops/{shopId}/shareactivities/{id}")
    @Audit
    //TODO: 设置异常情况返回码
    public Object updateShareActivities(@PathVariable Long shopId,
                                        @PathVariable Long id,
                                        @Validated @RequestBody ShareActivityVo shareActivityVo,
                                        BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }

        //检查规则
        try {
            ShareRuleVo ruleVo = JSON.parseObject(shareActivityVo.getStrategy(),ShareRuleVo.class);
            for(ShareRule shareRule:ruleVo.getRule()){
                if(shareRule.getNum()==null||shareRule.getRate()==null)
                {
                    httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                    return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, ResponseCode.FIELD_NOTVALID.getMessage()));
                }
            }
        }catch (Exception e){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, ResponseCode.FIELD_NOTVALID.getMessage()));
        }

        //检查时间
        LocalDateTime beginTime=null;
        LocalDateTime endTime=null;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            beginTime=LocalDateTime.parse(shareActivityVo.getBeginTime(),formatter);
            endTime=LocalDateTime.parse(shareActivityVo.getEndTime(),formatter);
        }catch(Exception e){
            logger.error("DateTime format error " + e.getMessage());
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("前端传来的时间格式不对:%s", e.getMessage())));
        }
        if(beginTime.isAfter(endTime)) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "开始时间大于结束时间"));
        }

        ShareActivity shareActivity=new ShareActivity();
        shareActivity.setShopId(shopId);
        shareActivity.setId(id);
        shareActivity.setBeginTime(beginTime);
        shareActivity.setEndTime(endTime);
        shareActivity.setStrategy(shareActivityVo.getStrategy());
        shareActivity.setState((byte) 0);
        ReturnObject<Object> ret=shareActivityService.updateShareActivity(shareActivity);

        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.decorateReturnObject(ret);

    }

    /**
     * @author 潘登 24320182203249
     *
     * @param shopId 商店id
     * @param id 分享活动id
     * @return
     */
    @ApiOperation(value="管理员下线指定分享活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="id", value="分享id", required = true, dataType="Long", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @DeleteMapping("/shops/{shopId}/shareactivities/{id}")
    @Audit
    public Object offline(@PathVariable Long shopId,
                          @PathVariable Long id){
        ReturnObject<Object> ret=shareActivityService.offlineShareActivity(shopId, id);

        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.decorateReturnObject(ret);
    }

    /**
     * @author 潘登 24320182203249
     *
     * @param shopId 商店id
     * @param id 分享活动id
     * @return
     */
    @ApiOperation(value="管理员上线指定分享活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="id", value="分享id", required = true, dataType="Long", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PutMapping("/shops/{shopId}/shareactivities/{id}/online")
    @Audit
    public Object online(@PathVariable Long shopId,
                         @PathVariable Long id){
        ReturnObject<Object> ret=shareActivityService.onlineShareActivity(shopId, id);

        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 新建分享
     *
     * @author 潘登 24320182203249
     * @param userId
     * @param id skuId
     * @return
     */
    @ApiOperation(value="新建分享")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="商品skuId", required = true, dataType="Long", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PostMapping("/skus/{id}/shares")
    @Audit
    public Object createShare(@LoginUser Long userId,
                              @PathVariable Long id){
        ReturnObject<VoObject> ret=shareService.insertShare(userId, id);

        if(ret.getCode().getCode()==0){
            Common.setHttpStatusCode(201,httpServletResponse);
            return Common.getRetObject(ret);
        } else {
            Common.setHttpStatusCode(201,httpServletResponse);
            return Common.decorateReturnObject(ret);
        }
    }

    /**
     * 用户查询分享
     *
     * @author 潘登
     * @param userId
     * @param skuId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value="用户查询分享")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="skuId", value="商品skuId", required = false, dataType="Long", paramType="query"),
            @ApiImplicitParam(name="beginTime", value="开始时间", required = false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="endTime", value="结束时间", required = false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="page", value="页号", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="pageSize", value="页大小", required = false, dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @GetMapping("/shares")
    @Audit
    //TODO: 设置异常情况返回码
    public Object searchShareByCustomer(@LoginUser Long userId,
                                        @RequestParam(required = false) Long skuId,
                                        @RequestParam(required = false) String beginTime,
                                        @RequestParam(required = false) String endTime,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer pageSize){
        LocalDateTime beginTime2 = null;
        LocalDateTime endTime2 = null;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if(beginTime!=null)beginTime2=LocalDateTime.parse(beginTime,formatter);
            if(endTime!=null)endTime2=LocalDateTime.parse(endTime,formatter);
        }catch(Exception e)
        {
            logger.error("DateTime format error " + e.getMessage());
            return Common.getPageRetObject(new ReturnObject<>(new PageInfo<VoObject>(new ArrayList<>())));
        }
        if(beginTime2!=null&&endTime2!=null)
            if(beginTime2.isAfter(endTime2))
                return Common.getPageRetObject(new ReturnObject<>(new PageInfo<VoObject>(new ArrayList<>())));
        page = (page==null) ? 1:page;
        pageSize = (pageSize==null) ? 10:pageSize;
        ReturnObject<PageInfo<VoObject>> ret=shareService.searchByUser(userId,skuId,beginTime2,endTime2,page,pageSize);
        if(ret.getCode().getCode()!=ResponseCode.OK.getCode())
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        ret.getData().setPageNum(page);
        ret.getData().setPageSize(pageSize);
        return Common.getPageRetObject(ret);
    }

    /**
     * 管理员查询分享
     *
     * @author 潘登 24320182203249
     * @param did
     * @param id skuid
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value="管理员查询分享")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="did", value="商店id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="id", value="skuid", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="page", value="页号", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="pageSize", value="页大小", required = false, dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @GetMapping("/shops/{did}/skus/{id}/shares")
    @Audit
    public Object searchShareByAdmin(@PathVariable Long did,
                                     @PathVariable Long id,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer pageSize){
        page = (page==null) ? 1:page;
        pageSize = (pageSize==null) ? 10:pageSize;
        ReturnObject<PageInfo<VoObject>> ret=shareService.searchByAdmin(did, id, page, pageSize);

        setResponse(ret.getCode().getCode(),httpServletResponse);
        ret.getData().setPageNum(page);
        ret.getData().setPageSize(pageSize);
        return Common.getPageRetObject(ret);
    }


    /**
     * 用户查询被分享成功记录
     *
     * @author 潘登24320182203249
     *
     * @param userId
     * @param skuId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value="用户查询被分享成功记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="skuId", value="商品skuId", required = false, dataType="Long", paramType="query"),
            @ApiImplicitParam(name="beginTime", value="开始时间", required = false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="endTime", value="结束时间", required = false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="page", value="页号", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="pageSize", value="页大小", required = false, dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
        @GetMapping("/beshared")
    @Audit
    //TODO: 设置异常情况返回码
    public Object searchBeShareByCustomer(@LoginUser Long userId,
                                          @RequestParam(required = false) Long skuId,
                                          @RequestParam(required = false) String beginTime,
                                          @RequestParam(required = false) String endTime,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer pageSize){
        page = (page==null) ? 1:page;
        pageSize = (pageSize==null) ? 10:pageSize;
        LocalDateTime beginTime2 = null;
        LocalDateTime endTime2 = null;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if(beginTime!=null)beginTime2=LocalDateTime.parse(beginTime,formatter);
            if(endTime!=null)endTime2=LocalDateTime.parse(endTime,formatter);
        }catch(Exception e)
        {
            PageInfo<VoObject> ret= (new PageInfo<VoObject>(new ArrayList<>()));
            ret.setPageNum(page);
            ret.setPageSize(pageSize);
            return Common.getPageRetObject(new ReturnObject<>(ret));
        }
        if(beginTime2!=null&&endTime2!=null)
            if(beginTime2.isAfter(endTime2)) {
                //return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.Log_Bigger, "开始时间大于结束时间"));
                PageInfo<VoObject> ret= (new PageInfo<VoObject>(new ArrayList<>()));
                ret.setPageNum(page);
                ret.setPageSize(pageSize);
                return Common.getPageRetObject(new ReturnObject<>(ret));
            }
        ReturnObject<PageInfo<VoObject>> ret=beShareService.searchByUser(userId,skuId,beginTime2,endTime2,page,pageSize);

        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.getPageRetObject(ret);
    }


    /**
     * 管理员查询被分享成功记录
     *
     * @author 潘登 24320182203249
     *
     * @param did 商店id
     * @param id skuId
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value="管理员被分享成功记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="did", value="商店id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="id", value="skuid", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="skuId", value="商品skuId", required = false, dataType="Long", paramType="query"),
            @ApiImplicitParam(name="page", value="页号", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="pageSize", value="页大小", required = false, dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @GetMapping("/shops/{did}/skus/{id}/beshared")
    @Audit
    public Object searchBeShareByAdmin(@PathVariable Long did,
                                       @PathVariable Long id,
                                       @RequestParam(required = false) String beginTime,
                                       @RequestParam(required = false) String endTime,
                                       @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer pageSize){
        page = (page==null) ? 1:page;
        pageSize = (pageSize==null) ? 10:pageSize;
        LocalDateTime beginTime2 = null;
        LocalDateTime endTime2 = null;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if(beginTime!=null)beginTime2=LocalDateTime.parse(beginTime,formatter);
            if(endTime!=null)endTime2=LocalDateTime.parse(endTime,formatter);
        }catch(Exception e)
        {
            PageInfo<VoObject> ret= (new PageInfo<VoObject>(new ArrayList<>()));
            ret.setPageNum(page);
            ret.setPageSize(pageSize);
            return Common.getPageRetObject(new ReturnObject<>(ret));
        }
        if(beginTime2!=null&&endTime2!=null)
            if(beginTime2.isAfter(endTime2)) {
                //return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.Log_Bigger, "开始时间大于结束时间"));
                PageInfo<VoObject> ret= (new PageInfo<VoObject>(new ArrayList<>()));
                ret.setPageNum(page);
                ret.setPageSize(pageSize);
                return Common.getPageRetObject(new ReturnObject<>(ret));
            }


        ReturnObject<PageInfo<VoObject>> ret =  beShareService.searchByAdmin(did, id,beginTime2,endTime2, page, pageSize);

        setResponse(ret.getCode().getCode(),httpServletResponse);
        ret.getData().setPageNum(page);
        ret.getData().setPageSize(pageSize);
        return Common.getPageRetObject(ret);
    }
}
