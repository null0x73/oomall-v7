package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.other.CustomerApplication;
import cn.edu.xmu.other.model.vo.AddressRecieveVo;
import cn.edu.xmu.other.model.vo.RegionRecieveVo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/10 17:08
 */
@SpringBootTest(classes = CustomerApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AddressControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(AddressControllerTest.class);;


    private final String createTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug(token);
        return token;
    }

    @Test
    void getAncestor() throws Exception {
        String token=createTestToken(2L,0L,100);
        String responseString=this.mvc.perform(get("/region/123/ancestor").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"data\":{\"id\":218,\"pid\":null,\"name\":\"test218\",\"postalCode\":541236,\"state\":\"VALID\",\"gmtCreate\":\"2020-12-16T20:19:12\",\"gmtModified\":\"2020-12-16T20:19:15\"},\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void insertSubregion() throws Exception {
        String token=createTestToken(2L,0L,100);
        RegionRecieveVo regionRecieveVo =new RegionRecieveVo();
        regionRecieveVo.setName("regionTest");
        regionRecieveVo.setPostalCode(128525L);
        String regionJson= JacksonUtil.toJson(regionRecieveVo);
        String responseString=this.mvc.perform(post("/shops/123/regions/129/subregions").header("authorization", token).contentType("application/json;charset=UTF-8").content(regionJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();
        logger.debug(responseString);
    }

    //地区已废弃
    @Test
    void insertSubregion602() throws Exception {
        String token=createTestToken(2L,0L,100);
        RegionRecieveVo regionRecieveVo =new RegionRecieveVo();
        regionRecieveVo.setName("regionTest");
        regionRecieveVo.setPostalCode(128525L);
        String regionJson= JacksonUtil.toJson(regionRecieveVo);
        String responseString=this.mvc.perform(post("/shops/123/regions/158/subregions").header("authorization", token).contentType("application/json;charset=UTF-8").content(regionJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":602,\"errmsg\":\"新增失败：regionTest\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void updateRegion() throws Exception {
        String token=createTestToken(2L,0L,100);
        RegionRecieveVo regionRecieveVo =new RegionRecieveVo();
        regionRecieveVo.setName("regionTest");
        regionRecieveVo.setPostalCode(128525L);
        String regionJson= JacksonUtil.toJson(regionRecieveVo);
        String responseString=this.mvc.perform(put("/shops/123/regions/123").header("authorization", token).contentType("application/json;charset=UTF-8").content(regionJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }


    @Test
    void invalidateRegion() throws Exception {
        String token=createTestToken(2L,0L,100);
        String responseString=this.mvc.perform(delete("/shops/123/regions/81").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void insertAddress() throws Exception {
        String token=createTestToken(2L,0L,100);
        AddressRecieveVo addressRecieveVo =new AddressRecieveVo();
        addressRecieveVo.setRegionId(129L);
        addressRecieveVo.setDetail("addressDetail for test");
        addressRecieveVo.setConsignee("addressConsignee for test");
        addressRecieveVo.setMobile("addressMobile for test");
        String addressJson= JacksonUtil.toJson(addressRecieveVo);
        String responseString=this.mvc.perform(post("/addresses").header("authorization", token).contentType("application/json;charset=UTF-8").content(addressJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        logger.debug(responseString);
    }

    //用户已有20条地址达上限
    @Test
    void insertAddress601() throws Exception {
        String token=createTestToken(123L,0L,100);
        AddressRecieveVo addressRecieveVo =new AddressRecieveVo();
        addressRecieveVo.setRegionId(129L);
        addressRecieveVo.setDetail("addressDetail for test");
        addressRecieveVo.setConsignee("addressConsignee for test");
        addressRecieveVo.setMobile("addressMobile for test");
        String addressJson= JacksonUtil.toJson(addressRecieveVo);
        String responseString=this.mvc.perform(post("/addresses").header("authorization", token).contentType("application/json;charset=UTF-8").content(addressJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":601,\"errmsg\":\"新增失败,customerId：123\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void selectAllAdresses() throws Exception {
        String token=createTestToken(123L,0L,100);
        String responseString=this.mvc.perform(get("/addresses?page=2&pageSize=10").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        logger.debug(responseString);
    }

    @Test
    void updateAddress() throws Exception {
        String token=createTestToken(123L,0L,100);
        AddressRecieveVo addressRecieveVo =new AddressRecieveVo();
        addressRecieveVo.setRegionId(129L);
        addressRecieveVo.setDetail("addressDetail for test");
        addressRecieveVo.setConsignee("addressConsignee for test");
        addressRecieveVo.setMobile("addressMobile for test");
        String addressJson= JacksonUtil.toJson(addressRecieveVo);
        String responseString=this.mvc.perform(put("/addresses/14").header("authorization", token).contentType("application/json;charset=UTF-8").content(addressJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }

    //无默认地址
    @Test
    void setAddressDefault1() throws Exception {
        String token=createTestToken(123L,0L,100);
        String responseString=this.mvc.perform(put("/addresses/19/default").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }

    //有默认地址
    @Test
    void setAddressDefault2() throws Exception {
        String token=createTestToken(193L,0L,100);
        String responseString=this.mvc.perform(put("/addresses/24/default").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void deleteAddress() throws Exception {
        String token=createTestToken(123L,0L,100);
        String responseString=this.mvc.perform(delete("/addresses/21").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }
}