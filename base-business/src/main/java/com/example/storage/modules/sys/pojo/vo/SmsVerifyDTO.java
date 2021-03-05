package com.example.storage.modules.sys.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class SmsVerifyDTO {
	@ApiModelProperty(value = "用户手机号",name = "mobile")
    private String mobile;
	@ApiModelProperty(value = "验证码",name = "verifyCode")
    private String verifyCode;

}
