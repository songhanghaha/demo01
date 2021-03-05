package com.example.storage.modules.sys.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TelCodeDTO {
	@ApiModelProperty(value = "用户手机号",name = "mobile")
    private String mobile;
	@ApiModelProperty(value = "验证码",name = "verifyCode")
    private String verifyCode;
	@ApiModelProperty(value = "设备Id",name = "deviceId")
	private String deviceId;
	@ApiModelProperty(value = "设备名称",name = "deviceName")
	private String deviceName;
	@ApiModelProperty(value = "系统版本号",name = "systemNumber")
	private String systemNumber;
}
