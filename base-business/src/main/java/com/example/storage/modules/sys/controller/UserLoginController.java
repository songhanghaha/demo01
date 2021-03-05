package com.example.storage.modules.sys.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.base.exception.BusException;
import com.example.storage.common.utils.IdGen;
import com.example.storage.modules.sys.entity.MailValidate;
import com.example.storage.modules.sys.entity.Role;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.sys.pojo.vo.AddManagerUserDTO;
import com.example.storage.modules.sys.pojo.vo.SelectByTemplateDto;
import com.example.storage.modules.sys.pojo.vo.SelectManagerUserInfo;
import com.example.storage.modules.sys.pojo.vo.back.LoginResponse;
import com.example.storage.modules.sys.service.IBackService;
import com.example.storage.modules.sys.utils.MailUtil;
import com.example.storage.modules.sys.utils.RedisUtil;
import com.example.storage.modules.sys.utils.VerifyCodeUtils;
import com.example.storage.modules.user.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(tags = "系统管理-登录管理")
@RestController
@RequestMapping("/user")
public class UserLoginController {
	@Value("${sso.rabbitmq.uri}")
	private String rabbitMqUri;
//
//    @Value("${sso.email.interval}")
//    private String interval;

	@Autowired
	private IBackService backService;

	@Autowired
	private MailUtil mailUtil;
	
	@Autowired
    private IUserService userService;
	
	@Autowired
	private RedisUtil redisUtil;

