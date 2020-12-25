package cn.edu.xmu.other.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分享活动传值对象
 * @author 潘登
 * @date 2020/11/26 18：03
 */
@Data
@ApiModel("分享活动传值对象")
public class ShareActivityVo {
    @NotBlank
    private String beginTime;
    @NotBlank
    private String endTime;
    @NotBlank
    private String strategy;
}
