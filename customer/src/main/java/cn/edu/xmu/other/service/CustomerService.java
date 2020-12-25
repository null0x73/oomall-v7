package cn.edu.xmu.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.other.dao.CustomerDao;
import cn.edu.xmu.other.dao.NewCustomerDao;
import cn.edu.xmu.other.model.bo.Customer;
import cn.edu.xmu.other.model.vo.CustomerModifyPwdVo;
import cn.edu.xmu.other.model.vo.CustomerModifyReceiveVo;
import cn.edu.xmu.other.model.vo.CustomerRegisterReceiveVo;
import cn.edu.xmu.other.model.vo.CustomerResetPwdVo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/13 17:39
 */
@Service
public class CustomerService {
    private Logger logger = LoggerFactory.getLogger(CustomerService.class);

    //@Value("${otherservice.login.jwtExpire}")
    private final Integer jwtExpireTime=3600;

    //@Value("${otherservice.user.expiretime}")
    private final long timeout=600;

    //@Value("${privilegeservice.login.multiply}")
    private Boolean canMultiplyLogin=false;

    //@Value("${privilegeservice.lockerExpireTime}")
    private long lockerExpireTime=30L;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private NewCustomerDao newCustomerDao;

    /**
     * 用户注册
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @Transactional
    public ReturnObject createNewCustomer(CustomerRegisterReceiveVo customerRegisterReceiveVo) {
        ReturnObject<Customer> retObj = newCustomerDao.createNewCustomer(customerRegisterReceiveVo);
        return retObj;
    }

    /**
     * 获取用户信息
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     * @return
     */
    @Transactional
    public ReturnObject<VoObject> getCustomerById(Long id){
        ReturnObject<VoObject> retObj=customerDao.getCustomerById(id);
        return retObj;
    }

