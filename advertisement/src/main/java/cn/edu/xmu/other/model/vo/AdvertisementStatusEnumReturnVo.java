package cn.edu.xmu.other.model.vo;


import cn.edu.xmu.other.model.bo.Advertisement;

public class AdvertisementStatusEnumReturnVo {

    Integer code;
    String name;

    public AdvertisementStatusEnumReturnVo() {
    }

    public AdvertisementStatusEnumReturnVo(Advertisement.AdvertisementStatus statusEnum){
        this.code = statusEnum.getCode();
        this.name = statusEnum.getName();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AdvertisementStatusEnumReturnVo{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
