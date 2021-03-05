package com.example.storage.modules.user.pojo.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class UserPwdDto {
	@ApiModelProperty(value = "用户手机号",name = "mobile")
    private String username;
	@ApiModelProperty(value = "验证码",name = "verifyCode")
    private String userpwd;

}
