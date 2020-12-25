package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.CustomerApplication;
import cn.edu.xmu.other.model.bo.Favorite;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/9 10:47
 */
@SpringBootTest(classes = CustomerApplication.class)   //标识本类是一个SpringBootTest
@Transactional
class FavoriteDaoTest {

    @Autowired
    FavoriteDao favoriteDao=new FavoriteDao();

    @Test
    void insertFavorite() {
        Long customerId= new Long(180);
        Long skuId=new Long(594);
        ReturnObject<Object> ret=favoriteDao.insertFavorite(new Favorite(customerId,skuId));
        //System.out.println(ret.getData());
        assertEquals(0,ret.getCode().getCode());
    }

    @Test
    void selectAllFavorites() {
        Long customerId= new Long(123);
        ReturnObject<PageInfo<VoObject>> ret=favoriteDao.selectAllFavorites(customerId,1,10);
        assertEquals(0,ret.getCode().getCode());
        assertEquals(1,ret.getData().getPageNum());
        //System.out.println(ret.getData());
    }
//
//    @Test
//    void deleteFavorite() {
//        Long id1= new Long(3735452);
//        ReturnObject<Object> ret1=favoriteDao.deleteFavorite(id1);
//        assertEquals(0,ret1.getCode().getCode());
//
//        //id不存在
//        Long id2= new Long(8856248);
//        ReturnObject<Object> ret2=favoriteDao.deleteFavorite(id2);
//        assertEquals(504,ret2.getCode().getCode());
//    }


}