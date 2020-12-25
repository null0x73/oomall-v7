//package cn.edu.xmu.other.controller;
//
//import cn.edu.xmu.ooad.annotation.Audit;
//import cn.edu.xmu.ooad.annotation.LoginUser;
//import cn.edu.xmu.ooad.model.VoObject;
//import cn.edu.xmu.ooad.util.Common;
//import cn.edu.xmu.ooad.util.ResponseCode;
//import cn.edu.xmu.ooad.util.ReturnObject;
//import cn.edu.xmu.other.model.bo.AfterSale;
//import cn.edu.xmu.other.model.bo.AfterSaleState;
//import cn.edu.xmu.other.model.vo.AfterSaleConfirmVo;
//import cn.edu.xmu.other.model.vo.AfterSaleLogSnVo;
//import cn.edu.xmu.other.model.vo.AfterSaleShopReceiveVo;
//import cn.edu.xmu.other.model.vo.AfterSaleVo;
//import cn.edu.xmu.other.service.AfterSaleService;
//import com.github.pagehelper.PageInfo;
//import io.swagger.annotations.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletResponse;
//import java.time.LocalDateTime;
//
//@Api(value = "售后服务", tags = "aftersale")
//@RestController /*Restful的Controller对象*/
//@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
//public class AfterSaleController {
//    private  static  final Logger logger = LoggerFactory.getLogger(AfterSaleController.class);
//
//    @Autowired
//    private AfterSaleService afterSaleService;
//
//    @Autowired
//    private HttpServletResponse httpServletResponse;
//
//
//    void setResponse(int state,HttpServletResponse httpServletResponse){
//        if(state!=ResponseCode.OK.getCode())
//            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//        if(state==ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
//            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
//        if(state==ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
//        if(state==ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//    }
//
//    /**
//     * 获得所有售后状态
//     * @author 潘登 24320182203249
//     * @return
//     */
//    @ApiOperation(value="查询售后所有状态")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header")
//    })
//    @ApiResponses({
//            @ApiResponse(code= 0,message = "成功")
//    })
//    @GetMapping("/aftersales/states")
//    @Audit
//    public Object findAllState(){
//        return Common.getRetObject(new ReturnObject<>(new AfterSaleState()));
//    }
//
//    /**
//     * 申请售后请求（插入新的aftersale）
//     *
//     * @author 潘登 24320182203249
//     * @param userId
//     * @param id orderItemId
//     * @param afterSaleVo
//     * @return
//     */
//    @ApiOperation(value="申请售后请求")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="订单明细id", required = true, dataType="Long", paramType="path"),
//            @ApiImplicitParam(name="afterSaleVo", value="分享活动输入Vo", required = true, dataType = "AfterSaleVo", paramType = "body")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @PostMapping("/orderitems/{id}/aftersales")
//    @Audit
//    public Object requestAfterSale(@LoginUser Long userId,
//                                   @PathVariable Long id,
//                                   @Validated @RequestBody AfterSaleVo afterSaleVo,
//                                   BindingResult bindingResult){
//        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
//        if (null != returnObject) {
//            logger.debug("validate fail");
//            return returnObject;
//        }
//        logger.debug("*flag1");
//        AfterSale afterSale=new AfterSale();
//        afterSale.setCustomerId(userId);
//        afterSale.setOrderItemId(id);
//
//        afterSale.setType(afterSaleVo.getType());
//        afterSale.setQuantity(afterSaleVo.getQuantity());
//        afterSale.setReason(afterSaleVo.getReason());
//        afterSale.setRegionId(afterSaleVo.getRegionId());
//        afterSale.setDetail(afterSaleVo.getDetail());
//        afterSale.setConsignee(afterSaleVo.getConsignee());
//        afterSale.setMobile(afterSaleVo.getMobile());
//
//        ReturnObject<VoObject> ret=afterSaleService.createAfterSale(afterSale);
//        //ReturnObject<VoObject> ret=new ReturnObject<>(new AfterSale());
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        if(ret.getCode().getCode()==ResponseCode.OK.getCode())
//            httpServletResponse.setStatus(HttpStatus.CREATED.value());
//        logger.debug(ret.getData().toString());
//        logger.debug(ret.getCode().getMessage());
//        Object retObject = Common.getRetObject(ret);
//        logger.debug("*"+retObject.toString());
//        return retObject;
//
//    }
//
//    /**
//     * 用户查询分享
//     *
//     * @author 潘登 24320182203249
//     *
//     * @param userId
//     * @param beginTime
//     * @param endTime
//     * @param page
//     * @param pageSize
//     * @param type
//     * @param state
//     * @return
//     */
//    @ApiOperation(value="用户查询分享")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="beginTime", value="结束时间", required = false, dataType="String", paramType="query"),
//            @ApiImplicitParam(name="endTime", value="结束时间", required = false, dataType="String", paramType="query"),
//            @ApiImplicitParam(name="page", value="页号", required = false, dataType = "Integer", paramType = "query"),
//            @ApiImplicitParam(name="pageSize", value="页大小", required = false, dataType = "Integer", paramType = "query"),
//            @ApiImplicitParam(name="type", value="种类", required = false, dataType = "Integer", paramType = "query"),
//            @ApiImplicitParam(name="state", value="状态", required = false, dataType = "Integer", paramType = "query")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @GetMapping("/aftersales")
//    @Audit
//    public Object searchAfterSaleByUser(@LoginUser Long userId,
//                                        @RequestParam(required = false) String beginTime,
//                                        @RequestParam(required = false) String endTime,
//                                        @RequestParam(required = false) Integer page,
//                                        @RequestParam(required = false) Integer pageSize,
//                                        @RequestParam(required = false) Integer type,
//                                        @RequestParam(required = false) Integer state){
//        LocalDateTime beginTime2 = null;
//        LocalDateTime endTime2 = null;
//        try{
//            if(beginTime!=null)beginTime2=LocalDateTime.parse(beginTime);
//            if(endTime!=null)endTime2=LocalDateTime.parse(endTime);
//        }catch(Exception e)
//        {
//            logger.error("DateTime format error " + e.getMessage());
//            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("前端传来的时间格式不对：%s", e.getMessage())));
//        }
//        if(beginTime2!=null&&endTime2!=null)
//            if(beginTime2.isAfter(endTime2))
//                return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.Log_Bigger, "开始时间大于结束时间"));
//        page = (page==null) ? 1:page;
//        pageSize = (pageSize==null) ? 10:pageSize;
//        ReturnObject<PageInfo<VoObject>> ret=afterSaleService.searchAfterSaleByUser(userId,beginTime2,endTime2,page,pageSize,type==null?null:type.byteValue(),state==null?null:state.byteValue());
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.getPageRetObject(ret);
//    }
//
//    /**
//     * 管理员查找本店铺的售后服务
//     *
//     * @author 潘登 24320182203249
//     *
//     * @param id shopId
//     * @param beginTime
//     * @param endTime
//     * @param page
//     * @param pageSize
//     * @param type
//     * @param state
//     * @return
//     */
//    @ApiOperation(value="管理员查询分享")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="店铺id", required = true, dataType = "Long", paramType = "path"),
//            @ApiImplicitParam(name="beginTime", value="结束时间", required = false, dataType="String", paramType="query"),
//            @ApiImplicitParam(name="endTime", value="结束时间", required = false, dataType="String", paramType="query"),
//            @ApiImplicitParam(name="page", value="页号", required = false, dataType = "Integer", paramType = "query"),
//            @ApiImplicitParam(name="pageSize", value="页大小", required = false, dataType = "Integer", paramType = "query"),
//            @ApiImplicitParam(name="type", value="种类", required = false, dataType = "Integer", paramType = "query"),
//            @ApiImplicitParam(name="state", value="状态", required = false, dataType = "Integer", paramType = "query")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @GetMapping("/shops/{id}/aftersales")
//    @Audit
//    public Object searchAfterSaleByrAdmin(@PathVariable Long id,
//                                        @RequestParam(required = false) String beginTime,
//                                        @RequestParam(required = false) String endTime,
//                                        @RequestParam(required = false) Integer page,
//                                        @RequestParam(required = false) Integer pageSize,
//                                        @RequestParam(required = false) Integer type,
//                                        @RequestParam(required = false) Integer state){
//        LocalDateTime beginTime2 = null;
//        LocalDateTime endTime2 = null;
//        try{
//            if(beginTime!=null)beginTime2=LocalDateTime.parse(beginTime);
//            if(endTime!=null)endTime2=LocalDateTime.parse(endTime);
//        }catch(Exception e)
//        {
//            logger.error("DateTime format error " + e.getMessage());
//            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("前端传来的时间格式不对：%s", e.getMessage())));
//        }
//        if(beginTime2!=null&&endTime2!=null)
//            if(beginTime2.isAfter(endTime2))
//                return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.Log_Bigger, "开始时间大于结束时间"));
//        page = (page==null) ? 1:page;
//        pageSize = (pageSize==null) ? 10:pageSize;
//        ReturnObject<PageInfo<VoObject>> ret=afterSaleService.searchAfterSaleByAdmin(id,beginTime2,endTime2,page,pageSize,type.byteValue(),state.byteValue());
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.getPageRetObject(ret);
//    }
//
//    /**
//     * 用户根据主键查找对应售后单
//     *
//     * @author 潘登 24320182203249
//     *
//     * @param id 售后单id
//     * @param userId
//     * @return
//     */
//    @ApiOperation(value="用户根据售后id查找对售后服务")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="售后id", required = true, dataType = "Long", paramType = "path")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @GetMapping("/aftersales/{id}")
//    @Audit
//    public Object searchByUserByPrimaryKey(@PathVariable Long id,
//                                           @LoginUser Long userId){
//        ReturnObject<VoObject> ret=afterSaleService.selectByPrimaryKeyByUser(id, userId);
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.getRetObject(ret);
//    }
//
//    /**
//     * 用户修改售后请求
//     *
//     * @author 潘登 24320182203249
//     *
//     * @param userId
//     * @param id
//     * @param afterSaleVo
//     * @return
//     */
//    @ApiOperation(value="用户修改售后请求")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path"),
//            @ApiImplicitParam(name="afterSaleVo", value="分享活动输入Vo", required = true, dataType = "AfterSaleVo", paramType = "body")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @PutMapping("/aftersales/{id}")
//    @Audit
//    public Object updateAfterSaleByUser(@LoginUser Long userId,
//                                        @PathVariable Long id,
//                                        @RequestBody AfterSaleVo afterSaleVo){
//        AfterSale afterSale=new AfterSale();
//        //afterSale.setCustomerId(userId);
//        afterSale.setId(id);
//
//        afterSale.setQuantity(afterSaleVo.getQuantity());
//        afterSale.setReason(afterSaleVo.getReason());
//        afterSale.setRegionId(afterSaleVo.getRegionId());
//        afterSale.setDetail(afterSaleVo.getDetail());
//        afterSale.setConsignee(afterSaleVo.getConsignee());
//        afterSale.setMobile(afterSaleVo.getMobile());
//
//        ReturnObject<Object> ret=afterSaleService.updateAfterSaleByUser(afterSale,userId);
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.decorateReturnObject(ret);
//    }
//
//
//    /**
//     * 用户逻辑删除或者取消订单
//     *
//     * @param userId
//     * @param id
//     * @return
//     */
//    @ApiOperation(value="用户取消或者逻辑删除售后单")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="售后id", required = true, dataType = "Long", paramType = "path"),
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @DeleteMapping("/aftersales/{id}")
//    @Audit
//    public Object cancelOrLogicDeleteAfterSale(@LoginUser Long userId,
//                                               @PathVariable Long id){
//        ReturnObject<Object> ret=afterSaleService.cancelOrLogicDeleteAfterSale(id,userId);
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.decorateReturnObject(ret);
//    }
//
//
//    /**
//     * 买家填写运单号
//     *
//     * @#author 潘登 24320182203249
//     * @param userId
//     * @param id
//     * @param afterSaleLogSnVo
//     * @return
//     */
//    @ApiOperation(value="买家填写运单号")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path"),
//            @ApiImplicitParam(name="afterSaleLogSnVo", value="用户运单号", required = true, dataType = "AfterSaleLogSnVo", paramType = "body")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @PutMapping("/aftersales/{id}/sendback")
//    @Audit
//    public Object sendBackByUser(@LoginUser Long userId,
//                                 @PathVariable Long id,
//                                 @Validated @RequestBody AfterSaleLogSnVo afterSaleLogSnVo,
//                                 BindingResult bindingResult){
//        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
//        if (null != returnObject) {
//            logger.debug("validate fail");
//            return returnObject;
//        }
//        String logSn=afterSaleLogSnVo.getLogSn();
//        ReturnObject<Object> ret=afterSaleService.sendBackByUser(id,userId,logSn);
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.decorateReturnObject(ret);
//    }
//
//
//    /**
//     * 买家确认售后结束
//     *
//     * @author 潘登 24320182203249
//     *
//     * @param userId
//     * @param id
//     * @return
//     */
//    @ApiOperation(value="买家确认售后结束")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @PutMapping("/aftersales/{id}/confirm")
//    @Audit
//    public Object userConfirm(@LoginUser Long userId,
//                                 @PathVariable Long id){
//        ReturnObject<Object> ret=afterSaleService.userConfirm(id, userId);
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.decorateReturnObject(ret);
//    }
//
//    /**
//     * 管理员根据主键查找对应的售后服务
//     *
//     * @param id
//     * @param shopId
//     * @return
//     */
//    @ApiOperation(value="管理员根据售后id查找对售后服务")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="售后id", required = true, dataType = "Long", paramType = "path"),
//            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType = "Long", paramType = "path")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @GetMapping("/shops/{shopId}/aftersales/{id}")
//    @Audit
//    public Object selectByPrimaryKeyByAdmin(@PathVariable Long id,
//                                            @PathVariable Long shopId){
//        ReturnObject<VoObject> ret =afterSaleService.selectByPrimaryKeyByAdmin(id, shopId);
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.getRetObject(ret);
//    }
//
//    /**
//     * 管理员同意/不同意售后请求
//     *
//     * @author 潘登 24320182203249
//     * @param id 售后id
//     * @param shopId
//     * @param afterSaleConfirmVo
//     * @return
//     */
//    @ApiOperation(value="管理员同意/不同意")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path"),
//            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType="Long", paramType="path"),
//            @ApiImplicitParam(name="afterSaleConfirmVo", value="管理员处理意见", required = true, dataType="AfterSaleConfirmVo", paramType="body")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @PutMapping("/shops/{shopId}/aftersales/{id}/confirm")
//    @Audit
//    public Object adminConfirm(@PathVariable Long id,
//                               @PathVariable Long shopId,
//                               @RequestBody AfterSaleConfirmVo afterSaleConfirmVo){
//        ReturnObject<Object> ret =afterSaleService.adminConfirm(id,shopId,afterSaleConfirmVo.getConfirm(),afterSaleConfirmVo.getPrice(),afterSaleConfirmVo.getConclusion(),afterSaleConfirmVo.getType());
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.decorateReturnObject(ret);
//    }
//
//
//    /**
//     * 店铺收到信息
//     *
//     * @author 潘登 24320182203249
//     *
//     * @param id
//     * @param shopId
//     * @param afterSaleShopReceiveVo
//     * @return
//     */
//    @ApiOperation(value="店铺收到货物")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path"),
//            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType="Long", paramType="path"),
//            @ApiImplicitParam(name="afterSaleShopReceiveVo", value="管理员处理意见", required = true, dataType="AfterSaleShopReceiveVo", paramType="body")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @PutMapping("/shops/{shopId}/aftersales/{id}/receive")
//    @Audit
//    public Object shopReceive(@PathVariable Long id,
//                              @PathVariable Long shopId,
//                              @RequestBody AfterSaleShopReceiveVo afterSaleShopReceiveVo){
//        ReturnObject<Object> ret=afterSaleService.shopReceive(id,shopId,afterSaleShopReceiveVo.getConfirm(),afterSaleShopReceiveVo.getConclusion());
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.decorateReturnObject(ret);
//    }
//
//    @ApiOperation(value="店铺寄出货物")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
//            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path"),
//            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType="Long", paramType="path"),
//            @ApiImplicitParam(name="logSn", value="运单号", required = true, dataType="String", paramType="body")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,message = "成功")
//    })
//    @PutMapping("shops/{shopId}/aftersales/{id}/deliver")
//    @Audit
//    public Object shopSend(@PathVariable Long id,
//                           @PathVariable Long shopId,
//                           @RequestBody(required = false) AfterSaleLogSnVo afterSaleLogSnVo){
//        String logSn=null;
//        if(afterSaleLogSnVo!=null)logSn=afterSaleLogSnVo.getLogSn();
//        ReturnObject<Object> ret=afterSaleService.shopSend(id,shopId,logSn);
//        setResponse(ret.getCode().getCode(),httpServletResponse);
//        return Common.decorateReturnObject(ret);
//    }
//
//
//
//    @GetMapping("/test")
//    @Audit
//    public Object test(@LoginUser Long userId)
//    {
//        logger.debug(userId.toString());
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        return Common.decorateReturnObject(new ReturnObject<>(userId.toString()));
//    }
//
//}



