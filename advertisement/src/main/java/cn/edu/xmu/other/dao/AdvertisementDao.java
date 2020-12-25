package cn.edu.xmu.other.dao;

import cn.edu.xmu.other.mapper.AdvertisementPoMapper;
import cn.edu.xmu.other.model.po.AdvertisementPo;
import cn.edu.xmu.other.model.po.AdvertisementPoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdvertisementDao {

    @Autowired
    AdvertisementPoMapper advertisementPoMapper;


    public int insertAdvertisementPo(AdvertisementPo po){

        return advertisementPoMapper.insert(po);

    }



    public AdvertisementPo getAdvertisementPoByAdvertisementId(Long advertisementId){

        return advertisementPoMapper.selectByPrimaryKey(advertisementId);

    }



    public PageInfo<AdvertisementPo> getAdvertisementPoInPageByTimeSegmentId(Long timeSegmentId, Integer pageIndex, Integer pageSize) {

        // 设置查询条件，按照要求的 timeSegmentId 进行查询
        AdvertisementPoExample example = new AdvertisementPoExample();
        example.createCriteria().andSegIdEqualTo(timeSegmentId);

        // 分页查询
        if(pageIndex!=null&&pageSize!=null){
            PageHelper.startPage(pageIndex,pageSize);
        } else {
            PageHelper.startPage(1,Integer.MAX_VALUE);
        }

        List<AdvertisementPo> poList = advertisementPoMapper.selectByExample(example);
        PageInfo<AdvertisementPo> poPage = new PageInfo<>(poList);
        System.out.println(poList);
        // 返回结果
        return poPage;

    }




    public List<AdvertisementPo> getAdvertisementPoInListByTimeSegmentId(Long timeSegmentId) {

        // 设置查询条件，按照要求的 timeSegmentId 进行查询
        AdvertisementPoExample example = new AdvertisementPoExample();
        example.createCriteria().andSegIdEqualTo(timeSegmentId);

        List<AdvertisementPo> poList = advertisementPoMapper.selectByExample(example);

        return poList;

    }







    public int updateAdvertisementPoSelectively(AdvertisementPo updatePo) {

        return advertisementPoMapper.updateByPrimaryKeySelective(updatePo);

    }





    public int updateAdvertisementPoByExampleSelective(AdvertisementPo updatePo, AdvertisementPoExample example) {

        return advertisementPoMapper.updateByExampleSelective(updatePo, example);

    }




    public int deleteAdvertisementPoByAdvertisementId(Long advertisementId) {

        return advertisementPoMapper.deleteByPrimaryKey(advertisementId);

    }




    public boolean isAdvertisementPoExistByAdvertisementId(Long advertisementId){

        return advertisementPoMapper.selectByPrimaryKey(advertisementId)!=null;

    }




    public boolean isAdvertisementPoExistByBeDefaultEqualsTrue(){

        AdvertisementPoExample example = new AdvertisementPoExample();
        example.createCriteria().andBeDefaultEqualTo((byte)1);

        if(advertisementPoMapper.selectByExample(example).size()!=0){
            return true;
        } else {
            return false;
        }

    }


    public int deleteAdvertisementPoByExample(AdvertisementPoExample example) {
        return advertisementPoMapper.deleteByExample(example);
    }

    public int updateAdvertisementForTimeSegmentId(Long advertisementId, Long timeSegmentId) {
        AdvertisementPo updatePo = new AdvertisementPo();
        updatePo.setId(advertisementId);
        updatePo.setSegId(timeSegmentId);
        return advertisementPoMapper.updateByPrimaryKeySelective(updatePo);
    }

    public void setCurrentDefaultAdvertisementToNotDefault() {
        AdvertisementPoExample example = new AdvertisementPoExample();
        example.createCriteria().andBeDefaultEqualTo((byte)1);
        AdvertisementPo po = advertisementPoMapper.selectByExample(example).get(0);
        po.setBeDefault(false);
        System.out.println("当前默认广告"+po.getId()+"被取消默认");
        advertisementPoMapper.updateByPrimaryKeySelective(po);
    }
}
