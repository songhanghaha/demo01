package com.example.storage.modules.expresstake.service;
import java.util.List;

import com.example.storage.common.base.Response;
import com.example.storage.common.pojo.vo.BaseTemplateDto;
import com.example.storage.modules.expresstake.entity.Expressinf;

/**
 * 快递信息Service
 * @author frank
 * @datatime 2021-01-11 03:57:49
 */
public interface ExpressinfService {
	public Response selectExpressinfList(BaseTemplateDto dto);
	
	public List<Expressinf> getExpressinfs(String userid);
}

