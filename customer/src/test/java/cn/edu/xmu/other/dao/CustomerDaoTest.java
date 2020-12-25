package cn.edu.xmu.other.dao;

import cn.edu.xmu.other.CustomerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/16 18:33
 */
@SpringBootTest(classes = CustomerApplication.class)   //标识本类是一个SpringBootTest
@Transactional
class CustomerDaoTest {

    @Test
    void isCustomerNameExist() {
    }

    @Test
    void isEmailExist() {
    }

    @Test
    void isMobileExist() {
    }

    @Test
    void insertCustomer() {
    }

    @Test
    void getCustomerById() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void getAllCustomers() {
    }

    @Test
    void changeCustomerState() {
    }
}