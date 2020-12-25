package cn.edu.xmu.other.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public class TimeSegmentReceiveVo {

    @Nullable
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    LocalDateTime beginTime;

    @Nullable
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    LocalDateTime endTime;

    public TimeSegmentReceiveVo() {
    }

    public TimeSegmentReceiveVo(LocalDateTime beginTime, LocalDateTime endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
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
        return "TimeSegmentCreateReceiveVo{" +
                "beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

}
