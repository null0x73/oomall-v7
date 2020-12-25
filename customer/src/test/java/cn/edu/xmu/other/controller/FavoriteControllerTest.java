package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.other.CustomerApplication;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/9 15:03
 */
@SpringBootTest(classes = CustomerApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FavoriteControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(FavoriteControllerTest.class);;


    private final String createTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug(token);
        return token;
    }

    @Test
    void selectAllFavorites() throws Exception {
        String token=createTestToken(2L,0L,100);
        String responseString=this.mvc.perform(get("/favorites?page=1&pageSize=10").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":0,\"data\":{\"total\":3,\"pages\":1,\"pageSize\":3,\"page\":1,\"list\":[{\"id\":3243935,\"customerId\":2,\"goodsSkuId\":639,\"gmtCreate\":\"2020-12-03T21:45:44\",\"gmtModified\":\"2020-12-03T21:45:44\"},{\"id\":3276702,\"customerId\":2,\"goodsSkuId\":479,\"gmtCreate\":\"2020-12-03T21:45:45\",\"gmtModified\":\"2020-12-03T21:45:45\"},{\"id\":3309469,\"customerId\":2,\"goodsSkuId\":564,\"gmtCreate\":\"2020-12-03T21:45:45\",\"gmtModified\":\"2020-12-03T21:45:45\"}]},\"errmsg\":\"成功\"}",responseString);
        logger.debug(responseString);
    }

    @Test
    void insertFavorite() throws Exception {
        String token=createTestToken(2L,0L,100);
        String responseString=this.mvc.perform(post("/favorites/goods/594").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        logger.debug(responseString);
    }

    @Test
    void deleteFavorite1() throws Exception {
        String token = createTestToken(2L, 0L, 100);
        String responseString = this.mvc.perform(delete("/favorites/3735447").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        logger.debug(responseString);
    }

    @Test
    void deleteFavorite2() throws Exception {
        String token = createTestToken(2L, 0L, 100);
        String responseString = this.mvc.perform(delete("/favorites/478").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}",responseString);
        logger.debug(responseString);
    }
}