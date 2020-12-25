package cn.edu.xmu.other.model.vo;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AdvertisementCreateReceiveVo {

    @NotNull
    String content;

    String weight;

    @NotNull
    String link;

    @NotNull
    Boolean repeat;

//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    String beginDate;

//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    String endDate;

    public AdvertisementCreateReceiveVo() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "AdvertisementCreateReceiveVo{" +
                "content='" + content + '\'' +
                ", weight=" + weight +
                ", link='" + link + '\'' +
                ", repeat=" + repeat +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                '}';
    }
}
