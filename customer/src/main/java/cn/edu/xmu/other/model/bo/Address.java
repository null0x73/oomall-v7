package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.AddressPo;
import cn.edu.xmu.other.model.vo.AddressReturnVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/5 11:14
 */
@Data
public class Address implements VoObject {

    private Long id;
    private Long customerId;
    private Long regionId;
    private String detail;
    private String consignee;
    private String mobile;
    private Boolean beDefault;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public  Address(){ }


    /**
     * 构造函数
     * @author 何明祥 24320182203193
     * @param addressPo 用PO构造
     * @return Address
     * createBy 何明祥 2020/12/6 20:46
     */
    public Address(AddressPo addressPo) {
        this.id=addressPo.getId();
        this.customerId=addressPo.getCustomerId();
        this.regionId=addressPo.getRegionId();
        this.detail=addressPo.getDetail();
        this.consignee=addressPo.getConsignee();
        this.mobile=addressPo.getMobile();
        int i=addressPo.getBeDefault().intValue();
        this.beDefault=(i==1)?true:false;
        this.gmtCreate=addressPo.getGmtCreate();
        this.gmtModified=addressPo.getGmtModified();
    }


    /**
     * 用bo对象创建更新po对象
     *
     * @author 何明祥 24320182203193
     * @return AddressPo
     * createBy 何明祥 2020/12/6 20:46
     */
    public AddressPo gotAddressPo()
    {
        AddressPo addressPo=new AddressPo();
        addressPo.setId(this.getId());
        addressPo.setCustomerId(this.getCustomerId());
        addressPo.setRegionId(this.getRegionId());
        addressPo.setDetail(this.getDetail());
        addressPo.setMobile(this.getMobile());
        Integer i=this.getBeDefault()?1:0;
        addressPo.setBeDefault(i.byteValue());
        addressPo.setGmtCreate(this.getGmtCreate());
        addressPo.setGmtModified(this.getGmtModified());
        return addressPo;
    }




    @Override
    public Object createVo() {
        AddressReturnVo addressReturnVo=new AddressReturnVo();
        addressReturnVo.setId(this.getId());
        addressReturnVo.setCustomerId(this.getCustomerId());
        addressReturnVo.setRegionId(this.getRegionId());
        addressReturnVo.setDetail(this.getDetail());
        addressReturnVo.setConsignee(this.getConsignee());
        addressReturnVo.setMobile(this.getMobile());
        addressReturnVo.setBeDefault(this.getBeDefault());
        addressReturnVo.setGmtCreate(this.getGmtCreate());
        return addressReturnVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
