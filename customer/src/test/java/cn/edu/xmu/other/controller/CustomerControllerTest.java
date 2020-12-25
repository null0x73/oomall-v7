package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.other.CustomerApplication;
import cn.edu.xmu.other.mapper.CustomerPoMapper;
import cn.edu.xmu.other.model.bo.Customer;
import cn.edu.xmu.other.model.vo.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/14 11:22
 */
@SpringBootTest(classes = CustomerApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(CustomerControllerTest.class);;

    @Autowired
    private CustomerPoMapper customerPoMapper;

    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;

    private final String createTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug(token);
        return token;
    }

    private String login(String userName, String password) throws Exception{
        LoginVo vo = new LoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);

        String requireJson = JacksonUtil.toJson(vo);
        String response = this.mvc.perform(post("/users/login")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson)).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();
        return  JacksonUtil.parseString(response, "data");
    }

    @Test
    void insertCustomer() throws Exception {
        CustomerRegisterReceiveVo customerRegisterReceiveVo =new CustomerRegisterReceiveVo();

        customerRegisterReceiveVo.setUserName("userNameTest");
        customerRegisterReceiveVo.setPassword("passwordTest123!");
        customerRegisterReceiveVo.setRealName("zhang de fa");
        customerRegisterReceiveVo.setEmail("15629456@qq.com");
        customerRegisterReceiveVo.setMobile("18569534526");
        customerRegisterReceiveVo.setGender(Customer.Gender.MALE.getCode());
        customerRegisterReceiveVo.setBirthday(LocalDate.now());

        String customerJson= JacksonUtil.toJson(customerRegisterReceiveVo);

        String responseString=this.mvc.perform(post("/users").contentType("application/json;charset=UTF-8").content(customerJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        logger.debug(responseString);
    }

    @Test
    void insertCustomer731() throws Exception {
        CustomerRegisterReceiveVo customerRegisterReceiveVo =new CustomerRegisterReceiveVo();
        customerRegisterReceiveVo.setUserName("8906373389");
        customerRegisterReceiveVo.setPassword("passwordTest123!");
        customerRegisterReceiveVo.setRealName("zhang de fa");
        customerRegisterReceiveVo.setEmail("15629456@qq.com");
        customerRegisterReceiveVo.setMobile("18569534526");
        customerRegisterReceiveVo.setGender(Customer.Gender.MALE.getCode());
        customerRegisterReceiveVo.setBirthday(LocalDate.now());
        String customerJson= JacksonUtil.toJson(customerRegisterReceiveVo);
        String responseString=this.mvc.perform(post("/users").contentType("application/json;charset=UTF-8").content(customerJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":731,\"errmsg\":\"用户名已被注册\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void insertCustomer732() throws Exception {
        CustomerRegisterReceiveVo customerRegisterReceiveVo =new CustomerRegisterReceiveVo();
        customerRegisterReceiveVo.setUserName("userNameTest");
        customerRegisterReceiveVo.setPassword("passwordTest123!");
        customerRegisterReceiveVo.setRealName("zhang de fa");
        customerRegisterReceiveVo.setEmail("2561023710@qq.com");
        customerRegisterReceiveVo.setMobile("18569534526");
        customerRegisterReceiveVo.setGender(Customer.Gender.MALE.getCode());
        customerRegisterReceiveVo.setBirthday(LocalDate.now());
        String customerJson= JacksonUtil.toJson(customerRegisterReceiveVo);
        String responseString=this.mvc.perform(post("/users").contentType("application/json;charset=UTF-8").content(customerJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":732,\"errmsg\":\"邮箱已被注册\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void insertCustomer733() throws Exception {
        CustomerRegisterReceiveVo customerRegisterReceiveVo =new CustomerRegisterReceiveVo();
        customerRegisterReceiveVo.setUserName("userNameTest");
        customerRegisterReceiveVo.setPassword("passwordTest123!");
        customerRegisterReceiveVo.setRealName("zhang de fa");
        customerRegisterReceiveVo.setEmail("15629456@qq.com");
        customerRegisterReceiveVo.setMobile("13959288888");
        customerRegisterReceiveVo.setGender(Customer.Gender.MALE.getCode());
        customerRegisterReceiveVo.setBirthday(LocalDate.now());
        String customerJson= JacksonUtil.toJson(customerRegisterReceiveVo);
        String responseString=this.mvc.perform(post("/users").contentType("application/json;charset=UTF-8").content(customerJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":733,\"errmsg\":\"电话已被注册\"}",responseString);
        logger.debug(responseString);
    }


    @Test
    void getCustomerSelf() throws Exception {
        String token=createTestToken(300L,0L,100);
        String responseString=this.mvc.perform(get("/users").contentType("application/json;charset=UTF-8").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();
        logger.debug(responseString);
    }


    @Test
    void updateCustomerSelf() throws Exception {
        String token=createTestToken(500L,0L,100);
        CustomerModifyReceiveVo customerModifyReceiveVo =new CustomerModifyReceiveVo();
        customerModifyReceiveVo.setGender(Customer.Gender.MALE);
        customerModifyReceiveVo.setBirthday(LocalDate.now());
        customerModifyReceiveVo.setRealName("Jelly beached");
        String customerJson= JacksonUtil.toJson(customerModifyReceiveVo);
        String responseString=this.mvc.perform(put("/users").header("authorization", token).contentType("application/json;charset=UTF-8").content(customerJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void getAllCustomers() throws Exception {
        String token=createTestToken(500L,0L,100);
        String responseString=this.mvc.perform(get("/users/all?mobile=13959288888").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        logger.debug(responseString);
    }

    @Test
    void getCustomerById() throws Exception {
        String token=createTestToken(490L,0L,100);
        String responseString=this.mvc.perform(get("/users/500").contentType("application/json;charset=UTF-8").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //assertEquals("{\"errno\":0,\"data\":{\"id\":500,\"userName\":\"98468692785\",\"name\":null,\"mobile\":\"13982282388\",\"email\":\"2561023710@qq.com\",\"gender\":\"MALE\",\"birthday\":\"2000-12-06T16:00:00.000+00:00\",\"state\":\"NORMAL\",\"gmtCreate\":\"2020-12-07T13:47:15.000+00:00\",\"gmtModified\":\"2020-12-07T13:47:15.000+00:00\"},\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void forbidCustomer() throws Exception {
        String token=createTestToken(490L,0L,100);
        String responseString=this.mvc.perform(put("/shops/123/users/300/ban").contentType("application/json;charset=UTF-8").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void releaseCustomer() throws Exception {
        String token=createTestToken(490L,0L,100);
        String responseString=this.mvc.perform(put("/shops/123/users/300/release").contentType("application/json;charset=UTF-8").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }


//    @Test
//    public void resetCustomerPwd() throws Exception{
//
//        //插入一条用于测试的记录(自己的真实邮箱)
//        CustomerPo customerPo = new CustomerPo();
//        customerPo.setEmail(AES.encrypt("925882085@qq.com",Customer.AESPASS));
//        customerPo.setMobile(AES.encrypt("13511335577", Customer.AESPASS));
//        customerPo.setUserName("test");
//        customerPo.setPassword(AES.encrypt("123456",Customer.AESPASS));
//        customerPo.setGmtCreate(LocalDateTime.now());
//        customerPoMapper.insertSelective(customerPo);
//
//        //发reset请求
//        CustomerResetPwdVo resetPwdVo = new  CustomerResetPwdVo();
//        resetPwdVo.setEmail("925882085@qq.com");
//        resetPwdVo.setMobile("13511335577");
//        String Json = JacksonUtil.toJson(resetPwdVo);
//
//        String responseString = this.mvc.perform(put("/users/password/reset").contentType("application/json;charset=UTF-8").content(Json))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        String expectedResponse = "{\"errno\": 0, \"errmsg\": \"成功\"}";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
//
//        //删除用于测试的数据库记录
//        CustomerPoExample customerPoExample1 = new CustomerPoExample();
//        CustomerPoExample.Criteria criteria = customerPoExample1.createCriteria();
//        criteria.andMobileEqualTo(AES.encrypt("13511335577",Customer.AESPASS));
//        List<CustomerPo> customerPo1 = customerPoMapper.selectByExample(customerPoExample1);
//        customerPoMapper.deleteByPrimaryKey(customerPo1.get(0).getId());
//    }
//
//
//    /**
//     * 修改密码：不能与旧密码相同
//     */
//    @Test
//    public void modifyPassword() throws Exception{
//
//        //获取一条记录
//        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(1L);
//
//        //向redis插入一条记录
//        redisTemplate.opsForValue().set("cp_666666","1");
//        redisTemplate.expire("cp_666666", 60*1000, TimeUnit.MILLISECONDS);
//
//        //发modify请求
//        CustomerModifyPwdVo vo = new  CustomerModifyPwdVo();
//        vo.setCaptcha("666666");
//        vo.setNewPassword(AES.decrypt(customerPo.getPassword(),Customer.AESPASS));
//        String Json1 = JacksonUtil.toJson(vo);
//
//        String responseString = this.mvc.perform(put("/users/password").contentType("application/json;charset=UTF-8").content(Json1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        String expectedResponse1 = "{\"errno\": 741, \"errmsg\": \"不能与旧密码相同\"}";
//        JSONAssert.assertEquals(expectedResponse1, responseString, true);
//
//    }


}