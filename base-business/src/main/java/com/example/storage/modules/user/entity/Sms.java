package com.example.storage.modules.user.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Sms {
	//短信类型：1.验证码通知 2.审核通知 3.分享通知 4.解绑通知
	public static final String type_verify_code = "1";
	public static final String type_audit_inform = "2";
	public static final String type_share_inform = "3";
	public static final String type_unbind_inform = "4";
	
	private String id; //主键id
	private String type;// 短信类型
	private String code;//验证码
	private String content;// 短信内容
	private String userId;//用户ID
	private String mobile;// 用户手机号
	private Date createTime;// 创建时间
}
