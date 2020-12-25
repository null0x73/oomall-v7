package cn.edu.xmu.other.model.vo;

import org.springframework.lang.Nullable;

public class AdvertisementModifyReceiveVo {

    @Nullable
    String content;

    @Nullable
    String weight;

    @Nullable
    Boolean repeat;

//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @Nullable
String beginDate;

//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @Nullable
String endDate;

    @Nullable
    String link;

    public AdvertisementModifyReceiveVo() {
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
        return "AdvertisementModifyReceiveVo{" +
                "content='" + content + '\'' +
                ", weight=" + weight +
                ", repeat=" + repeat +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", link='" + link + '\'' +
                '}';
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

}
