package com.example.storage.modules.user.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.common.base.KSSConstants;
import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.base.SmsContent;
import com.example.storage.common.base.exception.BusException;
import com.example.storage.common.config.enums.DELETE_STATE;
import com.example.storage.common.config.enums.UPDATE_DATA;
import com.example.storage.common.config.enums.USER_TYPE;
import com.example.storage.common.utils.HttpUtils;
import com.example.storage.common.utils.IdGen;
import com.example.storage.common.utils.JsonUtil;
import com.example.storage.common.utils.JwtUtil;
import com.example.storage.common.utils.UserThreadLocal;
import com.example.storage.modules.sys.pojo.po.SelectCertInfoByCertSnPO;
import com.example.storage.modules.sys.pojo.po.SelectDeviceListByUserIdPO;
import com.example.storage.modules.sys.pojo.vo.DeviceByUserIdPO;
import com.example.storage.modules.sys.pojo.vo.MobileUpdateDTO;
import com.example.storage.modules.sys.pojo.vo.SelectByTemplateDto;
import com.example.storage.modules.sys.pojo.vo.SelectUserListPO;
import com.example.storage.modules.sys.pojo.vo.SelectUserVehicleMappingDto;
import com.example.storage.modules.sys.pojo.vo.SmsVerifyDTO;
import com.example.storage.modules.sys.pojo.vo.TelCodeDTO;
import com.example.storage.modules.sys.pojo.vo.UserAddDTO;
import com.example.storage.modules.sys.pojo.vo.UserIsTestDTO;
import com.example.storage.modules.sys.pojo.vo.UserStatusDTO;
import com.example.storage.modules.sys.pojo.vo.UserUpdateDTO;
import com.example.storage.modules.sys.pojo.vo.back.MobileLoginResponse;
import com.example.storage.modules.sys.security.SSORealm;
import com.example.storage.modules.sys.service.IBackService;
import com.example.storage.modules.sys.utils.RedisUtil;
import com.example.storage.modules.sys.utils.ShortMessageUtils;
import com.example.storage.modules.sys.utils.TenCentMessageUtil;
import com.example.storage.modules.user.dao.SmsDao;
import com.example.storage.modules.user.dao.UserDao;
import com.example.storage.modules.user.entity.Sms;
import com.example.storage.modules.user.entity.User;
import com.example.storage.modules.user.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	public Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserDao userDao;
	@Autowired
	private SmsDao smsDao;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	ShortMessageUtils shortMessageUtils;
	@Autowired
	TenCentMessageUtil tenCentMessageUtil;
