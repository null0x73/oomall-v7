package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.TimeSegmentPo;
import cn.edu.xmu.other.model.vo.TimeSegmentReceiveVo;
import cn.edu.xmu.other.model.vo.TimeSegmentReturnVo;

import java.time.LocalDateTime;

public class TimeSegment implements VoObject {

    private Long id;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private TimeSegmentType type;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;



    public TimeSegment(TimeSegmentPo po){
        id = po.getId();
        beginTime = po.getBeginTime();
        endTime = po.getEndTime();
        gmtCreated = po.getGmtCreate();
        gmtModified = po.getGmtModified();
        type = TimeSegmentType.parseTypeEnumFromPoValue(po);
    }



    public TimeSegment(TimeSegmentReceiveVo vo, TimeSegmentType type){
        beginTime = vo.getBeginTime();
        endTime = vo.getEndTime();
        this.type = type;
    }



    public TimeSegment() {
    }



    public TimeSegmentPo createPo(){
        TimeSegmentPo po = new TimeSegmentPo();
        po.setId(id);
        po.setBeginTime(beginTime);
        po.setEndTime(endTime);
        po.setGmtCreate(gmtCreated);
        po.setGmtModified(gmtModified);
        po.setType(TimeSegmentType.findTypeValueForPo(type));
        return po;
    }



    public TimeSegmentReturnVo createReturnVo(){
        TimeSegmentReturnVo vo = new TimeSegmentReturnVo();
        vo.setId(id);
        vo.setBeginTime(beginTime);
        vo.setEndTime(endTime);
        vo.setGmtCreated(gmtCreated);
        vo.setGmtModified(gmtModified);
        return vo;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public TimeSegmentType getType() {
        return type;
    }

    public void setType(TimeSegmentType type) {
        this.type = type;
    }

    public LocalDateTime getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(LocalDateTime gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "TimeSegment{" +
                "id=" + id +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", type=" + type +
                ", gmtCreated=" + gmtCreated +
                ", gmtModified=" + gmtModified +
                '}';
    }



    @Override
    public Object createVo() {
        return this.createReturnVo();
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public enum TimeSegmentType {

        ADVERTISEMENT(0),
        FLASHSALE(1);

        private int typeValueInPo;

        TimeSegmentType(int typeValueInPo) {
            this.typeValueInPo = typeValueInPo;
        }

        public static TimeSegmentType parseTypeEnumFromPoValue(TimeSegmentPo po){
            if(po.getType()==(byte)ADVERTISEMENT.typeValueInPo){
                return TimeSegmentType.ADVERTISEMENT;
            } else if(po.getType()==(byte)FLASHSALE.typeValueInPo){
                return TimeSegmentType.FLASHSALE;
            } else {
                return null;
            }
        }

        public static Byte findTypeValueForPo(TimeSegmentType type) {
            return (byte)type.typeValueInPo;
        }

        public int getTypeValueInPo() {
            return typeValueInPo;
        }
    }

}
