package com.example.storage.modules.sys.entity;

import java.util.Date;

import lombok.Data;

@Data
public class ApikeyPermission {

    private String apikey;//apikey

    private String permissions;//权限
    
    private Date createTime;// 创建时间
	
	private Date updateTime;// 更新时间

}
