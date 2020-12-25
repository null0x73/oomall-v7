package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.model.bo.Favorite;
import cn.edu.xmu.other.service.FavoriteService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/6 23:33
 */
@Api(value = "商品收藏服务", tags = "favorite")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class FavoriteController {

    private  static  final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 分页查询用户所有收藏
     *
     * @author 24320182203193 何明祥
     * @param page 页数
     * @param pageSize 每页大小
     * @return Object 收藏分页查询结果
     * createBy 何明祥 2020/12/1 10:02
     */
    @ApiOperation(value = "查询收藏", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="pageNUm", value="页面号", required = false, dataType="Integer", paramType="query"),
            @ApiImplicitParam(name="pageSize", value="页面大小", required = false, dataType="Integer", paramType="query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("/favorites")
    public Object selectAllFavorites(@LoginUser Long customerId,
                                     @RequestParam(required = false, defaultValue = "1") Integer page,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        logger.debug("selectAllFavorites: page = "+ page +"  pageSize ="+pageSize);
        logger.debug("getCustomerSelf customerId:" + customerId);
        ReturnObject<PageInfo<VoObject>> ret=favoriteService.selectAllFavorites(customerId,page,pageSize);
        return Common.getPageRetObject(ret);
    }

    /**
     * 新增一个收藏
     *
     * @author 24320182203193 何明祥
     * @param customerId 当前用户id
     * @param skuId 商品SKU
     * @return Object
     * createBy 何明祥 2020/12/1 10:02
     */
    @ApiOperation(value="增加收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name="skuId", value="商品skuId", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @PostMapping("/favorites/goods/{skuId}")
    public Object insertFavorite(@LoginUser Long customerId,@PathVariable("skuId") Long skuId){
        Favorite favorite=new Favorite(customerId,skuId);
        ReturnObject<VoObject> ret=favoriteService.insertFavorite(favorite);
        if(ret.getCode().getCode()!=504){
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(ret);
        }
        else {
            System.out.println("Controller:"+ret.getData());
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,ret.getErrmsg()),httpServletResponse);
        }

    }

    /**
     * 删除一个收藏
     *
     * @author 24320182203193 何明祥
     * @param id 收藏商品id
     * @return Object
     * createBy 何明祥 2020/12/1 10:02
     */
    @ApiOperation(value="删除收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",name="id", value="收藏商品id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @DeleteMapping("/favorites/{id}")
    public Object deleteFavorite(@PathVariable("id") Long id,@LoginUser Long customerId){
        if (logger.isDebugEnabled()) {
            logger.debug("deleteFavorite: id = "+ id);
        }
        ReturnObject<VoObject> ret=favoriteService.deleteFavorite(id,customerId);
        if(ret.getCode()== ResponseCode.RESOURCE_ID_NOTEXIST){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ret.getErrmsg()), httpServletResponse);
        }
        if(ret.getCode()== ResponseCode.RESOURCE_ID_OUTSCOPE) {

            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, ret.getErrmsg()), httpServletResponse);
        }
        return Common.getRetObject(ret);
    }


}
