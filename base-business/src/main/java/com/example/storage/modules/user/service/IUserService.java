package com.example.storage.modules.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.common.base.Response;
import com.example.storage.modules.sys.pojo.po.SelectCertInfoByCertSnPO;
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
import com.example.storage.modules.user.entity.User;


public interface IUserService {
	
	/**
	 * 随机查看好友
	 */
	List<User> searchReviewListByTemplate(SelectByTemplateDto dto);
	
    //添加用户
	boolean addUser(UserAddDTO User);
	
	String updateStatus(UserStatusDTO User);

	//更换手机号
	boolean updateMobile(MobileUpdateDTO UserVO);
	//显示个人信息
	User getUser();
    //个人信息修改
	Response updateUser(UserUpdateDTO updateDTO);
    //删除用户
	int deleteUser(String id);

	User selectUserDetailById(String id);


	/**
	 * 暂时去除的代码
	 * Response updateUserKssUserId(BaseDTO vehicleVO);
	 */
	//发送验证码
	Response sendSMS(String phone, int dbVerifyCode, int dbLimitTime);
	//用户验证码登录/注册
	Response telLoginRegister(TelCodeDTO dto, HttpServletRequest request,
			int dbVerifyCode, int dbLimitTime, int dbUserToken, int dbSmsVerify);
    //退出登录
	long logout(String token, int dbUserToken);

	Page<SelectUserListPO> selectUserListByTemplate(SelectByTemplateDto dto);

	/**
	 * 根据用户ID查询关联的终端列表
	 */
	Page<DeviceByUserIdPO> selectDeviceListByUserId(SelectUserVehicleMappingDto dto);

	/**
	 * 根据证书序列号查询证书详情
	 */
	SelectCertInfoByCertSnPO selectCertInfoByCertSn(String certSn);
	
	//管理系统——用户登录
	Response mobileLoginRegister(SmsVerifyDTO dto, int dbVerifyCode, int dbLimitTime);
	
	String generateUserName(int length);
	
	Response validSmsCodeAndMobile(String mobile, String verifyCode, int dbVerifyCode, int dbLimitTime);
	
	Boolean insertUser(String mobile, String userName, String userType, String userId);
	
	int insertSms(String type, String code, String content, String userId, String mobile);

	User selectByMobile(String mobile);

	boolean updateType(String userId, String type);
	
	int updateIsTestUser(UserIsTestDTO dto);
	
    Response userLogin(String userName,String pwd);

}
