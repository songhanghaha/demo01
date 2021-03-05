package com.example.storage.modules.user.entity;

import com.example.storage.common.base.BaseEntity;

import lombok.Data;

/**
 * 用戶表
 * 
 * @author ljh
 * @time:2019年5月6日上午7:36:29
 * @description:
 */
@Data
public class User extends BaseEntity {
	public static final String store_one="5";//默认空间
	//用户状态
	public static final String status_0 = "1";//启用
	public static final String status_1 = "0";//禁用
	
	private String userName;//用户名称
	private String userType;// 类型，0：注册用户
	private String mobile;// 用户手机号
	private String status;// 状态 0：启用，1：禁用
	private String url;//用户头像地址
	private String store;//
	private String passWord;//密码
    
	//KSS
	public static final String syncStatus_ok = "1";
	public static final String syncStatus_not = "0";
	
	//用户是否是内测人员
	public static final int test_true = 0;//是
	public static final int test_false = 1;//否
	
	//
    private String qq;
    private String declaration;
    private String birthday;
    //
    private String usestore;//
}

