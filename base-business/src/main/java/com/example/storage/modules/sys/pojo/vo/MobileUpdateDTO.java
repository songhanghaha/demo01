package com.example.storage.modules.sys.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MobileUpdateDTO{
	@ApiModelProperty(value = "用户id", name = "id")
	private String id;
	@ApiModelProperty(value = "更换的手机号", name = "mobile")
	private String mobile;
}