    /**
     * 用户修改自己信息
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @Transactional
    public ReturnObject<Customer> updateCustomer(Long customerId, CustomerModifyReceiveVo customerModifyReceiveVo){
        ReturnObject<Customer> retObj=customerDao.updateCustomer(customerId, customerModifyReceiveVo);
        return retObj;
    }

    /**
     * 查询用户
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @Transactional
    public ReturnObject<PageInfo<VoObject>> getAllCustomers(String userName, String email, String mobile, Integer page, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = customerDao.getAllCustomers(userName,email,mobile, page, pageSize);
        return returnObject;
    }

    /**
     * 买家修改自己密码
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @Transactional
    public ReturnObject modifyPassword(CustomerModifyPwdVo vo) {
        return customerDao.modifyPassword(vo);
    }


    /**
     * 买家重置自己密码
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @Transactional
    public ReturnObject<Object> resetPassword(CustomerResetPwdVo vo, String ip) {
        return customerDao.resetPassword(vo, ip);
    }


    private void banJwt(String jwt){
        String[] banSetName = {"BanJwt_0", "BanJwt_1"};
        long bannIndex = 0;
        if (!redisTemplate.hasKey("banIndex")){
            redisTemplate.opsForValue().set("banIndex", Long.valueOf(0));
        } else {
            logger.debug("banJwt: banIndex = " +redisTemplate.opsForValue().get("banIndex"));
            bannIndex = Long.parseLong(redisTemplate.opsForValue().get("banIndex").toString());
        }
        logger.debug("banJwt: banIndex = " + bannIndex);
        String currentSetName = banSetName[(int) (bannIndex % banSetName.length)];
        logger.debug("banJwt: currentSetName = " + currentSetName);
        if(!redisTemplate.hasKey(currentSetName)) {
            // 新建
            logger.debug("banJwt: create ban set" + currentSetName);
            redisTemplate.opsForSet().add(currentSetName, jwt);
            redisTemplate.expire(currentSetName,jwtExpireTime * 2,TimeUnit.SECONDS);
        }else{
            //准备向其中添加元素
            if(redisTemplate.getExpire(currentSetName, TimeUnit.SECONDS) > jwtExpireTime) {
                // 有效期还长，直接加入
                logger.debug("banJwt: add to exist ban set" + currentSetName);
                redisTemplate.opsForSet().add(currentSetName, jwt);
            } else {
                // 有效期不够JWT的过期时间，准备用第二集合，让第一个集合自然过期
                // 分步式加锁
                logger.debug("banJwt: switch to next ban set" + currentSetName);
                long newBanIndex = bannIndex;
                while (newBanIndex == bannIndex &&
                        !redisTemplate.opsForValue().setIfAbsent("banIndexLocker","nouse", lockerExpireTime, TimeUnit.SECONDS)){
                    //如果BanIndex没被其他线程改变，且锁获取不到
                    try {
                        Thread.sleep(10);
                        //重新获得新的BanIndex
                        newBanIndex = (Long) redisTemplate.opsForValue().get("banIndex");
                    }catch (InterruptedException e){
                        logger.error("banJwt: 锁等待被打断");
                    }
                    catch (IllegalArgumentException e){

                    }
                }
                if (newBanIndex == bannIndex) {
                    //切换ban set
                    bannIndex = redisTemplate.opsForValue().increment("banIndex");
                }else{
                    //已经被其他线程改变
                    bannIndex = newBanIndex;
                }

                currentSetName = banSetName[(int) (bannIndex % banSetName.length)];
                //启用之前，不管有没有，先删除一下，应该是没有，保险起见
                redisTemplate.delete(currentSetName);
                logger.debug("banJwt: next ban set =" + currentSetName);
                redisTemplate.opsForSet().add(currentSetName, jwt);
                redisTemplate.expire(currentSetName,jwtExpireTime * 2,TimeUnit.SECONDS);
                // 解锁
                redisTemplate.delete("banIndexLocker");
            }
        }
    }

    /**
     * 用户登录
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    @Transactional
    public ReturnObject<String> login(String userName, String password, String ipAddr)
    {
        ReturnObject retObj = customerDao.getCustomerByUserName(userName);
        if (retObj.getCode() != ResponseCode.OK){
            return retObj;
        }
        Customer customer = (Customer) retObj.getData();

        if(customer == null || !password.equals(customer.getPassword())){
            retObj = new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
            return retObj;
        }
        if (customer.getState() != Customer.State.NORMAL){
            retObj = new ReturnObject<>(ResponseCode.AUTH_USER_FORBIDDEN);
            return retObj;
        }

        String key = "up_" + customer.getId();
        if(redisTemplate.hasKey(key) && !canMultiplyLogin){
            Set<String> set = redisTemplate.opsForSet().members(key);
            redisTemplate.delete(key);
        }


        //创建新的token
        JwtHelper jwtHelper = new JwtHelper();
        Random random = new Random();
        Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()+random.nextInt();
        String jwt = jwtHelper.createToken(customer.getId(),-2L, jwtExpireTime);
        redisTemplate.opsForSet().add(key, jwt);
        logger.debug("login: newJwt = "+ jwt);
        retObj = new ReturnObject<>(jwt);

        return retObj;
    }

    /**
     * 用户登出
     * @author 24320182203193 何明祥
     * createBy 何明祥 2020/12/6 10:02
     */
    public ReturnObject<Boolean> logout(Long customerId) {
        return new ReturnObject<>(redisTemplate.delete("up_" + customerId));
    }

    /**
     * 管理员冻结某个用户
     * @author 24320182203193 何明祥
     * createBy 何明祥 2020/12/6 10:02
     */
    @Transactional
    public ReturnObject forbidCustomer(Long id) {
        ReturnObject<Customer> retObj = customerDao.changeCustomerState(id, Customer.State.FORBIDDEN);
        return retObj;
    }
    /**
     * 管理员解冻某个用户
     * @author 24320182203193 何明祥
     * createBy 何明祥 2020/12/6 10:02
     */
    @Transactional
    public ReturnObject releaseCustomer(Long id) {
        ReturnObject<Customer> retObj = customerDao.changeCustomerState(id, Customer.State.NORMAL);
        return retObj;
    }
}
