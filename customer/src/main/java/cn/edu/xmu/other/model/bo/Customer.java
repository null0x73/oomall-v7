package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.CustomerPo;
import cn.edu.xmu.other.model.vo.CustomerReturnVo;
import cn.edu.xmu.other.model.vo.CustomerSimpleRetVo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/11/29 19:25
 */
@Data
public class Customer implements VoObject {

    public static String AESPASS = "OOAD2020-11-01";


    public Customer() {

    }

    /**
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/11/29 19:25
     */
    public enum Gender {
        MALE(0, "男"),
        FEMALE(1, "女");

        private static final Map<Integer, Gender> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Gender enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }
        private int code;
        private String description;

        Gender(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Gender getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/11/29 19:25
     */
    public enum State {
        MANAGEMENT(0, "后台用户"),
        NORMAL(4, "正常用户"),
        FORBIDDEN(6,"被封禁用户");

        private static final Map<Integer, State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }
        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }



    private Long id;
    private String userName;
    private String password;
    private String realName;
    private Gender gender;
    private LocalDate birthday;
    private int point;
    private State state;
    private String email;
    private String mobile;
    private Boolean beDeleted;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    /**
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/11/29 19:25
     */
    public Customer(CustomerPo customerPo){
        this.id=customerPo.getId();
        this.userName=customerPo.getUserName();
        this.password=customerPo.getPassword();
        this.realName=customerPo.getRealName();
        this.gender= Gender.getTypeByCode(customerPo.getGender().intValue());
        this.birthday=customerPo.getBirthday();
        this.point=customerPo.getPoint();
        this.state= State.getTypeByCode(customerPo.getState().intValue());
        this.email=customerPo.getEmail();
        this.mobile=customerPo.getMobile();
        int i=customerPo.getBeDeleted().intValue();
        this.beDeleted=(i==1)?true:false;
        this.gmtCreate=customerPo.getGmtCreate();
        this.gmtModified=customerPo.getGmtModified();
    }

    /**
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/11/29 19:25
     */
    public CustomerPo gotCustomerPo(){
        CustomerPo customerPo=new CustomerPo();
        customerPo.setId(this.getId());
        customerPo.setUserName(this.getUserName());
        customerPo.setPassword(this.getPassword());
        customerPo.setRealName(this.getRealName());
        customerPo.setGender(this.getGender().getCode().byteValue());
        customerPo.setBirthday(this.getBirthday());
        customerPo.setPoint(this.getPoint());
        customerPo.setState(this.getState().getCode().byteValue());
        customerPo.setEmail(this.getEmail());
        customerPo.setMobile(this.getMobile());
        Integer i=this.getBeDeleted()?1:0;
        customerPo.setBeDeleted(i.byteValue());
        customerPo.setGmtCreate(this.getGmtCreate());
        customerPo.setGmtModified(this.getGmtModified());
        return customerPo;
    }

    @Override
    public Object createVo(){
        CustomerReturnVo returnVo = new CustomerReturnVo();

        returnVo.setUserName(this.getUserName());
        returnVo.setName(this.getRealName());
        returnVo.setGender(this.getGender().getCode());
        returnVo.setBirthday(this.getBirthday());
        returnVo.setState(this.getState().getCode());
        returnVo.setEmail(this.getEmail());
        returnVo.setMobile(this.getMobile());


        return returnVo;
    }

    @Override
    public Object createSimpleVo() {
        CustomerSimpleRetVo customerSimpleRetVo=new CustomerSimpleRetVo();
        customerSimpleRetVo.setId(this.getId());
        customerSimpleRetVo.setUserName(this.getUserName());
        customerSimpleRetVo.setRealName(this.getRealName());
        return customerSimpleRetVo;
    }
}
