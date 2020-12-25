package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.model.bo.Customer;
import cn.edu.xmu.other.model.vo.*;
import cn.edu.xmu.other.service.CustomerService;
import cn.edu.xmu.other.util.IpUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/13 17:40
 */
@Api(value = "买家服务", tags = "user")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class CustomerController {

    private  static  final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private CustomerService customerService;



    /**
     * 获得买家的所有状态
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @ApiOperation(value="获得买家的所有状态")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CustomerRegisterReceiveVo", name = "customerRegisterReceiveVo", value = "买家视图", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("users/states")
    public Object getAllStates(){
        Customer.State[] states=Customer.State.class.getEnumConstants();
        List<StateVo> stateVos=new ArrayList<StateVo>();
        for(int i=0;i<states.length;i++){
            stateVos.add(new StateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(stateVos).getData());
    }

    /**
     * 用户注册
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @ApiOperation(value="用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CustomerRegisterReceiveVo", name = "customerRegisterReceiveVo", value = "买家视图", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功"),
            @ApiResponse(code= 731,message = "用户名已被注册"),
            @ApiResponse(code= 732,message = "邮箱已被注册"),
            @ApiResponse(code= 733,message = "电话已被注册")
    })
    @PostMapping("/users")
    public Object createNewCustomer(@Validated @RequestBody CustomerRegisterReceiveVo customerRegisterReceiveVo,BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (returnObject != null) {
            return returnObject;
        }
        logger.debug("insert customer:" + customerRegisterReceiveVo.getUserName());
        ReturnObject<Customer> retObject = customerService.createNewCustomer(customerRegisterReceiveVo);
        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.decorateReturnObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }

    /**
     * 买家查看自己信息
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @ApiOperation(value="买家查看自己信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value ="用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @GetMapping("/users")
    public Object getCustomerSelf(@LoginUser Long customerId) {
        logger.debug("getCustomerSelf customerId:" + customerId);
        Object returnObject;
        ReturnObject<VoObject> customer =  customerService.getCustomerById(customerId);
        if(customerId==null){
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return Common.getNullRetObj(new ReturnObject<>(customer .getCode(), customer .getErrmsg()), httpServletResponse);
        }
        logger.debug("finderSelf: customer = " + customer.getData() + " code = " + customer.getCode());
        returnObject = Common.decorateReturnObject(customer);
        return returnObject;
    }

    /**
     * 买家修改自己信息
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @ApiOperation(value="买家修改自己信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value ="用户token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "CustomerModifyReceiveVo", name = "customerModifyReceiveVo", value = "买家简单视图", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @PutMapping("/users")
    public Object updateCustomerSelf(@LoginUser Long customerId, @Validated @RequestBody CustomerModifyReceiveVo customerModifyReceiveVo) {
        logger.debug("updateCustomerSelf customerId:" + customerId);

        ReturnObject retObject = customerService.updateCustomer(customerId, customerModifyReceiveVo);
        if (retObject.getData() != null) {
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }

    /**
     * 买家修改自己密码
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @ApiOperation(value = "用户修改密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CustomerModifyPwdVo", name = "customerModifyPwdVo", value = "邮箱和用户名", required = true) })
    @ApiResponses({
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 741, message = "不能与旧密码相同"),
            @ApiResponse(code = 0, message = "成功"), })
    @PutMapping("/users/password")
    @ResponseBody
    public Object modifyPassword(@RequestBody CustomerModifyPwdVo vo) {
        ReturnObject returnObject = customerService.modifyPassword(vo);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 买家重置自己密码
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @ApiOperation(value = "用户重置密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CustomerResetPwdVo", name = "customerResetPwdVo", value = "邮箱和用户名", required = true) })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 745, message = "与系统预留的邮箱不一致")
    })
    @Audit
    @PutMapping("users/password/reset")
    public Object resetCustomerPwd(@LoginUser CustomerResetPwdVo vo, BindingResult bindingResult) {
        Object bindingObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null!=bindingObject) {
            return bindingObject;
        }
        String ip = IpUtil.getIpAddr(httpServletRequest);

        ReturnObject returnObject = customerService.resetPassword(vo, ip);
        return Common.decorateReturnObject(returnObject);

    }

    /**
     * 平台管理员获取所有用户列表
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @ApiOperation(value="查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value ="用户token", required = true),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "userName",      value ="用户名",    required = false),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "email",        value ="邮箱",  required = false),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "mobile",        value ="电话号码",  required = false),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "page",          value ="页码",      required = false),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "pagesize",      value ="每页数目",  required = false)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @GetMapping("/users/all")
    public Object getAllCustomers(
            @RequestParam(required = false)  String  userName,
            @RequestParam(required = false)  String  email,
            @RequestParam(required = false)  String  mobile,
            @RequestParam(required = false, defaultValue = "1")  Integer page,
            @RequestParam(required = false, defaultValue = "10")  Integer pagesize) {
        Object object = null;

        if(page <= 0 || pagesize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {
            ReturnObject<PageInfo<VoObject>> returnObject = customerService.getAllCustomers(userName,email, mobile, page, pagesize);
            logger.debug("findUserById: getUsers = " + returnObject);
            object = Common.getPageRetObject(returnObject);
        }
        return object;
    }


    /**
     * 用户登录
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @ApiOperation(value="用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "LoginVo", name = "loginVo", value = "登陆视图", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 702, message = "用户被禁止登陆")
    })
    @PostMapping("/users/login")
    public Object login(@Validated @RequestBody LoginVo loginVo, BindingResult bindingResult
            , HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        /* 处理参数校验错误 */
        Object o = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(o != null){
            return o;
        }

        String ip = IpUtil.getIpAddr(httpServletRequest);
        ReturnObject<String> jwt = customerService.login(loginVo.getUserName(), loginVo.getPassword(), ip);

        if(jwt.getData() == null){
            return ResponseUtil.fail(jwt.getCode(), jwt.getErrmsg());
        }else{
            httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
            return ResponseUtil.ok(jwt.getData());
        }
    }

    /**
     * 用户登出
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @ApiOperation(value="用户登出")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value ="用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @GetMapping("users/logout")
    public Object logout(@LoginUser Long customerId) {
        logger.debug("logout: userId = " + customerId);
        ReturnObject<Boolean> returnObject = customerService.logout(customerId);
        if (returnObject.getData() == null||!returnObject.getData()) {
            return ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg());
        } else {
            return ResponseUtil.ok();
        }
    }


    /**
     * 管理员查看任意用户信息
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @ApiOperation(value="管理员查看用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value ="用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",name="id", value="用户id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @GetMapping("/users/{id}")
    public Object getCustomerById(@PathVariable("id") Long id) {
        logger.debug("getCustomer customerId:" + id);
        Object returnObject;
        ReturnObject<VoObject> customer =  customerService.getCustomerById(id);
        logger.debug("find customer = " + customer.getData() + " code = " + customer.getCode());
        returnObject = Common.getRetObject(customer);
        return returnObject;
    }


    /**
     * 管理员封禁某个用户
     * @author 24320182203193 何明祥
     * @param id 用户id
     * @return Object
     * createBy 何明祥 2020/12/6 10:02
     */
    @ApiOperation(value="封禁用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",name="did", value="店id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",name="id", value="用户id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @PutMapping("/shops/{did}/users/{id}/ban")
    public Object forbidCustomer(@PathVariable("id") Long id,@PathVariable("did") Long did){
        if (logger.isDebugEnabled()) {
            logger.debug("invalidateCustomer: id = "+ id);
        }
        ReturnObject<Object> ret=customerService.forbidCustomer(id);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 管理员解禁某个用户
     * @author 24320182203193 何明祥
     * @param id 用户id
     * @return Object
     * createBy 何明祥 2020/12/6 10:02
     */
    @ApiOperation(value="解禁用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",name="did", value="店id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",name="id", value="用户id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code= 0,message = "成功")
    })
    @Audit
    @PutMapping("/shops/{did}/users/{id}/release")
    public Object releaseCustomer(@PathVariable("id") Long id,@PathVariable("did") Long did){
        if (logger.isDebugEnabled()) {
            logger.debug("invalidateCustomer: id = "+ id);
        }
        ReturnObject<Object> ret=customerService.releaseCustomer(id);
        return Common.decorateReturnObject(ret);
    }
}
