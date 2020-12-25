package cn.edu.xmu.other.model.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShareRule {
    @NotNull
    Integer num;
    @NotNull
    Integer rate;
}
