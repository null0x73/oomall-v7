package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.RandomCaptcha;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.other.mapper.CustomerPoMapper;
import cn.edu.xmu.other.model.bo.Customer;
import cn.edu.xmu.other.model.po.CustomerPo;
import cn.edu.xmu.other.model.po.CustomerPoExample;
import cn.edu.xmu.other.model.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/13 17:37
 */
@Repository
public class CustomerDao {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDao.class);

    @Autowired
    private CustomerPoMapper customerPoMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 获取用户信息
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     * @return
     */
    public ReturnObject<VoObject> getCustomerById(Long id) {
        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(id);
        if (customerPo == null) {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
        return new ReturnObject(new CustomerReturnVo(customerPo));
    }

    /**
     * 买家修改自己信息
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    public ReturnObject<Customer> updateCustomer(Long id, CustomerModifyReceiveVo customerModifyReceiveVo){
        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(id);
        customerPo.setBirthday(customerModifyReceiveVo.getBirthday());
        customerPo.setGender(customerModifyReceiveVo.getGender().getCode().byteValue());
        customerPo.setRealName(customerModifyReceiveVo.getRealName());
        customerPo.setGmtModified(LocalDateTime.now());
        ReturnObject<Customer> retObj = null;
        try{
            int ret = customerPoMapper.updateByPrimaryKeySelective(customerPo);
            if (ret == 0) {
                //修改失败
                logger.debug("updateCustomer: update customer fail : " + customerPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("用户id不存在：" + customerPo.getId()));
            } else {
                //修改成功
                logger.debug("updateCustomer: update customer = " + customerPo.toString());
                retObj = new ReturnObject<>();
            }
        }
        catch (DataAccessException e) {
            // 其他数据库错误
            logger.debug("other sql exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));

        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("其他错误：%s", e.getMessage()));
        }
        return retObj;
    }


    /**
     * 买家修改自己密码
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    public ReturnObject<Object> modifyPassword(CustomerModifyPwdVo modifyPwdVo) {

        // 通过验证码取出id
        if (!redisTemplate.hasKey("cp_" + modifyPwdVo.getCaptcha()))
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        String id = redisTemplate.opsForValue().get("cp_" + modifyPwdVo.getCaptcha()).toString();

        CustomerPo customerpo = null;
        try {
            customerpo = customerPoMapper.selectByPrimaryKey(Long.parseLong(id));
        } catch (Exception e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, e.getMessage());
        }

        // 新密码与原密码相同
        if (AES.decrypt(customerpo.getPassword(), Customer.AESPASS).equals(modifyPwdVo.getNewPassword()))
            return new ReturnObject<>(ResponseCode.PASSWORD_SAME);

        // 加密
        CustomerPo customerPo = new CustomerPo();
        customerPo.setPassword(AES.encrypt(modifyPwdVo.getNewPassword(), Customer.AESPASS));
        // 更新数据库
        try {
            CustomerPo po = null;
            customerPoMapper.updateByPrimaryKeySelective(po);
        } catch (Exception e) {

            e.printStackTrace();
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, e.getMessage());
        }
        return new ReturnObject<>(ResponseCode.OK);
    }

    /**
     * 买家重置自己密码
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    public ReturnObject<Object> resetPassword(CustomerResetPwdVo vo, String ip) {
        // 防止重复请求验证码
        if (redisTemplate.hasKey("ip_" + ip))
            return new ReturnObject<>(ResponseCode.AUTH_USER_FORBIDDEN);
        else {
            // 1 min中内不能重复请求
            redisTemplate.opsForValue().set("ip_" + ip, ip);
            redisTemplate.expire("ip_" + ip, 60 * 1000, TimeUnit.MILLISECONDS);
        }

        // 验证邮箱、手机号
        CustomerPoExample customerPoExample1 = new CustomerPoExample();
        CustomerPoExample.Criteria criteria = customerPoExample1.createCriteria();
        criteria.andMobileEqualTo(AES.encrypt(vo.getMobile(), Customer.AESPASS));
        List<CustomerPo> customerPo1 = null;
        try {
            customerPo1 = customerPoMapper.selectByExample(customerPoExample1);
        } catch (Exception e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, e.getMessage());
        }
        if (customerPo1.isEmpty())
            return new ReturnObject<>(ResponseCode.MOBILE_WRONG);
        else if (!customerPo1.get(0).getEmail().equals(AES.encrypt(vo.getEmail(), Customer.AESPASS)))
            return new ReturnObject<>(ResponseCode.EMAIL_WRONG);

        // 随机生成验证码
        String captcha = RandomCaptcha.getRandomString(6);
        while (redisTemplate.hasKey(captcha))
            captcha = RandomCaptcha.getRandomString(6);

        String id = customerPo1.get(0).getId().toString();
        String key = "cp_" + captcha;
        // key:验证码,value:id存入redis
        redisTemplate.opsForValue().set(key, id);
        // 五分钟后过期
        redisTemplate.expire("cp_" + captcha, 5 * 60 * 1000, TimeUnit.MILLISECONDS);
        return new ReturnObject<>(ResponseCode.OK);
    }


    /**
     * 分页查询所有用户
     * @author 24320182203193 何明祥
     * @return ReturnObject<List> 列表
     * createBy 何明祥 2020/12/6 20:46
     */
    public ReturnObject<PageInfo<VoObject>> getAllCustomers(String userName, String email, String mobile, Integer page, Integer pageSize) {
        CustomerPoExample example = new CustomerPoExample();
        CustomerPoExample.Criteria criteria = example.createCriteria();
        if(userName!=null)criteria.andUserNameEqualTo(userName);
        if(email!=null)criteria.andEmailEqualTo(email);
        if(mobile!=null)criteria.andMobileEqualTo(mobile);
        List<CustomerPo> customerPos = null;
        customerPos = customerPoMapper.selectByExample(example);
        //分页查询
        PageHelper.startPage(page, pageSize);
        logger.debug("page = " + page + "pageSize = " + pageSize);
        List<VoObject> ret = new ArrayList<>(customerPos.size());
        for (CustomerPo po : customerPos) {
            Customer customer = new Customer(po);
            ret.add(customer);
        }
        PageInfo<CustomerPo> customerPoPage = PageInfo.of(customerPos);
        PageInfo<VoObject> customerPage = new PageInfo<>(ret);
        customerPage.setPages(customerPoPage.getPages());
        customerPage.setPageNum(customerPoPage.getPageNum());
        customerPage.setPageSize(customerPoPage.getPageSize());
        customerPage.setTotal(customerPoPage.getTotal());
        return new ReturnObject<>(customerPage);
    }


    /**
     * 改变用户状态
     * @author 24320182203193 何明祥
     * createBy 何明祥 2020/12/6 20:46
     */
    public ReturnObject<Customer> changeCustomerState(Long id, Customer.State state) {
        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(id);
        customerPo.setState(state.getCode().byteValue());

        ReturnObject<Customer> retObj;
        int ret;
        try {
            ret = customerPoMapper.updateByPrimaryKeySelective(customerPo);
            if (ret == 0) {
                logger.info("用户不存在或已被删除：id = " + id);
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("用户 id = " + id + " 的状态修改为 " + state.getDescription());
                retObj = new ReturnObject<>();
            }
        } catch (DataAccessException e) {
            // 数据库错误
            logger.error("数据库错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            logger.error("严重错误：" + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }


    /**
     * 根据用户名获取买家信息
     * @author 24320182203193 何明祥
     * createBy 何明祥 2020/12/6 20:46
     */
    public ReturnObject<Customer> getCustomerByUserName(String userName) {
        CustomerPoExample example = new CustomerPoExample();
        CustomerPoExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(userName);
        List<CustomerPo> customers = null;
        try {
            customers = customerPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getUserByName: ").append(e.getMessage());
            logger.error(message.toString());
        }
        if (null == customers || customers.isEmpty()) {
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        } else {
            Customer user = new Customer(customers.get(0));
            return new ReturnObject<>(user);
        }
    }
}
