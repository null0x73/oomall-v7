package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.service.FootprintService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 潘登 24320182203249
 * 足迹controller
 */
@Api(value = "足迹服务", tags = "footprint")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class FootprintController {
    private  static  final Logger logger = LoggerFactory.getLogger(FootprintController.class);

    @Autowired
    private FootprintService footprintService;

    @Autowired
    private HttpServletResponse httpServletResponse;


    /**
     * 根据条件查询足迹
     *
     * author 潘登 24320182203249
     * @parm did 店id
     * @param userId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value="查询足迹")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="did", value="店id", required = true, dataType="Long", paramType="path"),
            @ApiImplicitParam(name="userId", value="用户id", required = false, dataType="Long", paramType="query"),
            @ApiImplicitParam(name="beginTime", value="开始时间", required = false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="endTime", value="结束时间", required = false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="pageNUm", value="页面号", required = false, dataType="Integer", paramType="query"),
            @ApiImplicitParam(name="pageSize", value="页面大小", required = false, dataType="Integer", paramType="query")
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @GetMapping("/shops/{did}/footprints")
    @Audit
    
    //TODO: 设置异常情况返回码
    public Object searchFootprint(@PathVariable Long did,
                                  @RequestParam(required = false) Long userId,
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
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("前端传来的时间格式不对：%s", e.getMessage())));
        }
        if(beginTime2!=null&&endTime2!=null)
            if(beginTime2.isAfter(endTime2)) {
                httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.Log_Bigger, "开始时间大于结束时间"));
            }
        page = (page==null) ? 1:page;
        pageSize = (pageSize==null) ? 10:pageSize;
        ReturnObject<PageInfo<VoObject>> ret=footprintService.selectFootprint(did,userId,beginTime2,endTime2,page,pageSize);
        if(ret.getCode().getCode()!=ResponseCode.OK.getCode())
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        if(ret.getCode().getCode()==ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        if(ret.getCode().getCode()==ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        return Common.getPageRetObject(ret);
    }
}
