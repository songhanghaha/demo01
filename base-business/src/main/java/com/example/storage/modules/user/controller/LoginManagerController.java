package com.example.storage.modules.user.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.storage.common.base.BaseController;
import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.base.exception.BusException;
import com.example.storage.common.config.enums.REDIS_DB;
import com.example.storage.modules.sys.pojo.vo.SmsVerifyDTO;
import com.example.storage.modules.user.pojo.vo.UserPwdDto;
import com.example.storage.modules.user.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "用户管理", tags = "用户管理")
@RestController
@RequestMapping(value = "/mobile")
public class LoginManagerController extends BaseController{
	
	@Autowired
    private IUserService userService;
	
	@ApiOperation(value = "发送验证码")
	@RequestMapping(value = "sendVerifyCode", method = RequestMethod.POST)
	public Response sendVerifyCode(String mobile){
		if (StringUtils.isBlank(mobile)) {
			logger.warn("sendVerifyCode parameter cannot be empty");
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		return userService.sendSMS(mobile, REDIS_DB.MANAGER_VERIFY_CODE.getDbIndex(), REDIS_DB.MANAGER_LOGIN_LIMIT_TIME.getDbIndex());
	}
	
    @ApiOperation(value = "免密登录")
	@RequestMapping(value = "mobileLoginRegister", method = RequestMethod.POST)
	public Response mobileLoginRegister(SmsVerifyDTO dto) {
		if (dto == null || StringUtils.isAnyBlank(dto.getMobile(), dto.getVerifyCode())) {
			logger.warn("mobileLoginRegister parameter cannot be empty , param is {}", dto);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		return userService.mobileLoginRegister(dto, REDIS_DB.MANAGER_VERIFY_CODE.getDbIndex(),  REDIS_DB.MANAGER_LOGIN_LIMIT_TIME.getDbIndex());
	}
    
    @ApiOperation(value = "账号密码登录")
  	@RequestMapping(value = "userlogin", method = RequestMethod.POST)
  	public Response mobileLoginRegister(UserPwdDto dto) {
  		if (dto == null || StringUtils.isAnyBlank(dto.getUsername(), dto.getUserpwd())) {
  			logger.warn("mobileLoginRegister parameter cannot be empty , param is {}", dto);
  			throw new BusException(ReturnCode.PARAMETER_ERROR);
  		}
  		return userService.userLogin(dto.getUsername(), dto.getUserpwd());
  	}

}
