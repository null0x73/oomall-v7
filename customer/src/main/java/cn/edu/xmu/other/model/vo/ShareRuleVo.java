package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.ShareRule;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ShareRuleVo {
    @NotNull
    List<ShareRule> rule;
    @NotNull
    Integer firstOrAvg;
}
