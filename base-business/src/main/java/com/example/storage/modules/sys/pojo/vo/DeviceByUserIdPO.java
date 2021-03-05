package com.example.storage.modules.sys.pojo.vo;

import java.util.List;

import lombok.Data;

@Data
public class DeviceByUserIdPO {
	private String deviceName;
	private String deviceId;
	private List<String> authType;
}
