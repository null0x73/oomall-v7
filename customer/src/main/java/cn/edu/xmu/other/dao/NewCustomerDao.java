package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.ooad.util.bloom.BloomFilterHelper;
import cn.edu.xmu.ooad.util.bloom.RedisBloomFilter;
import cn.edu.xmu.other.mapper.CustomerPoMapper;
import cn.edu.xmu.other.model.bo.Customer;
import cn.edu.xmu.other.model.po.CustomerPo;
import cn.edu.xmu.other.model.po.CustomerPoExample;
import cn.edu.xmu.other.model.vo.CustomerRegisterReceiveVo;
import cn.edu.xmu.other.model.vo.CustomerReturnVo;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/17 14:02
 */
@Repository
public class NewCustomerDao implements InitializingBean {

    private  static  final Logger logger = LoggerFactory.getLogger(NewCustomerDao.class);

    @Autowired
    CustomerPoMapper customerPoMapper;
    @Autowired
    RedisTemplate redisTemplate;

    RedisBloomFilter bloomFilter;

    String[] fieldName;
    final String suffixName="BloomFilter";

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 通过该参数选择是否清空布隆过滤器
     */
    private boolean reinitialize=true;


    /**
     * 初始化布隆过滤器
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        BloomFilterHelper bloomFilterHelper=new BloomFilterHelper<>(Funnels.stringFunnel(Charsets.UTF_8),1000,0.02);
        fieldName=new String[]{"email","mobile","userName"};
        bloomFilter=new RedisBloomFilter(redisTemplate,bloomFilterHelper);
        if(reinitialize){
            for(int i=0;i<fieldName.length;i++){
                redisTemplate.delete(fieldName[i]+suffixName);
            }
        }

    }

    /**
     *
     * @param po
     * @return ReturnObject 错误返回对象
     */
    public ReturnObject checkBloomFilter(CustomerPo po){
        if(bloomFilter.includeByBloomFilter("email"+suffixName,po.getEmail())){
            return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
        }
        if(bloomFilter.includeByBloomFilter("mobile"+suffixName,po.getMobile())){
            return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
        }
        if(bloomFilter.includeByBloomFilter("userName"+suffixName,po.getUserName())){
            return new ReturnObject(ResponseCode.USER_NAME_REGISTERED);
        }
        return null;

    }

    /**
     * 由属性名及属性值设置相应布隆过滤器
     * @param name 属性名
     * @param po po对象
     */
    public void setBloomFilterByName(String name,CustomerPo po) {
        try {
            Field field = CustomerPo.class.getDeclaredField(name);
            Method method=po.getClass().getMethod("get"+name.substring(0,1).toUpperCase()+name.substring(1));
            logger.debug("add value "+method.invoke(po)+" to "+field.getName()+suffixName);
            bloomFilter.addByBloomFilter(field.getName()+suffixName,method.invoke(po));
        }
        catch (Exception ex){
            logger.error("Exception happened:"+ex.getMessage());
        }
    }

    /**
     * 检查用户名重复
     * @param customerName 需要检查的用户名
     * @return boolean
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    public boolean isUserNameExist(String customerName){
        logger.debug("is checking customerName in customer table");
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andUserNameEqualTo(customerName);
        List<CustomerPo> customerPos=customerPoMapper.selectByExample(example);
        //if(!customerPos.isEmpty())logger.debug(customerPos.get(0).getEmail()+"-"+AES.decrypt(customerPos.get(0).getMobile(),Customer.AESPASS));
        return !customerPos.isEmpty();
    }

    /**
     * 检查邮箱重复
     * @param email
     * @return boolean
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    public boolean isEmailExist(String email){
        logger.debug("is checking email in customer table");
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andEmailEqualTo(email);
        List<CustomerPo> customerPos=customerPoMapper.selectByExample(example);
        return !customerPos.isEmpty();
    }

    /**
     * 检查电话重复
     * @param mobile 电话号码
     * @return boolean
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    public boolean isMobileExist(String mobile){
        logger.debug("is checking mobile in customer table");
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<CustomerPo> customerPos=customerPoMapper.selectByExample(example);
        return !customerPos.isEmpty();
    }

    /**
     * 用户注册
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/13 17:37
     */
    public ReturnObject createNewCustomer(CustomerRegisterReceiveVo vo){
        if(vo.getEmail()==null) {
            return new ReturnObject<>(ResponseCode.EMAIL_NOTVERIFIED);
        }
        if(vo.getMobile()==null) {
            return new ReturnObject<>(ResponseCode.MOBILE_NOTVERIFIED);
        }
        if(vo.getGender()==null) {
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        }

        CustomerPo customerPo=new CustomerPo();
        ReturnObject returnObject;
        customerPo.setEmail(vo.getEmail());
        customerPo.setMobile(vo.getMobile());
        customerPo.setUserName(vo.getUserName());
        returnObject=checkBloomFilter(customerPo);
        if(returnObject!=null){
            logger.debug("found duplicate in bloomFilter");
            return returnObject;
        }

        if(isEmailExist(customerPo.getEmail())){
            setBloomFilterByName("email",customerPo);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
        }
        if(isMobileExist(customerPo.getMobile())){
            setBloomFilterByName("mobile",customerPo);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
        }
        if(isUserNameExist(customerPo.getUserName())){
            setBloomFilterByName("userName",customerPo);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return new ReturnObject(ResponseCode.USER_NAME_REGISTERED);
        }


        customerPo.setPassword(vo.getPassword());
        customerPo.setRealName(vo.getRealName());
        customerPo.setPoint(0);
        customerPo.setGender(vo.getGender().byteValue());
        LocalDateTime localDateTime = LocalDateTime.now();
        customerPo.setGmtCreate(localDateTime);
        customerPo.setGmtModified(localDateTime);

        customerPo.setBirthday(vo.getBirthday());
        customerPo.setState(Customer.State.NORMAL.getCode().byteValue());
        Integer i=0;
        customerPo.setBeDeleted(i.byteValue());
        try{
            int ret = customerPoMapper.insert(customerPo);
            if(ret!=0) {
                CustomerPoExample example = new CustomerPoExample();
                CustomerPoExample.Criteria criteria = example.createCriteria();
                criteria.andUserNameEqualTo(customerPo.getUserName());
                customerPo = customerPoMapper.selectByExample(example).get(0);
                returnObject=new ReturnObject<>(new CustomerReturnVo(customerPo));
            }
            logger.debug("success trying to insert newUser");
        }

        catch (DuplicateKeyException e){
            logger.debug("failed trying to insert newUser");
            //e.printStackTrace();
            String info=e.getMessage();
            if(info.contains("user_name_uindex")){
                setBloomFilterByName("userName",customerPo);
                return new ReturnObject(ResponseCode.USER_NAME_REGISTERED);
            }
            if(info.contains("email_uindex")){
                setBloomFilterByName("email",customerPo);
                return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
            }
            if(info.contains("mobile_uindex")){
                setBloomFilterByName("mobile",customerPo);
                return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
            }

        }
        catch (Exception e){
            logger.error("Internal error Happened:"+e.getMessage());
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
        return returnObject;
    }

}
