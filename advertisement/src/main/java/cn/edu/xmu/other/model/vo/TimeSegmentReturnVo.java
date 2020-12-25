package cn.edu.xmu.other.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TimeSegmentReturnVo {

    Long id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    LocalDateTime gmtCreated;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    LocalDateTime gmtModified;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    LocalDateTime beginTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    LocalDateTime endTime;

    public TimeSegmentReturnVo() {
    }

    public TimeSegmentReturnVo(Long id, LocalDateTime gmtCreated, LocalDateTime gmtModified, LocalDateTime beginTime, LocalDateTime endTime) {
        this.id = id;
        this.gmtCreated = gmtCreated;
        this.gmtModified = gmtModified;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "TimeSegmentReturnVo{" +
                "id=" + id +
                ", gmtCreated=" + gmtCreated +
                ", gmtModified=" + gmtModified +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

}
