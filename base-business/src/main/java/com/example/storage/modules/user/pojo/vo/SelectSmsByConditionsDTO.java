package com.example.storage.modules.user.pojo.vo;

import com.example.storage.common.base.PageDTO;

import lombok.Data;

@Data
public class SelectSmsByConditionsDTO extends PageDTO {

	private String phoneNumber;
	
	private String type;

}