package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.model.bo.AfterSale;
import cn.edu.xmu.other.model.bo.AfterSaleState;
import cn.edu.xmu.other.model.vo.AfterSaleConfirmVo;
import cn.edu.xmu.other.model.vo.AfterSaleLogSnVo;
import cn.edu.xmu.other.model.vo.AfterSaleShopReceiveVo;
import cn.edu.xmu.other.model.vo.AfterSaleVo;
import cn.edu.xmu.other.service.AfterSaleService;
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

@Api(value = "售后服务", tags = "aftersale")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class AfterSaleController {
    private  static  final Logger logger = LoggerFactory.getLogger(AfterSaleController.class);

    @Autowired
    private AfterSaleService afterSaleService;

    @Autowired
    private HttpServletResponse httpServletResponse;


    void setResponse(int state,HttpServletResponse httpServletResponse){
        if(state!=ResponseCode.OK.getCode())
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        if(state==ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        if(state==ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        if(state==ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
            httpServletResponse.setStatus(HttpStatus.OK.value());
    }

    /**
     * 获得所有售后状态
     * @author 潘登 24320182203249
     * @return
     */
    @ApiOperation(value="查询售后所有状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header")
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @GetMapping("/aftersales/states")
    @Audit
    public Object findAllState(){
        return Common.getRetObject(new ReturnObject<>(new AfterSaleState()));
    }

    /**
     * 申请售后请求（插入新的aftersale）
     *
     * @author 潘登 24320182203249
     * @param userId
     * @param id orderItemId
     * @param afterSaleVo
     * @return
     */
    @ApiOperation(value="申请售后请求")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="订单明细id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="afterSaleVo", value="分享活动输入Vo", required = true, dataType = "AfterSaleVo", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PostMapping("/orderitems/{id}/aftersales")
    @Audit
    public Object requestAfterSale(@LoginUser Long userId,
                                   @PathVariable Long id,
                                   @Validated @RequestBody AfterSaleVo afterSaleVo,
                                   BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        AfterSale afterSale=new AfterSale();
        afterSale.setCustomerId(userId);
        afterSale.setOrderItemId(id);

        afterSale.setType(afterSaleVo.getType());
        afterSale.setQuantity(afterSaleVo.getQuantity());
        afterSale.setReason(afterSaleVo.getReason());
        afterSale.setRegionId(afterSaleVo.getRegionId());
        afterSale.setDetail(afterSaleVo.getDetail());
        afterSale.setConsignee(afterSaleVo.getConsignee());
        afterSale.setMobile(afterSaleVo.getMobile());

        ReturnObject<VoObject> ret=afterSaleService.createAfterSale(afterSale);
        //ReturnObject<VoObject> ret=new ReturnObject<>(new AfterSale());
        setResponse(ret.getCode().getCode(),httpServletResponse);
        if(ret.getCode().getCode()==ResponseCode.OK.getCode())
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
        logger.debug(ret.getData().toString());
        logger.debug(ret.getCode().getMessage());
        Object retObject = Common.getRetObject(ret);
        logger.debug("*"+retObject.toString());
        return retObject;

    }

    /**
     * 用户查询分享
     *
     * @author 潘登 24320182203249
     *
     * @param userId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @param type
     * @param state
     * @return
     */
    @ApiOperation(value="用户查询分享")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="beginTime", value="结束时间", required = false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="endTime", value="结束时间", required = false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="page", value="页号", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="pageSize", value="页大小", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="type", value="种类", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="state", value="状态", required = false, dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @GetMapping("/aftersales")
    @Audit
    public Object searchAfterSaleByUser(@LoginUser Long userId,
                                        @RequestParam(required = false) String beginTime,
                                        @RequestParam(required = false) String endTime,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer pageSize,
                                        @RequestParam(required = false) Integer type,
                                        @RequestParam(required = false) Integer state){
        LocalDateTime beginTime2 = null;
        LocalDateTime endTime2 = null;
        try{
            if(beginTime!=null)beginTime2=LocalDateTime.parse(beginTime);
            if(endTime!=null)endTime2=LocalDateTime.parse(endTime);
        }catch(Exception e)
        {
            logger.error("DateTime format error " + e.getMessage());
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("前端传来的时间格式不对：%s", e.getMessage())));
        }
        if(beginTime2!=null&&endTime2!=null)
            if(beginTime2.isAfter(endTime2))
                return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.Log_Bigger, "开始时间大于结束时间"));
        page = (page==null) ? 1:page;
        pageSize = (pageSize==null) ? 10:pageSize;
        ReturnObject<PageInfo<VoObject>> ret=afterSaleService.searchAfterSaleByUser(userId,beginTime2,endTime2,page,pageSize,type==null?null:type.byteValue(),state==null?null:state.byteValue());
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.getPageRetObject(ret);
    }

    /**
     * 管理员查找本店铺的售后服务
     *
     * @author 潘登 24320182203249
     *
     * @param id shopId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @param type
     * @param state
     * @return
     */
    @ApiOperation(value="管理员查询分享")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="店铺id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name="beginTime", value="结束时间", required = false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="endTime", value="结束时间", required = false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="page", value="页号", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="pageSize", value="页大小", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="type", value="种类", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="state", value="状态", required = false, dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @GetMapping("/shops/{id}/aftersales")
    @Audit
    public Object searchAfterSaleByrAdmin(@PathVariable Long id,
                                          @RequestParam(required = false) String beginTime,
                                          @RequestParam(required = false) String endTime,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer pageSize,
                                          @RequestParam(required = false) Integer type,
                                          @RequestParam(required = false) Integer state){
        LocalDateTime beginTime2 = null;
        LocalDateTime endTime2 = null;
        try{
            if(beginTime!=null)beginTime2=LocalDateTime.parse(beginTime);
            if(endTime!=null)endTime2=LocalDateTime.parse(endTime);
        }catch(Exception e)
        {
            logger.error("DateTime format error " + e.getMessage());
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("前端传来的时间格式不对：%s", e.getMessage())));
        }
        if(beginTime2!=null&&endTime2!=null)
            if(beginTime2.isAfter(endTime2))
                return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.Log_Bigger, "开始时间大于结束时间"));
        page = (page==null) ? 1:page;
        pageSize = (pageSize==null) ? 10:pageSize;
        ReturnObject<PageInfo<VoObject>> ret=afterSaleService.searchAfterSaleByAdmin(id,beginTime2,endTime2,page,pageSize,type.byteValue(),state.byteValue());
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.getPageRetObject(ret);
    }

    /**
     * 用户根据主键查找对应售后单
     *
     * @author 潘登 24320182203249
     *
     * @param id 售后单id
     * @param userId
     * @return
     */
    @ApiOperation(value="用户根据售后id查找对售后服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="售后id", required = true, dataType = "Long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @GetMapping("/aftersales/{id}")
    @Audit
    public Object searchByUserByPrimaryKey(@PathVariable Long id,
                                           @LoginUser Long userId){
        ReturnObject<VoObject> ret=afterSaleService.selectByPrimaryKeyByUser(id, userId);
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.getRetObject(ret);
    }

    /**
     * 用户修改售后请求
     *
     * @author 潘登 24320182203249
     *
     * @param userId
     * @param id
     * @param afterSaleVo
     * @return
     */
    @ApiOperation(value="用户修改售后请求")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="afterSaleVo", value="分享活动输入Vo", required = true, dataType = "AfterSaleVo", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PutMapping("/aftersales/{id}")
    @Audit
    public Object updateAfterSaleByUser(@LoginUser Long userId,
                                        @PathVariable Long id,
                                        @RequestBody AfterSaleVo afterSaleVo){
        AfterSale afterSale=new AfterSale();
        //afterSale.setCustomerId(userId);
        afterSale.setId(id);

        afterSale.setQuantity(afterSaleVo.getQuantity());
        afterSale.setReason(afterSaleVo.getReason());
        afterSale.setRegionId(afterSaleVo.getRegionId());
        afterSale.setDetail(afterSaleVo.getDetail());
        afterSale.setConsignee(afterSaleVo.getConsignee());
        afterSale.setMobile(afterSaleVo.getMobile());

        ReturnObject<Object> ret=afterSaleService.updateAfterSaleByUser(afterSale,userId);
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.decorateReturnObject(ret);
    }


    /**
     * 用户逻辑删除或者取消订单
     *
     * @param userId
     * @param id
     * @return
     */
    @ApiOperation(value="用户取消或者逻辑删除售后单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="售后id", required = true, dataType = "Long", paramType = "path"),
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @DeleteMapping("/aftersales/{id}")
    @Audit
    public Object cancelOrLogicDeleteAfterSale(@LoginUser Long userId,
                                               @PathVariable Long id){
        ReturnObject<Object> ret=afterSaleService.cancelOrLogicDeleteAfterSale(id,userId);
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.decorateReturnObject(ret);
    }


    /**
     * 买家填写运单号
     *
     * @#author 潘登 24320182203249
     * @param userId
     * @param id
     * @param afterSaleLogSnVo
     * @return
     */
    @ApiOperation(value="买家填写运单号")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="afterSaleLogSnVo", value="用户运单号", required = true, dataType = "AfterSaleLogSnVo", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PutMapping("/aftersales/{id}/sendback")
    @Audit
    public Object sendBackByUser(@LoginUser Long userId,
                                 @PathVariable Long id,
                                 @Validated @RequestBody AfterSaleLogSnVo afterSaleLogSnVo,
                                 BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        String logSn=afterSaleLogSnVo.getLogSn();
        ReturnObject<Object> ret=afterSaleService.sendBackByUser(id,userId,logSn);
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.decorateReturnObject(ret);
    }


    /**
     * 买家确认售后结束
     *
     * @author 潘登 24320182203249
     *
     * @param userId
     * @param id
     * @return
     */
    @ApiOperation(value="买家确认售后结束")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PutMapping("/aftersales/{id}/confirm")
    @Audit
    public Object userConfirm(@LoginUser Long userId,
                              @PathVariable Long id){

        System.out.println("CONFIRM");

        ReturnObject<Object> ret=afterSaleService.userConfirm(id, userId);
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 管理员根据主键查找对应的售后服务
     *
     * @param id
     * @param shopId
     * @return
     */
    @ApiOperation(value="管理员根据售后id查找对售后服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="售后id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType = "Long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @GetMapping("/shops/{shopId}/aftersales/{id}")
    @Audit
    public Object selectByPrimaryKeyByAdmin(@PathVariable Long id,
                                            @PathVariable Long shopId){
        ReturnObject<VoObject> ret =afterSaleService.selectByPrimaryKeyByAdmin(id, shopId);
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.getRetObject(ret);
    }

    /**
     * 管理员同意/不同意售后请求
     *
     * @author 潘登 24320182203249
     * @param id 售后id
     * @param shopId
     * @param afterSaleConfirmVo
     * @return
     */
    @ApiOperation(value="管理员同意/不同意")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="afterSaleConfirmVo", value="管理员处理意见", required = true, dataType="AfterSaleConfirmVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PutMapping("/shops/{shopId}/aftersales/{id}/confirm")
    @Audit
    public Object adminConfirm(@PathVariable Long id,
                               @PathVariable Long shopId,
                               @RequestBody AfterSaleConfirmVo afterSaleConfirmVo){
        ReturnObject<Object> ret =afterSaleService.adminConfirm(id,shopId,afterSaleConfirmVo.getConfirm(),afterSaleConfirmVo.getPrice(),afterSaleConfirmVo.getConclusion(),afterSaleConfirmVo.getType());
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.decorateReturnObject(ret);
    }


    /**
     * 店铺收到信息
     *
     * @author 潘登 24320182203249
     *
     * @param id
     * @param shopId
     * @param afterSaleShopReceiveVo
     * @return
     */
    @ApiOperation(value="店铺收到货物")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="afterSaleShopReceiveVo", value="管理员处理意见", required = true, dataType="AfterSaleShopReceiveVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PutMapping("/shops/{shopId}/aftersales/{id}/receive")
    @Audit
    public Object shopReceive(@PathVariable Long id,
                              @PathVariable Long shopId,
                              @RequestBody AfterSaleShopReceiveVo afterSaleShopReceiveVo){
        ReturnObject<Object> ret=afterSaleService.shopReceive(id,shopId,afterSaleShopReceiveVo.getConfirm(),afterSaleShopReceiveVo.getConclusion());
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.decorateReturnObject(ret);
    }

    @ApiOperation(value="店铺寄出货物")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="售后单id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="shopId", value="商店id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="logSn", value="运单号", required = true, dataType="String", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @PutMapping("shops/{shopId}/aftersales/{id}/deliver")
    @Audit
    public Object shopSend(@PathVariable Long id,
                           @PathVariable Long shopId,
                           @RequestBody(required = false) AfterSaleLogSnVo afterSaleLogSnVo){

        System.out.println("DELIVER");

        String logSn=null;
        if(afterSaleLogSnVo!=null)logSn=afterSaleLogSnVo.getLogSn();
        ReturnObject<Object> ret=afterSaleService.shopSend(id,shopId,logSn);
        setResponse(ret.getCode().getCode(),httpServletResponse);
        return Common.decorateReturnObject(ret);
    }



    @GetMapping("/test")
    @Audit
    public Object test(@LoginUser Long userId)
    {
        logger.debug(userId.toString());
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return Common.decorateReturnObject(new ReturnObject<>(userId.toString()));
    }

}
