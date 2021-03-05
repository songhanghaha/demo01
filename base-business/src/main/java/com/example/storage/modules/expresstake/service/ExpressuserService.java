package com.example.storage.modules.expresstake.service;
import java.util.List;

import com.example.storage.common.base.Response;
import com.example.storage.common.pojo.vo.BaseTemplateDto;
import com.example.storage.modules.expresstake.entity.Expressuser;

/**
 * 用户信息Service
 * @author frank
 * @datatime 2021-01-11 03:57:49
 */
public interface ExpressuserService {
	public Response selectExpressuserList(BaseTemplateDto dto);
	
	public List<Expressuser> getExpressusers(String userid);
}