	@ApiOperation(value = "用户信息分页列表", notes = "用户分页列表")
	@RequestMapping(value = "managerList", method = RequestMethod.POST)
	public Response UserList(SelectByTemplateDto dto) {
		if (null == dto || dto.getPageSize() <= 0) {
			logger.warn("userList : param is error, param is " + dto);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		Page<SelectManagerUserInfo> page = backService.selectManagerUserList(dto);
		return page == null ? Response.ok(new Page<>()) : Response.ok(page);
	}

	private static Logger logger = LogManager.getLogger(UserLoginController.class.getName());

	private Pattern reEmail = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

	private Map<String, Integer> loginErr = new HashMap<String, Integer>();

	private HashMap<String, String> codeMap = new HashMap<String, String>(10);

	private Map<String, Integer> updatePasswordErrorMap = new HashMap<>();

	private Map<String, Date> updatePasswordLimitDateMap = new HashMap<>();

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Response userLogin(String loginName, String password, String verifyCode, String key) {
		UserInfo userInfo;
		if (reEmail.matcher(loginName).matches()) {
			// Login by email
			userInfo = backService.selectUserByEmail(loginName);
		} else {
			// Login by username
			userInfo = backService.selectUserByUserName(loginName);
		}
		if (userInfo == null) {
			return Response.selectFailed();
		}
		if (userInfo.getState() != 1) {
			logger.warn("用户未激活邮箱 , " + userInfo.getUsername());
			return Response.returnCode(ReturnCode.USER_NOT_ACTIVATE.getCode(), ReturnCode.USER_NOT_ACTIVATE.getMsg());
		}
		// 每次请求之前先验证此登录名是不是到达错误上限
		if (getUserLoginErrNum(userInfo.getUsername())) {
			if (validateImgCode(key, verifyCode)) {
				if (backService.validate(userInfo, password)) {
					return loginSuccess(userInfo);
				} else {
					isValidateCodeLogin(userInfo.getUsername(), true, false);
					return Response.returnCode(ReturnCode.PASSWORD_NOT_MATCH.getCode(), ReturnCode.PASSWORD_NOT_MATCH.getMsg());
				}
			} else {
				return Response.returnCode(ReturnCode.SMS_ERROR.getCode(), ReturnCode.SMS_ERROR.getMsg());
			}
		} else {
			// 未达上限 正常登录
			if (backService.validate(userInfo, password)) {
				return loginSuccess(userInfo);
			} else {
				// 达到错误上限产生验证码
				if (isValidateCodeLogin(userInfo.getUsername(), true, false)) {
					return Response.returnCode(ReturnCode.USER_ERROR_TO_MANY.getCode(), ReturnCode.USER_ERROR_TO_MANY.getMsg());
				}
				return Response.returnCode(ReturnCode.PASSWORD_NOT_MATCH.getCode(), ReturnCode.PASSWORD_NOT_MATCH.getMsg());
			}
		}
	}

	@ApiOperation(value = "新增用户", notes = "新增用户")
//	@RequiresPermissions(value = "manager:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public Response add(AddManagerUserDTO dto) {
		if (null == dto || StringUtils.isEmpty(dto.getUsername()) || StringUtils.isEmpty(dto.getPassword()) || StringUtils.isEmpty(dto.getPasswordCheck())
				|| StringUtils.isEmpty(dto.getEmail()) || StringUtils.isEmpty(dto.getRole())) {
			logger.warn("manager user add : param is error, param is " + dto);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		if(backService.add(dto)) {
			return Response.ok();
		} else {
			throw new BusException(ReturnCode.BIZ_FAIL);
		}
	}
	
	@ApiOperation(value = "更新用户", notes = "更新用户")
	@RequiresPermissions(value = "manager:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Response update(AddManagerUserDTO dto) {
		if (null == dto ||StringUtils.isEmpty(dto.getRole())) {
			logger.warn("manager user update : param is error, param is " + dto);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		if(backService.update(dto)) {
			return Response.ok();
		} else {
			throw new BusException(ReturnCode.BIZ_FAIL);
		}
	}
	
	@RequestMapping(value = "/getUserLoginErr", method = RequestMethod.POST)
	public Response getUserErr(String username) {
		boolean flag = getUserLoginErrNum(username);
		if (flag) {
			return Response.fail();
		} else {
			return Response.ok();
		}
	}

	private Response loginSuccess(UserInfo userInfo) {
		String loginToken = backService.generateToken(userInfo);
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setEmail(userInfo.getEmail());
		loginResponse.setAccessToken(loginToken);
		loginResponse.setLoginName(userInfo.getUsername());
		loginResponse.setPermissions(backService.getPermissionByRoleName(userInfo.getRoles()));
		isValidateCodeLogin(userInfo.getUsername(), false, true);
		// return ApiResponseUtil.pass("login successful", loginResponse);
		return Response.ok(loginResponse);
	}

	@RequestMapping("/tokenValidate")
	public Response tokenValidate(String uuid) throws Exception {
		boolean flag = backService.validateToken(uuid);
		if (flag) {
			// return ApiResponseUtil.pass("token有效");
			return Response.ok();
		} else {
			// return ApiResponseUtil.fieldInvalid("uuid", "用户未登录或已注销");
			return Response.fail();
		}

	}

	@RequestMapping(value = "/loginOut", method = RequestMethod.POST)
	public Response loginOut(HttpServletRequest request) {
		try {
//            Subject subject = SecurityUtils.getSubject();
//            SSOAuthorizingRealm.Principal principal = (SSOAuthorizingRealm.Principal) subject.getPrincipal();
//            tokenService.removeLoginListByUUID(principal.getUuid());
			// 使用mq发送通知
			/*ConnectionFactory connectionFactory = new ConnectionFactory();
			connectionFactory.setUri(rabbitMqUri);
			Connection connection = connectionFactory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclarePassive("logout");
			Map<String, Object> prop = new HashMap<String, Object>();
			// prop.put("uuid", principal.getUuid());
			channel.basicPublish("logout", "", new AMQP.BasicProperties.Builder().headers(prop).build(), null);
			channel.close();
			connection.close();*/
			// subject.logout();
			// return ApiResponseUtil.pass("注销成功");
			return Response.returnCode(ReturnCode.LOGOUT_OK.getCode(), ReturnCode.LOGOUT_OK.getMsg());
		} catch (Exception e) {
			logger.error("loginOut error : ", e);
			return Response.systemError();
		}
	}

//    @RequiresRoles(value = {"super_admin"})
//    @RequestMapping(value = "/getAllUserList", method = RequestMethod.POST)
//    public Response getAllUserList() {
//        List<UserInfoVo> userInfoList = backService.getAllUserInfo();
//        Response resultVo = ApiResponseUtil.pass();
//        resultVo.setResult(userInfoList);
//
//        return resultVo;
//    }

	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public Response getUserByUid(String uid) {
//        if (StringUtils.isEmpty(uid)) {
//            return ApiResponseUtil.fieldInvalid("uid", "uid = null");
//        }
//        if (!SecurityUtils.getSubject().hasRole("super_admin") &&
//                !((SSOAuthorizingRealm.Principal) SecurityUtils.getSubject().getPrincipal()).getUid().equals(uid)) {
//            return ApiResponseUtil.fieldInvalid("role", "权限不足！");
//        }
		UserInfo userInfo = backService.selectUserByUserId(uid);
		if (userInfo == null) {
			// return ApiResponseUtil.notFound("用户不存在");
			return Response.fail();
		}
		// return ApiResponseUtil.pass("", userInfo);
		return Response.ok(userInfo);
	}

	/**
	 * 通过登录名查找用户
	 *
	 * @param loginName
	 * @return Response
	 */
	@RequestMapping(value = "/getUserByLoginName", method = RequestMethod.POST)
	public Response getUserByLoginName(String loginName) {
		UserInfo userInfo;
		if (reEmail.matcher(loginName).matches()) {
			// Login by email
			userInfo = backService.selectUserByEmail(loginName);
		} else {
			// Login by username
			userInfo = backService.selectUserByUserName(loginName);
		}
		if (userInfo == null) {

			return Response.fail();
		} else {
			return Response.ok(userInfo);
		}

	}

	@RequestMapping(value = "/findPwd", method = RequestMethod.POST)
	public Response findPwd(String email, String password, String code) {
		try {
			if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
				logger.warn("findPwd : param is null , email is " + email);
				return Response.paramError();
			}
			UserInfo userInfo = backService.selectUserByEmail(email);
			if (userInfo == null) {
				logger.warn("findPwd : email has not been registered");
				return Response.userNotExist();
			}

			//比较邮件验证码
			MailValidate mailValidate = backService.selectMailValidateByEmail(email);
			if (mailValidate == null) {
				logger.warn("findPwd : select mailValidate result is null");
				return Response.userNotExist();
			}
			if(! mailValidate.getToken().equals(code)) {
				logger.warn(String.format("findPwd : verify code not match, accept code is %s, db code is %s ", code, mailValidate.getToken()));
				return Response.returnCode(ReturnCode.SMS_ERROR.getCode(), ReturnCode.SMS_ERROR.getMsg());
			}
			//判断验证码是否过期
			if (Long.parseLong(mailValidate.getExpiryTime()) < new Date().getTime()) {
				logger.warn("findPwd: verify code has expiry");
				return Response.returnCode(ReturnCode.SMS_EXPIRED.getCode(), ReturnCode.SMS_EXPIRED.getMsg());
			}
			//更新密码, 更新验证码
			backService.updatePasswordByEmail(email, password);
			mailValidate.setState(1);
			backService.updateMailValidateStateByEmail(mailValidate);
			logger.info("findPwd : success , userInfo is " + userInfo);
			return Response.ok();
		} catch (Exception ex) {
			logger.error("findPwd error : ", ex);
			return Response.systemError();
		}
	}

	@RequestMapping(value = "/newPwd", method = RequestMethod.POST)
	public Response newPwd(String email, String oldPass, String newPass) {
		try {
			if (StringUtils.isEmpty(email)  || StringUtils.isEmpty(oldPass) || StringUtils.isEmpty(newPass)) {
				logger.warn("newPwd : email is " + email);
				return Response.paramError();
			}

			//判断是否限制修改密码
			if (updatePasswordLimitDateMap.get(email) != null &&
					DateUtils.addHours(updatePasswordLimitDateMap.get(email),1).after(new Date())) {
				return Response.returnCode(ReturnCode.USER_ERROR_TO_MANY.getCode(), ReturnCode.USER_ERROR_TO_MANY.getMsg());
			}

			UserInfo user = backService.selectUserByEmail(email);
			if (user == null || user.getId() == null) {
				logger.warn("newPwd : select user by email is null , email is " + email);
				return Response.userNotExist();
			}

			if (! backService.validate(user, oldPass)) {
				logger.warn("newPwd: old password not match, user is " + user);
				updatePasswordErrorMap.put(email, updatePasswordErrorMap.get(email) == null ? 1 : updatePasswordErrorMap.get(email) + 1);
				//判断错误次数是否达到3次
				if (updatePasswordErrorMap.get(email) != null && updatePasswordErrorMap.get(email) >= 3) {
					logger.warn("newPwd: too many failures , email is " + email);
					updatePasswordLimitDateMap.put(email, new Date());
					updatePasswordErrorMap.remove(email);
					return Response.returnCode(ReturnCode.USER_ERROR_TO_MANY.getCode(), ReturnCode.USER_ERROR_TO_MANY.getMsg());
				} else {
					return Response.returnCode(ReturnCode.PASSWORD_NOT_MATCH.getCode(), ReturnCode.PASSWORD_NOT_MATCH.getMsg());
				}
			}

			backService.updatePasswordByEmail(email, newPass.trim());
			updatePasswordErrorMap.remove(email);
			updatePasswordLimitDateMap.remove(email);
			logger.info("newPwd success, user is " + user);
			return Response.ok();
		} catch (Exception ex) {
			logger.error("newPwd error is ", ex);
			return Response.systemError();
		}
	}

	@RequestMapping(value = "/buildCodeImg", method = RequestMethod.GET)
	public Response getImgValidate(String key, HttpServletResponse response) {
		try {
			int w = 100, h = 30;
			String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
			Response pass = Response.ok();
			// codeMap.put(key, verifyCode.toLowerCase(), Long.parseLong(interval));
			codeMap.put(key, verifyCode.toLowerCase());
			VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
			return pass;
		} catch (Exception e) {
			logger.error("getImgValidate error :" , e);
			// return ApiResponseUtil.unknowProblem(e);
			return Response.systemError();
		}
	}

	/**
	 * 验证图片验证码
	 *
	 * @param key
	 * @param code
	 */
	public boolean validateImgCode(String key, String code) {
		try {
			String validateCode = codeMap.get(key);
			if (validateCode != null) {
				if (validateCode.equals(code.toLowerCase())) {
					codeMap.remove(key);
					return true;
				} else {
					codeMap.remove(key);
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 判断用户是否以验证码登录
	 *
	 * @param username
	 * @param isFail
	 * @param clean
	 */
	public boolean isValidateCodeLogin(String username, boolean isFail, boolean clean) {
		Integer loginFailNum = loginErr.get(username);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			loginErr.put(username, loginFailNum);
		}
		if (clean) {
			loginErr.remove(username);
		}
		return loginFailNum >= 3;
	}

	public boolean getUserLoginErrNum(String username) {
		Integer loginFailNum = loginErr.get(username);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		return loginFailNum >= 3;
	}

	//
	// @RequiresUser
	// @RequireRealLogin
	@RequestMapping(value = "/getPermission", method = RequestMethod.POST)
	public Response getPermission(String uid) {
		UserInfo user = backService.selectUserByUserId(uid);
		List<Role> roleList = new ArrayList<Role>();
		return Response.ok();
	}

	@ApiOperation(value = "发送邮件验证码")
	@RequestMapping(value = "sendEmail", method = RequestMethod.POST)
	public Response sendEmail(String email){
		if (StringUtils.isEmpty(email)) {
			logger.warn("sendEmail:  parameter cannot be empty");
			return Response.paramError();
		}

		UserInfo userInfo = backService.selectUserByEmail(email);
		if (userInfo == null) {
			logger.warn("findPwd : email has not been registered");
			return Response.userNotExist();
		}

		//构建六位随机数
		String code = String.format("%06d", new Random().nextInt(1000000));

		//发送邮件
		if(! mailUtil.sendMail(email, "找回密码验证码", "你正在进行密码找回, 验证码是 "  + code + " 请在5分钟内完成身份认证")) {
			logger.warn("sendEmail : send failed");
			return Response.returnCode(ReturnCode.BIZ_FAIL.getCode(), ReturnCode.BIZ_FAIL.getMsg());
		}

		//往邮件表里插入数据, 设置存活时间
		MailValidate mailValidate = backService.selectMailValidateByEmail(email);
		if (mailValidate == null) {
			mailValidate = new MailValidate();
			mailValidate.setExpiryTime(String.valueOf(DateUtils.addMinutes(new Date(), 5).getTime()));
			mailValidate.setToken(code);
			mailValidate.setTargetMail(email);
			mailValidate.setCreateDate(new Date());
			mailValidate.setId(IdGen.uuid());
			mailValidate.setState(0);
			backService.insertMailValidate(mailValidate);
		} else {
			mailValidate.setExpiryTime(String.valueOf(DateUtils.addMinutes(new Date(), 5).getTime()));
			mailValidate.setToken(code);
			mailValidate.setUpdateDate(new Date());
			backService.updateMailValidateByEmail(mailValidate);
		}
		return Response.ok();
	}

}
