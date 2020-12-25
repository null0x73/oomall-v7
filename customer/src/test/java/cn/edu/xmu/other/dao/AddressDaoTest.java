package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.CustomerApplication;
import cn.edu.xmu.other.model.bo.Address;
import cn.edu.xmu.other.model.vo.AddressRecieveVo;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/10 13:31
 */
@SpringBootTest(classes = CustomerApplication.class)   //标识本类是一个SpringBootTest
@Transactional
class AddressDaoTest {

    @Autowired
    AddressDao addressDao=new AddressDao();

    @Test
    void insertAddress1() {
        Address address=new Address();
        address.setCustomerId(193L);
        address.setRegionId(129L);
        address.setDetail("fsdg");
        address.setConsignee("dsaf");
        address.setMobile("fdsghf");
        address.setBeDefault(Boolean.FALSE);
        ReturnObject<Address> ret=addressDao.insertAddress(address);
        assertEquals(0,ret.getCode().getCode());
        assertEquals(129L,ret.getData().getRegionId());
    }

    //该买家已有20条地址簿
    @Test
    void insertAddress2() {
        Address address=new Address();
        address.setCustomerId(123L);
        address.setRegionId(128L);
        address.setDetail("fsdg");
        address.setConsignee("dsaf");
        address.setMobile("fdsghf");
        address.setBeDefault(Boolean.FALSE);
        ReturnObject<Address> ret=addressDao.insertAddress(address);
        assertEquals(601,ret.getCode().getCode());
    }

    //一个有20条地址的买家查看
    @Test
    void selectAllAddresses() {
        ReturnObject<PageInfo<VoObject>> ret=addressDao.selectAllAddresses(123L,3,10);
        assertEquals(0,ret.getCode().getCode());
        assertEquals(20,ret.getData().getList().size());
    }

//    @Test
//    void updateAddress() {
//        AddressRecieveVo addressRecieveVo=new AddressRecieveVo();
//        addressRecieveVo.setConsignee("consignee");
//        addressRecieveVo.setDetail("detail");
//        addressRecieveVo.setRegionId(123L);
//        addressRecieveVo.setMobile("123589554823");
//        ReturnObject<Address> ret=addressDao.updateAddress(15L,addressRecieveVo);
//        assertEquals(0,ret.getCode().getCode());
//    }

//    //无默认地址
//    @Test
//    void setAddressDefault1() {
//        ReturnObject<Address> ret=addressDao.setAddressDefault(15L);
//        assertEquals(0,ret.getCode().getCode());
//    }
//
//    //有默认地址
//    @Test
//    void setAddressDefault2() {
//        ReturnObject<Address> ret=addressDao.setAddressDefault(24L);
//        assertEquals(0,ret.getCode().getCode());
//    }

    @Test
    void deleteAddress() {
        ReturnObject<Address> ret=addressDao.deleteAddress(15L);
        assertEquals(0,ret.getCode().getCode());
    }
}