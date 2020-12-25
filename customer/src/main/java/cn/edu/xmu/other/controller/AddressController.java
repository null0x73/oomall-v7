package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.model.bo.Address;
import cn.edu.xmu.other.model.bo.Region;
import cn.edu.xmu.other.model.vo.AddressRecieveVo;
import cn.edu.xmu.other.model.vo.RegionRecieveVo;
import cn.edu.xmu.other.service.AddressService;
import cn.edu.xmu.other.service.RegionService;
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
import java.util.List;


@Api(value = "地址服务", tags = "address")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class AddressController {

    private  static  final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;


    @Autowired
    private RegionService regionService;

    @Autowired
    private AddressService addressService;

    /**
     * 查询某个地区的所有上级地区
     *
     * @author 24320182203193 何明祥
     * @param id 收藏商品id
     * @return Object
     * createBy 何明祥 2020/12/5 19:31
     */
    @ApiOperation(value="查询上级地区")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name="id", value="该地区id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @GetMapping("/region/{id}/ancestor")
    public Object getAncestor(@PathVariable("id") Long id){
        logger.debug("getAncestor id:" + id);
        Object returnObject;
        ReturnObject<List> region =  regionService.getAncestor(id);
        if(region.getCode().getCode()==504){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return Common.getNullRetObj(new ReturnObject<>(region.getCode(), region.getErrmsg()), httpServletResponse);
        }
        if(region.getCode()==ResponseCode.REGION_OBSOLETE)
        {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.getNullRetObj(new ReturnObject<>(region.getCode(), region.getErrmsg()), httpServletResponse);
        }
        logger.debug("getAncestor: region = " + region.getData());
        returnObject = Common.getListRetObject(region);
        return returnObject;

    }


    /**
     * 管理员在地区下新增子地区
     *
     * @author 24320182203193 何明祥
     * @param id 地区id
     * @param regionRecieveVo 地区视图
     * @return Object
     * createBy 何明祥 2020/12/5 19:31
     */
    @ApiOperation(value = "新增子地区", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "该地区id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "RegionRecieveVo", name = "regionRecieveVo", value = "地区视图", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 602, message = "地区已废弃"),
    })
    @Audit
    @PostMapping("/shops/{did}/regions/{id}/subregions")
    public Object insertSubregion(@Validated @RequestBody RegionRecieveVo regionRecieveVo,BindingResult bindingResult,
                                  @PathVariable("id")Long id) {
        Object o = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != o) {
            return o;
        }
        logger.debug("insert subregion of region:" + id);
        Region region= regionRecieveVo.createRegion();
        region.setPid(id);
        region.setState(Region.State.VALID);
        region.setGmtCreate(LocalDateTime.now());

        ReturnObject retObject = regionService.insertSubregion(region);
        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.decorateReturnObject(retObject);
        } else if(retObject.getCode()==ResponseCode.RESOURCE_ID_NOTEXIST){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        } else {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }

    }

    /**
     * 管理员修改某个地区
     *
     * @author 24320182203193 何明祥
     * @param id 地区id
     * @param regionRecieveVo 地区视图
     * @return Object
     * createBy 何明祥 2020/12/5 21:25
     */
    @ApiOperation(value = "修改地区信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "该地区id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "RegionRecieveVo", name = "regionRecieveVo", value = "地区视图", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/shops/{did}/regions/{id}")
    public Object updateRegion(@Validated @RequestBody RegionRecieveVo regionRecieveVo,BindingResult bindingResult,
                               @PathVariable("id")Long id) {
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        logger.debug("update region by regionId:" + id);

        ReturnObject retObject = regionService.updateRegion(id, regionRecieveVo);
        if (retObject.getData() != null) {
            return Common.getRetObject(retObject);
        } else if(retObject.getCode()==ResponseCode.RESOURCE_ID_NOTEXIST){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        } else {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }

    }

    /**
     * 管理员使某个地区无效
     *
     * @author 24320182203193 何明祥
     * @param id 地区id
     * @return Object
     * createBy 何明祥 2020/12/6 10:02
     */
    @ApiOperation(value="地区禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",name="id", value="地区id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @DeleteMapping("/shops/{did}/regions/{id}")
    public Object invalidateRegion(@PathVariable("id") Long id){
        if (logger.isDebugEnabled()) {
            logger.debug("invalidateRegion: id = "+ id);
        }
        ReturnObject<Object> ret=regionService.invalidateRegion(id);
        if(ret.getCode()==ResponseCode.RESOURCE_ID_NOTEXIST)
        {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return Common.getNullRetObj(new ReturnObject<>(ret.getCode(), ret.getErrmsg()), httpServletResponse);
        }
        return Common.decorateReturnObject(ret);
    }

    /**
     * 买家新增地址
     *
     * @author 24320182203193 何明祥
     * @param customerId 当前用户id
     * @param addressRecieveVo 地址视图
     * @return Object
     * createBy 何明祥 2020/12/5 19:31
     */
    @ApiOperation(value="增加地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "AddressRecieveVo", name = "addressRecieveVo", value = "地区视图", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功"),
            @ApiResponse(code= 601,message = "达到地址簿上限")
    })
    @Audit
    @PostMapping("/addresses")
    public Object insertAddress(@Validated @RequestBody AddressRecieveVo addressRecieveVo,BindingResult bindingResult,
                                @LoginUser Long customerId){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        logger.debug("insert address of customer:" + customerId);
        Address address= addressRecieveVo.createAddress();
        address.setCustomerId(customerId);
        address.setBeDefault(false);
        address.setGmtCreate(LocalDateTime.now());
        ReturnObject<Address> retObject = addressService.insertAddress(address);
        if(retObject.getCode()== ResponseCode.FIELD_NOTVALID){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, retObject.getErrmsg()), httpServletResponse);
        }
        else if(retObject.getCode()== ResponseCode.REGION_OBSOLETE){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.REGION_OBSOLETE, retObject.getErrmsg()), httpServletResponse);
        }
        else if(retObject.getCode()== ResponseCode.RESOURCE_ID_NOTEXIST){
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, retObject.getErrmsg()), httpServletResponse);
        }
        else if(retObject.getCode()== ResponseCode.ADDRESS_OUTLIMIT){
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.ADDRESS_OUTLIMIT, retObject.getErrmsg()), httpServletResponse);
        }
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return Common.decorateReturnObject(retObject);
    }


    /**
     * 分页查询用户所有地址
     *
     * @author 24320182203193 何明祥
     * @param page 页数
     * @param pageSize 每页大小
     * @param customerId 用户id
     * @return Object 地址分页查询结果
     * createBy 何明祥 2020/12/6 20:46
     */
    @ApiOperation(value = "查询地址", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="pageNUm", value="页面号", required = false, dataType="Integer", paramType="query"),
            @ApiImplicitParam(name="pageSize", value="页面大小", required = false, dataType="Integer", paramType="query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("/addresses")
    public Object selectAllAdresses(@LoginUser  Long customerId,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        logger.debug("selectAllAdresses: page = "+ page +"  pageSize ="+pageSize);
        logger.debug("getCustomerSelf customerId:" + customerId);
        ReturnObject<PageInfo<VoObject>> ret=addressService.selectAllAddresses(customerId,page,pageSize);
        return Common.getPageRetObject(ret);
    }


    /**
     * 用户修改某个地址
     *
     * @author 24320182203193 何明祥
     * @param id 地址id
     * @param addressRecieveVo 地址视图
     * @return Object
     * createBy 何明祥 2020/12/5 21:25
     */
    @ApiOperation(value = "修改地址", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "该地址id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "AddressRecieveVo", name = "addressRecieveVo", value = "地址视图", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/addresses/{id}")
    public Object updateAddress(@Validated @RequestBody AddressRecieveVo addressRecieveVo, BindingResult bindingResult,
                                @PathVariable("id")Long id, @LoginUser Long customerId) {
        logger.debug("update address by addressId:" + id);
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }

        ReturnObject retObject = addressService.updateAddress(customerId, id, addressRecieveVo);
        if (retObject.getData() != null) {
            return Common.getRetObject(retObject);
        }
        else if(retObject.getCode()== ResponseCode.FIELD_NOTVALID){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, retObject.getErrmsg()), httpServletResponse);
        }
        else if(retObject.getCode()== ResponseCode.REGION_OBSOLETE){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.REGION_OBSOLETE, retObject.getErrmsg()), httpServletResponse);
        }
        else if(retObject.getCode()== ResponseCode.RESOURCE_ID_OUTSCOPE){
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, retObject.getErrmsg()), httpServletResponse);
        }
        else {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, retObject.getErrmsg()), httpServletResponse);
        }

    }


    /**
     * 用户设置默认地址
     *
     * @author 24320182203193 何明祥
     * @param id 地址id
     * @return Object
     * createBy 何明祥 2020/12/7 10:02
     */
    @ApiOperation(value="设置默认地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",name="id", value="地址id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @PutMapping("/addresses/{id}/default")
    public Object setAddressDefault(@PathVariable("id") Long id,@LoginUser Long customerId){
        if (logger.isDebugEnabled()) {
            logger.debug("set address default: id = "+ id);
        }
        ReturnObject<Object> ret=addressService.setAddressDefault(id,customerId);
        if(ret.getCode()== ResponseCode.RESOURCE_ID_OUTSCOPE){
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, ret.getErrmsg()), httpServletResponse);
        }
        else if(ret.getCode()== ResponseCode.RESOURCE_ID_NOTEXIST){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ret.getErrmsg()), httpServletResponse);
        }
        return Common.decorateReturnObject(ret);
    }


    /**
     * 买家删除自己的地址
     *
     * @author 24320182203193 何明祥
     * @param id 地址id
     * @return Object
     * createBy 何明祥 2020/12/7 10:02
     */
    @ApiOperation(value="删除地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",name="id", value="地址id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @DeleteMapping("/addresses/{id}")
    public Object deleteAddress(@PathVariable("id") Long id){
        if (logger.isDebugEnabled()) {
            logger.debug("delete address: id = "+ id);
        }
        ReturnObject<Object> ret=addressService.deleteAddress(id);
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