//	@Autowired
//	private IDeviceService deviceService;
	@Autowired
	private IBackService backService;

	@Value("${kss.domain}")
	private String kssBaseUrl;

	@Value("${app.token.expire}")
	private int tokenExpire;

	public static HashMap<String, Integer> map = new HashMap<>();

	public int deleteUser(String id) {
		Wrapper<User> wrapper = new EntityWrapper<>();
		wrapper.eq("id", id);
		User user = new User();
		user.setIsDeleted(DELETE_STATE.ALREADY_DELETE.getCode());
		user.setUpdateTime(new Date());
		return userDao.update(user, wrapper);
	}

	public User getUserById(String id) {
		return userDao.selectById(id);
	}

	public boolean addUser(UserAddDTO dto) {
		User user = new User();
		user.setMobile(dto.getMobile());
		user = userDao.selectOne(user);
		if (user != null) {
			logger.warn("addUser : the user has exist, param is " + dto);
			throw new BusException(ReturnCode.MOBILE_EXISTED);
		}
		// 数据填充
		user = new User();
		user.setId(IdGen.uuid());// 用户id
		user.setUserType(USER_TYPE.GENERAL.getCode());// 用户类型0普通用户
		user.setUrl(KSSConstants.headUrl);
		user.setCreateTime(new Date());
		user.setStatus(User.status_0);// 用户状态0启用
		user.setMobile(dto.getMobile());
		user.setStore(User.store_one);
//		user.setIsTestUser(User.test_false);
		if (StringUtils.isEmpty(dto.getUserName())) {
			user.setUserName(generateUserName(dto.getMobile().length()));
		} else {
			user.setUserName(dto.getUserName());
		}
		int res = userDao.insert(user);
		if (res == UPDATE_DATA.SUCCESS.getCode()) {
			logger.info("addUser : is success , mobile is " + dto.getMobile());
			return true;
		} else {
			throw new BusException(ReturnCode.INSERT_FAIL);
		}
	}

	public boolean updateMobile(MobileUpdateDTO dto) {
		logger.info("updateMobile: param is {}", dto);
		User user = userDao.selectUserByMobile(dto.getMobile());
		if (user != null) {
			logger.warn("updateMobile : The mobile number has existed , param is " + dto);
			throw new BusException(ReturnCode.MOBILE_EXISTED);
		}
		if (userDao.updateUserColunms(dto.getMobile(), null, null, null, null, dto.getId(),
				DELETE_STATE.NOT_DELETE.getCode()) == UPDATE_DATA.SUCCESS.getCode()) {
			logger.info("updateMobile is successful, param is " + dto);
			return true;
		} else {
			logger.warn("updateMobile is failed, param is " + dto);
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
	}

	public User selectUserDetailById(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		User user = userDao.selectUserById(id);
		return user;
	}

	@Override
	public String updateStatus(UserStatusDTO user) {
		if (userDao.updateUserColunms(null, user.getStatus(), null, null, null, user.getId(),
				DELETE_STATE.NOT_DELETE.getCode()) == UPDATE_DATA.SUCCESS.getCode()) {
			logger.info("updateUserStatus : success , param is " + user);
			return user.getStatus();
		} else {
			logger.warn("updateUserStatus : fail , param is " + user);
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
	}

	@Override
	public Response sendSMS(String phone, int dbVerifyCode, int dbLimitTime) {
		logger.info("Begin sendSMS method ， phoneNumber is " + phone);
		phone = phone.replace(" ", "");
		if (!shortMessageUtils.isMatch(phone)) {
			logger.warn("The phone number should be 11 digits");
			throw new BusException(ReturnCode.PHONE_NOT_FORMAT);
		}
		if (shortMessageUtils.isLimited(phone, dbLimitTime)) {
			logger.warn("Sending short messages is time-limited");
			throw new BusException(ReturnCode.SMS_TIME_LIMITED);
		}
		String code = "666666";
		Response response = null;
		boolean flag = false;// true:发送真实验证码 ，false:发送固定验证码 666666
		if (flag) {
			code = shortMessageUtils.getMsgCode(6);
			ArrayList<String> list = new ArrayList<String>();
			list.add(code);
			list.add(String.valueOf(KSSConstants.tenMinutes));
			response = tenCentMessageUtil.sendSms(phone, KSSConstants.verify_code_id, list);
		} else {
			response = new Response();
			response.setCode(ReturnCode.SUCCESS.getCode());
		}
		if (ReturnCode.SUCCESS.getCode().equals(response.getCode())) {
			shortMessageUtils.saveCodeAndExpiredDate(phone, code, KSSConstants.tenMinutes, dbVerifyCode);
			shortMessageUtils.sendSmsTimeIntervalLimit(phone, KSSConstants.oneMinutes, dbLimitTime);
			map.remove(phone);// 将此号码的记录错误次数清除
			insertSms(Sms.type_verify_code, code, SmsContent.getVerifyCodeContent(code, KSSConstants.tenMinutes), null,
					phone);
			logger.info("sendSMS is successful , code is {}", code);
			return Response.ok();
		}
		logger.warn("Failure to access tencent Short Message Platform");
		return Response.returnCode(response.getCode(), response.getMessage());
	}

	@Override
	public int insertSms(String type, String code, String content, String userId, String mobile) {
		Sms sms = new Sms();
		sms.setId(IdGen.uuid());
		sms.setType(type);
		sms.setCode(code);
		sms.setContent(content);
		sms.setUserId(userId);
		sms.setMobile(mobile);
		sms.setCreateTime(new Date());
		return smsDao.insert(sms);
	}

	@Override
	public User selectByMobile(String mobile) {
		return userDao.selectUserByMobile(mobile);
	}

	@Override
	public boolean updateType(String userId, String type) {
		return userDao.updateUserColunms(null, null, null, type, null, userId,
				DELETE_STATE.NOT_DELETE.getCode()) == UPDATE_DATA.SUCCESS.getCode();
	}

	/**
	 * 登录/注册
	 */
	@Override
	public Response telLoginRegister(TelCodeDTO dto, HttpServletRequest request, int dbVerifyCode, int dbLimitTime,
			int dbUserToken, int dbSmsVerify) {
		logger.info("Begin telLoginRegister method , TelCodeDTO is " + dto);
		String mobile = dto.getMobile().replace(" ", "");
		// 1.验证
		Response res = validSmsCodeAndMobile(mobile, dto.getVerifyCode(), dbVerifyCode, dbLimitTime);
		if (ReturnCode.SUCCESS.getCode().equals(res.getCode())) {
			// 验证成功后处理
			redisUtil.del(mobile, dbVerifyCode);// 将此号的验证码和验证码过期时间失效
			// 登录/注册
			return loginAndReg(mobile, dto.getDeviceId(), dto.getDeviceName(), dto.getSystemNumber(), request,
					dbUserToken, dbSmsVerify);
		}
		return res;
	}

	// 验证码相关验证
	public Response validSmsCodeAndMobile(String mobile, String verifyCode, int dbVerifyCode, int dbLimitTime) {
		// 判断redis的key值是否存在
		if (!redisUtil.exists(mobile, dbVerifyCode)) {
			logger.warn("The verification code for this phone does not exist");
			throw new BusException(ReturnCode.SMS_INVALID);
		}
		// 获取当前号码的value值
		String value = redisUtil.get(mobile, dbVerifyCode).toString();
		if (StringUtils.isBlank(value)) {
			logger.warn("redis not have cache value");
			throw new BusException(ReturnCode.SELECT_RESULT_NULL);
		}
		String[] valueStrings = value.split("#");
		// 获取到验证码的过期时间
		String expiredString = valueStrings[1];
		long expiredDate = 0;
		try {
			expiredDate = Long.parseLong(expiredString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new BusException(ReturnCode.ERROR_500);
		}

		// 判断用户输入的验证码当前时间是否过期:做这一步是为了防止 redis设置过期时间不起作用未能删除
		if (System.currentTimeMillis() > expiredDate) {
			logger.warn("The verification code has expired");
			redisUtil.del(mobile, dbVerifyCode);
			throw new BusException(ReturnCode.SMS_EXPIRED);
		}
		// 获取到验证码
		String code = valueStrings[0];
		if (code.equals(verifyCode)) {
			return Response.ok();
		}
		int count = Count(mobile, true);
		logger.warn("Entering error count is " + count);
		// 判断用户输入验证码错误的次数,如果等于3次，将 原来有效验证码清除，但不清空1分钟限制
		if (map.get(mobile) == 3) {
			redisUtil.del(mobile, dbVerifyCode);
			map.remove(mobile);
			logger.warn("Error in entering verification code up to 3 times");
			throw new BusException(ReturnCode.SMS_ERROR_3);
		}
		throw new BusException(ReturnCode.SMS_ERROR);
	}

	// 登录/注册操作
	private Response loginAndReg(String mobile, String deviceId, String deviceName, String systemNumber,
			HttpServletRequest request, int dbUserToken, int dbSmsVerify) {
		User user = userDao.selectUserByMobile(mobile);
		if (user == null) {
			logger.info("Currently is register, mobile is " + mobile);
			if (!dealUserAndDevice(mobile, IdGen.uuid(), deviceId, deviceName, systemNumber)) {
				throw new BusException(ReturnCode.INSERT_FAIL);
			}
			user = userDao.selectUserByMobile(mobile);
			logger.info("Register is successful");
			return Response.ok(getResultMap(user, request, dbUserToken, dbSmsVerify));
		} else {
			logger.info("Currently is login , mobile is " + mobile);
			if (User.status_1.equals(user.getStatus())) {
				logger.warn("user status is disable, user is " + user);
				throw new BusException(ReturnCode.USER_STATUS_NOT);
			}
			logger.info("Login is successful");
			return Response.ok(getResultMap(user, request, dbUserToken, dbSmsVerify));
		}
	}

	@Transactional
	public Boolean dealUserAndDevice(String mobile, String userId, String deviceId, String deviceName,
			String systemNumber) {
		String userName = generateUserName(mobile.length());
		return insertUser(mobile, userName, USER_TYPE.GENERAL.getCode(), userId);
	}

	@Override
	public long logout(String token, int dbUserToken) {
		String userId = JwtUtil.getTokenUserId(token);
		logger.info("logout userId is " + userId);
		if (redisUtil.exists(userId, dbUserToken)) {
			long count = redisUtil.del(userId, dbUserToken);
			return count;
		}
		return 0;
	}

	/**
	 * 记录用户输入验证码错误的次数
	 * 
	 * @param phone
	 * @param isFail
	 * @return
	 */
	public Integer Count(String phone, Boolean isFail) {
		Integer errorTimes = map.get(phone);
		if (errorTimes == null) {
			errorTimes = 0;
		}
		if (isFail) {
			errorTimes++;
			map.put(phone, errorTimes);
			return errorTimes;
		}
		return errorTimes;
	}

	/**
	 * 登录或注册成功返resultMap给app
	 * 
	 * @param u
	 * @return
	 */
	public Map<String, Object> getResultMap(User u, HttpServletRequest request, int dbUserToken, int dbSmsVerify) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String token = JwtUtil.createToken(u.getId(), u.getMobile(), request);
		if (token != null) {
			redisUtil.set(u.getId(), token, KSSConstants.sixtySeconds * KSSConstants.sixtyMinutes * tokenExpire,
					dbUserToken);// 有效期24小时*30天
			// token的认证有效期10分钟-->现在改成1分钟
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, KSSConstants.oneMinutes);
			long expiredTime = calendar.getTimeInMillis();
			redisUtil.set(token, String.valueOf(expiredTime), KSSConstants.sixtySeconds * KSSConstants.oneMinutes,
					dbSmsVerify);

			resultMap.put("user", u);
			resultMap.put("token", token);
		}
		return resultMap;
	}

	@Override
	public User getUser() {
		String userId = UserThreadLocal.get();
		User user = userDao.selectUserById(userId);
		return user;
	}

	@Override
	public Response updateUser(UserUpdateDTO updateDTO) {
		logger.info("Begin updateUser method , param is " + updateDTO);
		User user = getUser();
		if (user == null) {
			return Response.userNotExist();
		}
		if (userDao.updateUserColunms(null, null, null, null, updateDTO.getUserName(), user.getId(),
				DELETE_STATE.NOT_DELETE.getCode()) == UPDATE_DATA.SUCCESS.getCode()) {
			user = userDao.selectById(user.getId());
			return Response.ok(user);
		}
		throw new BusException(ReturnCode.UPDATE_ERROR);
	}

	public String generateUserName(int length) {
		String appName = "storage_";
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 生成字母：大写或小写
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				// 生成数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		String userName = appName + val;
		return userName;
	}

	@Override
	public Page<SelectUserListPO> selectUserListByTemplate(SelectByTemplateDto dto) {
		Page<SelectUserListPO> page = new Page<>(dto.getPageIndex(), dto.getPageSize());
		List<SelectUserListPO> list = userDao.selectValidListByTemplate(page, StringUtils.trim(dto.getTemplate()),
				DELETE_STATE.NOT_DELETE.getCode());
		int count = userDao.selectValidCountByTemplate(StringUtils.trim(dto.getTemplate()),
				DELETE_STATE.NOT_DELETE.getCode());
		page.setTotal(count);
		page.setRecords(list);
		return page;
	}

	@Override
	public Page<DeviceByUserIdPO> selectDeviceListByUserId(SelectUserVehicleMappingDto dto) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userId", dto.getUserId());
		String responseResult;
		Page<DeviceByUserIdPO> page = new Page<>(dto.getPageIndex(), dto.getPageSize());
		try {
			responseResult = HttpUtils.sendPost(kssBaseUrl + KSSConstants.kss_select_deviceList, jsonObject,
					HttpUtils.charset);
			JSONObject json = (JSONObject) JSONObject.parse(responseResult);
			if (!ReturnCode.SUCCESS.getCode().equals(json.get("code").toString())) {
				logger.warn(String.format("selectDeviceListByUserId : select failed , param is %s, return is %s",
						jsonObject, json));
				return null;
			}
			List<SelectDeviceListByUserIdPO> list = JsonUtil.json2List(json.getString("data"),
					SelectDeviceListByUserIdPO.class);
			if (list == null) {
				return new Page<>();
			}
			List<DeviceByUserIdPO> resultList = new ArrayList<>(
					list.stream().collect(Collectors.groupingBy(SelectDeviceListByUserIdPO::getDeviceId)).entrySet()
							.parallelStream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, e -> {
								DeviceByUserIdPO mapping = new DeviceByUserIdPO();
								List<String> authTypeList = new ArrayList<>();
								for (int i = 0; i < e.getValue().size(); i++) {
									authTypeList.add(e.getValue().get(i).getAuthType());
								}
								mapping.setDeviceId(e.getValue().get(0).getDeviceId());
								mapping.setAuthType(authTypeList);
								return mapping;
							})).values());

			page.setTotal(resultList.size());
			page.setRecords(resultList.subList(dto.getPageSize() * (dto.getPageIndex() - 1),
					((dto.getPageIndex() * dto.getPageSize()) > resultList.size() ? resultList.size()
							: (dto.getPageSize() * dto.getPageIndex()))));
			return page;
		} catch (Exception e) {
			logger.error("selectDeviceListByUserId KSS server exception : ", e);
			return new Page<>();
		}
	}

	@Override
	public SelectCertInfoByCertSnPO selectCertInfoByCertSn(String certSn) {
//   		if (StringUtils.isEmpty(certSn)) {
//   			logger.warn("selectCertInfoByCertSn : certSN is null");
//   			return null;
//		}
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("certSn", certSn);
//		String responseResult;
//		try {
//			responseResult = HttpUtils.sendPost(kssBaseUrl + Constants.kss_select_certInfo_by_certSn, jsonObject, HttpUtils.charset);
//			JSONObject json = (JSONObject) JSONObject.parse(responseResult);
//			if (!ReturnCode.SUCCESS.getCode().equals(json.get("code").toString())) {
//				logger.warn(String.format("selectCertInfoByCertSn : select failed , param is %s, return is %s", json, json));
//				return null;
//			}
//			return JsonUtil.json2Ojbect(json.getString("data"), SelectCertInfoByCertSnPO.class);
//		} catch (Exception e) {
//			logger.error("selectCertInfoByCertSn KSS server exception : ", e);
//			return null;
//		}
		return null;
	}

	@Override
	public Response mobileLoginRegister(SmsVerifyDTO dto, int dbVerifyCode, int dbLimitTime) {
		String mobile = dto.getMobile().replace(" ", "");
		Response res = validSmsCodeAndMobile(mobile, dto.getVerifyCode(), dbVerifyCode, dbLimitTime);
		if (!ReturnCode.SUCCESS.getCode().equals(res.getCode())) {
			return res;
		}
		// 验证成功后处理
		redisUtil.del(mobile, dbVerifyCode);// 将此号的验证码和验证码过期时间失效
		User user1 = userDao.selectUserByMobile(mobile);

		if(null==user1) {
		    insertUser(mobile, generateUserName(6), "0", IdGen.uuid());
		}
		User user = userDao.selectUserByMobile(mobile);
		if (User.status_1.equals(user.getStatus())) {
			logger.warn("user status is disable, user is " + user);
			throw new BusException(ReturnCode.USER_STATUS_NOT);
		}
		logger.info("Login is successful");
		return loginSuccess(user);
	}

	
	public Response loginSuccess(User user) {
		String accessToken = backService.generateToken(user);
		MobileLoginResponse mobileLoginResponse = new MobileLoginResponse();
		mobileLoginResponse.setUserId(user.getId());
		mobileLoginResponse.setAccessToken(accessToken);
		mobileLoginResponse.setLoginName(user.getUserName());
		return Response.ok(mobileLoginResponse);
	}
	@Override
	public Response userLogin(String userName,String pwd) {
        User entity=new User();
        entity.setUserName(userName);        
		User user = userDao.selectOne(entity);
		if (User.status_1.equals(user.getStatus())) {
			logger.warn("user status is disable, user is " + user);
			throw new BusException(ReturnCode.USER_STATUS_NOT);
		}
		logger.info("Login is successful");
		return loginSuccess(user);
	}

	

	@Override
	public Boolean insertUser(String mobile, String userName, String userType, String userId) {
		User user = new User();
		user.setId(userId);
		user.setCreateTime(new Date());
		user.setUrl(KSSConstants.headUrl);
		user.setUserType(userType);
		user.setStatus(User.status_0);
		user.setUserName(userName);
		user.setMobile(mobile);
		user.setStore(User.store_one);
		//user.setIsTestUser(User.test_false);// 默认不是内测人员
		return userDao.insert(user) == UPDATE_DATA.SUCCESS.getCode();
	}

	@Override
	public int updateIsTestUser(UserIsTestDTO dto) {
		User user=new User();
		user.setId(dto.getUserId());
	    user.setStore(dto.getIsTestUser());
	    
		return userDao.updateById(user);
//		if (userDao.updateUserColunms(null, null, Integer.parseInt(dto.getIsTestUser()), null, null, dto.getId(),
//				DELETE_STATE.NOT_DELETE.getCode()) == UPDATE_DATA.SUCCESS.getCode()) {
//			logger.info("updateIsTestUser : success");
//			return UPDATE_DATA.SUCCESS.getCode();
//		} else {
//			logger.warn("updateIsTestUser : fail");
//			throw new BusException(ReturnCode.UPDATE_ERROR);
//		}
	}
	
	@Override
	public List<User> searchReviewListByTemplate(SelectByTemplateDto dto) {
		String uid = ((SSORealm.Principal) SecurityUtils.getSubject().getPrincipal()).getUid();
		return userDao.selectReviewListByTemplate(StringUtils.trim(dto.getTemplate()), uid);
	}

}
