package com.example.storage.modules.sys.pojo.po;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SmsListPO {
	private String mobile;
	private String type;
	private String code;
	private String content;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
}
