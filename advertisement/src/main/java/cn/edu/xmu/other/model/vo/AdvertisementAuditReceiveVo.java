package cn.edu.xmu.other.model.vo;

public class AdvertisementAuditReceiveVo {

    Boolean conclusion;
    String message;

    public AdvertisementAuditReceiveVo() {
    }

    public Boolean getConclusion() {
        return conclusion;
    }

    public void setConclusion(Boolean conclusion) {
        this.conclusion = conclusion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AdvertisementAuditVo{" +
                "conclusion=" + conclusion +
                ", message='" + message + '\'' +
                '}';
    }
}
