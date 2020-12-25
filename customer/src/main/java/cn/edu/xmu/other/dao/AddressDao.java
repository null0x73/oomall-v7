package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.AddressPoMapper;
import cn.edu.xmu.other.mapper.RegionPoMapper;
import cn.edu.xmu.other.model.bo.Address;
import cn.edu.xmu.other.model.po.AddressPo;
import cn.edu.xmu.other.model.po.AddressPoExample;
import cn.edu.xmu.other.model.po.RegionPo;
import cn.edu.xmu.other.model.vo.AddressRecieveVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/5 11:13
 */
@Repository
public class AddressDao {
    private static final Logger logger = LoggerFactory.getLogger(AddressDao.class);

    @Autowired
    private AddressPoMapper addressPoMapper;

    @Autowired
    private RegionPoMapper regionPoMapper;
    /**
     * 增加一条地址
     *
     * @author 何明祥 24320182203193
     * @param address 地址bo
     * @return ReturnObject<Address> 新增结果
     * createBy 何明祥 2020/12/6 20:46
     */
    public ReturnObject<Address> insertAddress(Address address) {
        //达地址簿上限
        RegionPo regionPo=regionPoMapper.selectByPrimaryKey(address.getRegionId());
        if(regionPo==null) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("新增失败：" ));
        }
        if(regionPo.getState().intValue()==1){
            //地区废弃插入失败
            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE, String.format("新增失败：" + regionPo.getId()));
        }
        AddressPoExample example = new AddressPoExample();
        AddressPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(address.getCustomerId());
        List<AddressPo> addressPos = null;
        addressPos = addressPoMapper.selectByExample(example);
        if(addressPos.size()>=20)
        {
            //插入失败
            logger.debug("insertAddress: insert address fail " + address.toString());
            return new ReturnObject<>(ResponseCode.ADDRESS_OUTLIMIT, String.format("新增失败,customerId：" + address.getCustomerId()));
        }

        //插入
        AddressPo addressPo = address.gotAddressPo();
        ReturnObject<Address> retObj = null;
        int ret = addressPoMapper.insertSelective(addressPo);
        if (ret == 0) {
            //插入失败
            logger.debug("insertAddress: insert address fail " + addressPo.toString());
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败,id：" + addressPo.getId()));
        } else {
            //插入成功
            logger.debug("insertAddress: insert address = " + addressPo.toString());
            address.setId(addressPo.getId());
            retObj = new ReturnObject<>(address);
        }
        return retObj;
    }

    /**
     * 分页查询用户所有地址
     *
     * @author 24320182203193 何明祥
     * @param customerId 用户id
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @return ReturnObject<List> 地址列表
     * createBy 何明祥 2020/12/6 20:46
     */
    public ReturnObject<PageInfo<VoObject>> selectAllAddresses(Long customerId, Integer pageNum, Integer pageSize) {
        AddressPoExample example = new AddressPoExample();
        AddressPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        //分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<AddressPo> addressPos = null;
        addressPos = addressPoMapper.selectByExample(example);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        List<VoObject> ret = new ArrayList<>(addressPos.size());
        for (AddressPo po : addressPos) {
            Address address = new Address(po);
            ret.add(address);
        }
        PageInfo<AddressPo> addressPoPage = PageInfo.of(addressPos);
        PageInfo<VoObject> addressPage = new PageInfo<>(ret);
        addressPage.setPages(addressPoPage.getPages());
        addressPage.setPageNum(addressPoPage.getPageNum());
        addressPage.setPageSize(addressPoPage.getPageSize());
        addressPage.setTotal(addressPoPage.getTotal());
        return new ReturnObject<>(addressPage);
    }

    /**
     * 用户修改某个地址
     *
     * @author 24320182203193 何明祥
     * @param addressRecieveVo 地址Vo
     * @return ReturnObject<Address>
     * createBy 何明祥 2020/12/6 20:46
     */
    public ReturnObject<Address> updateAddress(Long customerId, Long id, AddressRecieveVo addressRecieveVo) {

        AddressPo addressPo = addressPoMapper.selectByPrimaryKey(id);
        RegionPo regionPo=regionPoMapper.selectByPrimaryKey(addressRecieveVo.getRegionId());
        if(regionPo==null) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, String.format("地区id不存在" ));
        }
        if(regionPo.getState().intValue()==1) {
            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE, String.format("地区已废弃" ));
        }
        if(addressPo==null){
            ReturnObject<Address> returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("地址id不存在：" ));
            return  returnObject;
        }
        if(addressPo.getCustomerId()!=customerId){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("不属于当前用户地址：" + addressPo.getId()));
        }

        ReturnObject<Address> retObj = null;
        addressPo.setGmtModified(LocalDateTime.now());
        addressPo.setRegionId(addressRecieveVo.getRegionId());
        addressPo.setMobile(addressRecieveVo.getMobile());
        addressPo.setDetail(addressRecieveVo.getDetail());
        addressPo.setConsignee(addressRecieveVo.getConsignee());
        int ret = addressPoMapper.updateByPrimaryKeySelective(addressPo);
        if (ret == 0) {
            //修改失败
            logger.debug("updateAddress: update address fail : " + addressPo.toString());
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("地址id不存在：" + addressPo.getId()));
        } else {
            //修改成功
            logger.debug("updateAddress: update address = " + addressPo.toString());
            retObj = new ReturnObject<>(ResponseCode.OK);
        }

        return retObj;
    }

    /**
     * 用户设置默认地址
     *
     * @author 24320182203193 何明祥
     * @param id 地址id
     * @return ReturnObject<Address>
     * createBy 何明祥 2020/12/7 10:02
     */
    public ReturnObject<Address> setAddressDefault(Long id,Long customerId){
        ReturnObject<Address> retObj = null;
        Integer def=1,indef=0;
        //如果有，先把已设置的默认地址取消默认地址
        AddressPoExample addressPoExample = new AddressPoExample();
        AddressPoExample.Criteria criteria = addressPoExample.createCriteria();
        criteria.andBeDefaultEqualTo(def.byteValue());
        List<AddressPo> list=addressPoMapper.selectByExample(addressPoExample);
        if(list.size()!=0) {
            AddressPo po=list.get(0);
            po.setBeDefault(indef.byteValue());
            addressPoMapper.updateByPrimaryKeySelective(po);
        }
        //设置新默认地址
        AddressPo addressPo = addressPoMapper.selectByPrimaryKey(id);
        if(addressPo==null){
            ReturnObject<Address> returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("地址id不存在：" ));
            return  returnObject;
        }
        if(addressPo.getCustomerId()!=customerId){

            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("不属于当前用户地址：" + addressPo.getId()));
        }
        addressPo.setBeDefault(def.byteValue());

        int ret = addressPoMapper.updateByPrimaryKeySelective(addressPo);
        if (ret == 0) {

            //修改失败
            logger.debug("updateAddress: update address fail : " + addressPo.toString());
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("地址id不存在：" + addressPo.getId()));
        } else {
            //修改成功
            logger.debug("updateAddress: update address = " + addressPo.toString());
            retObj = new ReturnObject<>();
        }
        return retObj;
    }


    /**
     * 买家删除自己的地址
     *
     * @author 24320182203193 何明祥
     * @param id 地址id
     * @return ReturnObject<Address>
     * createBy 何明祥 2020/12/7 10:02
     */
    public ReturnObject<Address> deleteAddress(Long id){
        ReturnObject<Address> retObj = null;
        int ret = addressPoMapper.deleteByPrimaryKey(id);
        if (ret == 0) {
            logger.info("地址不存在或已被删除,id:"+id);
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("地址"+id+"已被永久删除");
            retObj = new ReturnObject<>();
        }
        return retObj;
    }


}
