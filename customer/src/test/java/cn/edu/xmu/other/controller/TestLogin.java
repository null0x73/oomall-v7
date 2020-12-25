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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CustomerApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TestLogin {
    @Autowired
    private MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(TestLogin.class);

    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug(token);
        return token;
    }

    @Test
    public void insertTest() throws Exception {
        String token=creatTestToken(2487l,0l,500000);
        //String spuIdJson = JacksonUtil.toJson(2l);
        String responseString=this.mvc.perform(get("/test").header("authorization", token).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        logger.debug(responseString);
        System.out.println("************"+responseString);
        System.out.println(token);
    }


}
