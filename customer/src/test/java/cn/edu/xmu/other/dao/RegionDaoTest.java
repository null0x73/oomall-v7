package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.CustomerApplication;
import cn.edu.xmu.other.model.bo.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/9 22:15
 */
@SpringBootTest(classes = CustomerApplication.class)   //标识本类是一个SpringBootTest
@Transactional
class RegionDaoTest {

    @Autowired
    RegionDao regionDao=new RegionDao();

//    //'123', '125', 'hh', '123156', '1', '2020-12-09 23:13:41', '2020-12-09 23:13:41'
//    @Test
//    void getAncestor1() {
//        Long id=new Long(123);
//        ReturnObject<Region> ret=regionDao.getAncestor(id);
//        assertEquals(0,ret.getCode().getCode());
//        assertEquals(125,ret.getData().getId());
//    }

//    //无此ID
//    @Test
//    void getAncestor2() {
//        Long id=new Long(1235);
//        ReturnObject<Region> ret=regionDao.getAncestor(id);
//        assertEquals(504,ret.getCode().getCode());
//    }

    @Test
    void insertRegion1() {
        Region region=new Region();
        Long pos=123985L;
        Long pid=128L;
        region.setName("abc");
        region.setPostalCode(pos);
        region.setPid(pid);
        ReturnObject<Region> ret=regionDao.insertRegion(region);
        assertEquals(0,ret.getCode().getCode());
    }

    //父地区已废弃
    @Test
    void insertRegion2() {
        Region region=new Region();
        Long pos=123985L;
        Long pid=129L;
        region.setName("abdcd");
        region.setPostalCode(pos);
        region.setPid(pid);
        ReturnObject<Region> ret=regionDao.insertRegion(region);
        assertEquals(602,ret.getCode().getCode());
    }

//    @Test
//    void updateRegion() {
//        Region region=new Region();
//        Long id=125L;
//        Long pos=172985L;
//        region.setId(id);
//        region.setPostalCode(pos);
//        region.setName("defgs");
//        ReturnObject<Region> ret=regionDao.updateRegion(region);
//        assertEquals(0,ret.getCode().getCode());
//    }

    @Test
    void changeRegionState() {
        Long id=125L;
        ReturnObject<Region> ret=regionDao.changeRegionState(id, Region.State.INVALID);
        assertEquals(0,ret.getCode().getCode());
    }
}