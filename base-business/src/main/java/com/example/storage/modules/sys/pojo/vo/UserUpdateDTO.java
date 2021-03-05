package com.example.storage.modules.sys.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class UserUpdateDTO {
	@ApiModelProperty(value = "用户名", name = "userName")
	private String userName;
//	@ApiModelProperty(value = "原密码", name = "oldUserPassword")
//	private String oldUserPassword;
//	@ApiModelProperty(value = "新密码", name = "newUserPassword")
//	private String newUserPassword;
}
