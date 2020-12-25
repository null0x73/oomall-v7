package cn.edu.xmu.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.AddressDao;
import cn.edu.xmu.other.model.bo.Address;
import cn.edu.xmu.other.model.vo.AddressRecieveVo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/5 11:15
 */
@Service
public class AddressService {

    private Logger logger = LoggerFactory.getLogger(AddressService.class);
    @Autowired
    private AddressDao addressDao;

    /**
     * 买家新增地址
     *
     * @author 24320182203193 何明祥
     * @param address 地址bo
     * @return ReturnObject<Address> 地址返回视图
     * createBy 何明祥 2020/12/6 20:46
     */
    @Transactional
    public ReturnObject insertAddress(Address address) {
        ReturnObject<Address> retObj = addressDao.insertAddress(address);
        return retObj;
    }

    /**
     * 分页查询用户所有地址
     *
     * @author 24320182203193 何明祥
     * @param customerId 用户id
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @return Object 地址分页查询结果
     * createBy 何明祥 2020/12/6 20:46
     */
    @Transactional
    public ReturnObject<PageInfo<VoObject>> selectAllAddresses(Long customerId , Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = addressDao.selectAllAddresses(customerId , pageNum, pageSize);
        return returnObject;
    }

    /**
     * 用户修改某个地址
     *
     * @author 24320182203193 何明祥
     * @param addressRecieveVo 地址Vo
     * @return ReturnObject<Address> 地址返回视图
     * createBy 何明祥 2020/12/6 20:46
     */
    @Transactional
    public ReturnObject updateAddress(Long customerId, Long id, AddressRecieveVo addressRecieveVo) {
        ReturnObject<Address> retObj = addressDao.updateAddress(customerId, id, addressRecieveVo);
        return retObj;
    }


    /**
     * 用户设置默认地址
     *
     * @author 24320182203193 何明祥
     * @param id 地址id
     * @return ReturnObject<Address> 地址返回视图
     * createBy 何明祥 2020/12/7 10:02
     */
    @Transactional
    public ReturnObject setAddressDefault(Long id,Long customerId) {
        ReturnObject<Address> retObj = addressDao.setAddressDefault(id,customerId);
        return retObj;
    }

    /**
     * 买家删除自己的地址
     *
     * @author 24320182203193 何明祥
     * @param id 地址id
     * @return ReturnObject<Address> 地址返回视图
     * createBy 何明祥 2020/12/7 10:02
     */
    @Transactional
    public ReturnObject deleteAddress(Long id) {
        ReturnObject<Address> retObj = addressDao.deleteAddress(id);
        return retObj;
    }


}
