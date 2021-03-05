package com.example.storage.modules.sys.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserAddDTO {
	@ApiModelProperty(value = "用户手机号", name = "mobile")
	private String mobile;
	@ApiModelProperty(value = "用户名", name = "userName")
	private String userName;
}
