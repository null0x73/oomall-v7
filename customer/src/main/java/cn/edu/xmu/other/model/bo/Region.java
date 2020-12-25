package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.RegionPo;
import cn.edu.xmu.other.model.vo.RegionReturnVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/5 13:31
 */
@Data
public class Region implements VoObject {

    /**
     * @author 何明祥 24320182203193
     * createBy 何明祥 2020/12/5 13:31
     */
    public enum State {
        VALID(0, "有效"),
        INVALID(1, "废弃");

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
    private Long pid;
    private String name;
    private Long postalCode;
    private State state= State.VALID;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public Region() {
    }

    /**
     * 构造函数
     * @author 何明祥 24320182203193
     * @param regionPo 用PO构造
     * @return Region
     * createBy 何明祥 2020/12/5 15:31
     */
    public Region(RegionPo regionPo) {
        this.id=regionPo.getId();
        this.pid=regionPo.getPid();
        this.name=regionPo.getName();
        this.postalCode=regionPo.getPostalCode();
        if (null != regionPo.getState()) {
            this.state = State.getTypeByCode(regionPo.getState().intValue());
        }
        this.gmtCreate=regionPo.getGmtCreate();
        this.gmtModified=regionPo.getGmtModified();
    }

    /**
     * 用bo对象创建更新po对象
     *
     * @author 何明祥 24320182203193
     * @return RegionPo
     * createBy 何明祥 2020/12/5 19:31
     */
    public RegionPo gotRegionPo()
    {
        RegionPo regionPo=new RegionPo();
        regionPo.setId(this.getId());
        regionPo.setPid(this.getPid());
        regionPo.setName(this.getName());
        regionPo.setPostalCode(this.getPostalCode());
        regionPo.setState(this.getState().getCode().byteValue());
        regionPo.setGmtCreate(this.getGmtCreate());
        regionPo.setGmtModified(this.getGmtModified());
        return regionPo;
    }



    @Override
    public Object createVo() {
        RegionReturnVo regionReturnVo =new RegionReturnVo();
        regionReturnVo.setId(this.getId());
        regionReturnVo.setPid(this.getPid());
        regionReturnVo.setName(this.getName());
        regionReturnVo.setPostalCode(this.getPostalCode());
        regionReturnVo.setState(this.getState().getCode());
        regionReturnVo.setGmtCreate(this.getGmtCreate());
        regionReturnVo.setGmtModified(this.getGmtModified());
        return regionReturnVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

}
