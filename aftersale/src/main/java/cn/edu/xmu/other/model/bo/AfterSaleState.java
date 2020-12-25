package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.vo.AfterSaleStateRetVo;
import cn.edu.xmu.other.util.AfterSaleResponseCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AfterSaleState implements VoObject, Serializable {
    @Override
    public Object createVo() {
        return getRetList();
    }

    @Override
    public Object createSimpleVo() {
        return getRetList();
    }

    private List<AfterSaleStateRetVo> getRetList() {
        List<AfterSaleResponseCode> states = new ArrayList<>();
        Collections.addAll(states, AfterSaleResponseCode.values());
        List<AfterSaleStateRetVo> ret = new ArrayList<>(states.size());
        for (AfterSaleResponseCode code : states) {
            AfterSaleStateRetVo retVo = new AfterSaleStateRetVo();
            retVo.setCode(code.getCode());
            retVo.setName(code.getMessage());
            ret.add(retVo);
        }
        return ret;
    }
}
