package cn.edu.xmu.other.model.vo;

import java.time.LocalDateTime;

public class CouponActivityReturnVo {

    Long id;
    String name;
    LocalDateTime beginTime;
    LocalDateTime endTime;

    public CouponActivityReturnVo() {
    }

    @Override
    public String toString() {
        return "CouponActivityReturnVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
