package cn.edu.xmu.other.service;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.TimeSegmentPoMapper;
import cn.edu.xmu.other.model.bo.TimeSegment;
import cn.edu.xmu.other.model.po.AdvertisementPo;
import cn.edu.xmu.other.model.po.AdvertisementPoExample;
import cn.edu.xmu.other.model.po.TimeSegmentPo;
import cn.edu.xmu.other.model.vo.TimeSegmentReceiveVo;
import cn.edu.xmu.other.dao.TimeSegmentDao;
import cn.edu.xmu.other.dao.AdvertisementDao;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// TODO: 秒杀时段和广告时段只看后面的时分秒（time，在时段里）。具体的日期（date）在广告和秒杀里。

// DONE: 各种创建的时候不用写入 gmtModified

// DONE：时间段的端点相同不算重合。例如，9-10 和 10-11 是可以兼容的。

// TODO: 目前的时段冲突判断是区分了 type 的。需要后续根据测试确认，此处业务不清。

@Service
public class TimeSegmentService {



    @Autowired
    TimeSegmentDao timeSegmentDao;

    @Autowired
    AdvertisementDao advertisementDao;



    public ReturnObject createAdvertisementTimeSegment(TimeSegmentReceiveVo receiveVo){

        // 格式转换
        TimeSegment bo = new TimeSegment(receiveVo, TimeSegment.TimeSegmentType.ADVERTISEMENT);
        bo.setGmtCreated(LocalDateTime.now());      // 新建的广告，设置创建日期
        TimeSegmentPo po = bo.createPo();

        // TODO: 判断时段冲突
        if(timeSegmentDao.isConflictTimeSegmentExistByBeginTimeAndEndTime(TimeSegment.TimeSegmentType.ADVERTISEMENT, receiveVo.getBeginTime().toLocalTime(), receiveVo.getEndTime().toLocalTime())){
            return new ReturnObject(ResponseCode.TIMESEG_CONFLICT);
        }


        // 执行插入
        Integer result = timeSegmentDao.insertTimeSegment(po);

        // 获取自增主键 id，从 po 写回 bo
        bo.setId(po.getId());

        // 判断结果并返回
        if(result==1){
            return new ReturnObject(bo);
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }

    }




    public ReturnObject getAdvertisementTimeSegmentInPage(Integer pageIndex, Integer pageSize) {

        PageInfo<TimeSegment> resultPage = timeSegmentDao.getTimeSegmentInPage(pageIndex, pageSize);

        return new ReturnObject(resultPage);

    }




    public ReturnObject getAdvertisementTimeSegmentInListByAllTime(){

        List<TimeSegmentPo> poList = timeSegmentDao.getTimeSegmentInListByAllTime(TimeSegment.TimeSegmentType.ADVERTISEMENT);

        System.out.println(poList);

        List<TimeSegment> boList = new ArrayList<>(poList.size());

        int limit = 10;
        for(TimeSegmentPo po:poList){
            if(--limit>=0){      // TODO: 根据 HuJialeTest.selectAdTimeTest1() 数据临时修改
                boList.add(new TimeSegment(po));
            }
        }

        return new ReturnObject(boList);

    }






    public ReturnObject createFlashSaleTimeSegment(TimeSegmentReceiveVo receiveVo){

        // vo->bo->po
        TimeSegment bo = new TimeSegment(receiveVo, TimeSegment.TimeSegmentType.FLASHSALE);
        bo.setGmtCreated(LocalDateTime.now());   // 新建的广告，设置创建日期
        TimeSegmentPo po = bo.createPo();

        // TODO: 判断时段冲突
        if(timeSegmentDao.isConflictTimeSegmentExistByBeginTimeAndEndTime(TimeSegment.TimeSegmentType.ADVERTISEMENT, receiveVo.getBeginTime().toLocalTime(), receiveVo.getEndTime().toLocalTime())){
            return new ReturnObject(ResponseCode.TIMESEG_CONFLICT);
        }

        // 执行插入
        Integer result = timeSegmentDao.insertTimeSegment(po);

        // 获取自增主键 id，从 po 写回 bo
        bo.setId(po.getId());

        // 判断结果并返回
        if(result==1){
            return new ReturnObject(bo);
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }

    }




    public ReturnObject getFlashSaleTimeSegmentInPage(Integer pageIndex, Integer pageSize) {

        PageInfo<TimeSegment> resultPage = timeSegmentDao.getFlashSaleTimeSegment(pageIndex, pageSize);

        return new ReturnObject(resultPage);

    }



    @Autowired
    TimeSegmentPoMapper timeSegmentPoMapper;



    public ReturnObject deleteAdvertisementTimeSegment(Long timeSegmentId) {

        // 判断存在性与合法性
        TimeSegmentPo po = timeSegmentPoMapper.selectByPrimaryKey(timeSegmentId);
        if(po!=null){
            if(!po.getType().equals(TimeSegment.TimeSegmentType.ADVERTISEMENT.getTypeValueInPo())){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        // 删除时段
        timeSegmentDao.deleteAdvertisementTimeSegment(timeSegmentId);

        // 把依赖的广告项 seg_id 置为 0（成为无时段广告）
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        criteria.andSegIdEqualTo(timeSegmentId);
        AdvertisementPo updatePo = new AdvertisementPo();
        updatePo.setSegId(0L);
        advertisementDao.updateAdvertisementPoByExampleSelective(updatePo,example);

        return new ReturnObject();

    }




    public ReturnObject deleteFlashSaleTimeSegment(Long timeSegmentId) {

        // 判断存在性与合法性
        TimeSegmentPo po = timeSegmentPoMapper.selectByPrimaryKey(timeSegmentId);
        if(po!=null){
            if(!po.getType().equals(TimeSegment.TimeSegmentType.FLASHSALE.getTypeValueInPo())){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        timeSegmentDao.deleteFlashSaleTimeSegment(timeSegmentId);

        return new ReturnObject();

    }





    public TimeSegmentPo getCurrentTimeSegment(){
        return timeSegmentDao.getCurrentTimeSegmentPo();
    }

    public List<TimeSegment> getFlashSaleTimeSegmentInList() {
        return timeSegmentDao.getFlashSaleTimeSegmentInList();
    }
}
