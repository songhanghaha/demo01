package com.example.storage.modules.sys.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserIsTestDTO {
	@ApiModelProperty(value = "空间大小", name = "isTestUser")
	private String isTestUser;
	@ApiModelProperty(value = "用户id", name = "userId")
	private String userId;
}
