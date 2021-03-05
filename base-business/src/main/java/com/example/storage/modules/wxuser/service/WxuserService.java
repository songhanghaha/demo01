package com.example.storage.modules.wxuser.service;
import java.util.List;

import com.example.storage.common.base.Response;
import com.example.storage.common.pojo.vo.BaseTemplateDto;
import com.example.storage.modules.wxuser.entity.Wxuser;

/**
 * 微信用户Service
 * @author frank
 * @datatime 2020-11-15 09:13:17
 */
public interface WxuserService {
	public Response selectWxuserList(BaseTemplateDto dto);
	
	public List<Wxuser> getAllWxUser(String userid);
}

