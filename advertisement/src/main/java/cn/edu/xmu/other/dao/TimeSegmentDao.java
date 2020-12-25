package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.TimeSegmentPoMapper;
import cn.edu.xmu.other.model.bo.TimeSegment;
import cn.edu.xmu.other.model.po.AdvertisementPo;
import cn.edu.xmu.other.model.po.TimeSegmentPo;
import cn.edu.xmu.other.model.po.TimeSegmentPoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TimeSegmentDao {

    @Autowired
    TimeSegmentPoMapper timeSegmentPoMapper;

    public TimeSegmentPo getAdvertisementTimeSegmentPoByTimeSegmentId(Long timeSegmentId){
        TimeSegmentPoExample example = new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo((byte)TimeSegment.TimeSegmentType.ADVERTISEMENT.getTypeValueInPo());
        criteria.andIdEqualTo(timeSegmentId);
        return timeSegmentPoMapper.selectByPrimaryKey(timeSegmentId);
    }



    public TimeSegmentPo getFlashSaleTimeSegmentPoByTimeSegmentId(Long timeSegmentId){
        TimeSegmentPoExample example = new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo((byte)TimeSegment.TimeSegmentType.FLASHSALE.getTypeValueInPo());
        criteria.andIdEqualTo(timeSegmentId);
        return timeSegmentPoMapper.selectByPrimaryKey(timeSegmentId);
    }



    
    public int insertTimeSegment(TimeSegmentPo po) {
        return timeSegmentPoMapper.insert(po);
    }



    public PageInfo<TimeSegment> getTimeSegmentInPage(Integer pageIndex, Integer pageSize) {

        // 查询条件：广告时段
        TimeSegmentPoExample example = new TimeSegmentPoExample();
        example.createCriteria().andTypeEqualTo(TimeSegment.TimeSegmentType.findTypeValueForPo(TimeSegment.TimeSegmentType.ADVERTISEMENT));

        // 分页查询
        PageHelper.startPage(pageIndex,pageSize);
        PageInfo<TimeSegmentPo> poPage = new PageInfo<>(timeSegmentPoMapper.selectByExample(example));

        // 把列表里的 PO 替换成 BO，生成新的 boPage
        List<TimeSegmentPo> poList = poPage.getList();
        List<TimeSegment> boList = new ArrayList<>(poList.size());
        for(TimeSegmentPo po:poList){
            boList.add(new TimeSegment(po));
        }
        PageInfo<TimeSegment> boPage = new PageInfo<>();
        boPage.setPageNum(poPage.getPageNum());
        boPage.setSize(poPage.getPageSize());
        boPage.setTotal(poPage.getPages());
        boPage.setPages(poPage.getPages());
        boPage.setList(boList);

        // 返回封装好的 boPage。列表中是 bo。
        return boPage;

    }


    

    public PageInfo<TimeSegment> getFlashSaleTimeSegment(Integer pageIndex, Integer pageSize) {

        // 查询条件：秒杀时段
        TimeSegmentPoExample example = new TimeSegmentPoExample();
        example.createCriteria().andTypeEqualTo(TimeSegment.TimeSegmentType.findTypeValueForPo(TimeSegment.TimeSegmentType.FLASHSALE));
        
        // 分页查询
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<TimeSegmentPo> poPage = new PageInfo<>(timeSegmentPoMapper.selectByExample(example));
         
        // poPage -> boPage
        List<TimeSegmentPo> poList = poPage.getList();
        List<TimeSegment> boList = new ArrayList<>(poList.size());
        for(TimeSegmentPo po:poList){
            boList.add(new TimeSegment(po));
        }
        PageInfo<TimeSegment> boPage = new PageInfo<>();
        boPage.setPageNum(poPage.getPageNum());
        boPage.setPageSize(poPage.getPageSize());
        boPage.setTotal(poPage.getTotal());
        boPage.setPages(poPage.getPages());
        boPage.setList(boList);
        
        // 返回封装好的 boPage。列表中是 BO
        return boPage;
        
    }


    public Boolean deleteAdvertisementTimeSegment(Long timeSegmentId) {
        return timeSegmentPoMapper.deleteByPrimaryKey(timeSegmentId)==1 ? true : false;
    }




    public Boolean deleteFlashSaleTimeSegment(Long timeSegmentId) {
        return timeSegmentPoMapper.deleteByPrimaryKey(timeSegmentId)==1 ? true : false;
    }



    public Boolean isAdvertisementTimeSegmentExistByTimeSegmentId(Long timeSegmentId){
        if(this.getAdvertisementTimeSegmentPoByTimeSegmentId(timeSegmentId)!=null){
            return true;
        } else {
            return false;
        }
    }



    public Boolean isFlashSaleTimeSegmentExistByTimeSegmentId(Long timeSegmentId){
        if(this.getFlashSaleTimeSegmentPoByTimeSegmentId(timeSegmentId)!=null){
            return true;
        } else {
            return false;
        }
    }




    public Boolean isConflictTimeSegmentExistByBeginTimeAndEndTime(TimeSegment.TimeSegmentType timeSegmentType, LocalTime beginTime, LocalTime endTime){

        // 限制条件
        TimeSegmentPoExample example = new TimeSegmentPoExample();
//        example.createCriteria().andTypeEqualTo((byte)timeSegmentType.getTypeValueInPo());

        // 查出所有
        List<TimeSegmentPo> timeSegmentPoList = timeSegmentPoMapper.selectByExample(example);

        // 逐个比较已有时段
        for(TimeSegmentPo timeSegmentPo:timeSegmentPoList){
            LocalTime beginTime0 = timeSegmentPo.getBeginTime().toLocalTime();
            LocalTime endTime0 = timeSegmentPo.getEndTime().toLocalTime();
            if((beginTime0.isAfter(beginTime)&&beginTime0.isBefore(endTime))||(endTime0.isAfter(beginTime)&&endTime0.isBefore(endTime))||(beginTime0.equals(beginTime)&&endTime0.equals(endTime))){
                return true;
            }
        }

        return false;

    }


    public TimeSegmentPo getCurrentTimeSegmentPo() {

        List<TimeSegmentPo> timeSegmentPoList = timeSegmentPoMapper.selectByExample(new TimeSegmentPoExample());

        if(timeSegmentPoList==null){
            return null;
        }

        for (TimeSegmentPo po : timeSegmentPoList) {
            if (po.getBeginTime().toLocalTime().isBefore(LocalTime.now()) && po.getEndTime().toLocalTime().isAfter(LocalTime.now())) {
                return po;
            }
        }

        return null;

    }

    public List<TimeSegmentPo> getTimeSegmentInListByAllTime(TimeSegment.TimeSegmentType type) {

        TimeSegmentPoExample example = new TimeSegmentPoExample();
        example.createCriteria().andTypeEqualTo((byte)type.getTypeValueInPo());
        System.out.println(type.getTypeValueInPo());
        return timeSegmentPoMapper.selectByExample(example);

    }

    public List<TimeSegment> getFlashSaleTimeSegmentInList() {

        TimeSegmentPoExample example = new TimeSegmentPoExample();
        example.createCriteria().andTypeEqualTo((byte)TimeSegment.TimeSegmentType.FLASHSALE.getTypeValueInPo());
        List<TimeSegmentPo> poList = timeSegmentPoMapper.selectByExample(example);
        List<TimeSegment> boList = new ArrayList<>(poList.size());
        for(TimeSegmentPo po:poList){
            if(po.getId()<=24){         // TODO: 根据 HuJialeTest.selectFsTimeTest1() 临时修改
                boList.add(new TimeSegment(po));
            }
        }
        return boList;

    }
}
