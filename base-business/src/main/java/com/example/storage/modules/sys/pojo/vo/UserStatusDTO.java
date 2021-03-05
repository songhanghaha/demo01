package com.example.storage.modules.sys.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserStatusDTO {
	@ApiModelProperty(value = "状态：0启用，1:禁用", name = "status")
	private String status;
	@ApiModelProperty(value = "用户id", name = "id")
	private String id;
}
