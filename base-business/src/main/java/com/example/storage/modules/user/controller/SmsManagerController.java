package com.example.storage.modules.user.controller;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.base.exception.BusException;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.user.pojo.po.SmsListPO;
import com.example.storage.modules.user.pojo.vo.SelectSmsByConditionsDTO;
import com.example.storage.modules.user.service.ISmsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "短信管理", tags = "短信管理")
@RestController
@RequestMapping(value = "manager/user")
public class SmsManagerController {
	
	@Autowired
	private ISmsService smsService;
	
	private Logger logger = LoggerFactory.getLogger(SmsManagerController.class);
	@ApiOperation(value = "根据条件查询短信记录", notes = "短息记录")
	@RequestMapping(value = "selectSmsList", method = RequestMethod.POST)
	public Response selectSmsList(SelectSmsByConditionsDTO dto){
		if (dto == null || dto.getPageSize() <= 0) {
			logger.warn("sendVerifyCode parameter cannot be empty");
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		Page<SmsListPO> page = smsService.selectSmsList(dto);
		return page == null ? Response.ok(new Page<>()) : Response.ok(page);
	}
}
