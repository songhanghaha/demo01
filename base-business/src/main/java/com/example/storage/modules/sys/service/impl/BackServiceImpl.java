package com.example.storage.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.base.exception.BusException;
import com.example.storage.common.config.enums.DELETE_STATE;
import com.example.storage.common.utils.IdGen;
import com.example.storage.modules.sys.dao.MailValidateDao;
import com.example.storage.modules.sys.dao.RoleDao;
import com.example.storage.modules.sys.dao.UserInfoDao;
import com.example.storage.modules.sys.entity.MailValidate;
import com.example.storage.modules.sys.entity.Role;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.sys.pojo.vo.AddManagerUserDTO;
import com.example.storage.modules.sys.pojo.vo.AddRoleDto;
import com.example.storage.modules.sys.pojo.vo.SelectByTemplateDto;
import com.example.storage.modules.sys.pojo.vo.SelectManagerUserInfo;
import com.example.storage.modules.sys.pojo.vo.back.TokenEntity;
import com.example.storage.modules.sys.pojo.vo.back.UserInfoVo;
import com.example.storage.modules.sys.service.IBackService;
import com.example.storage.modules.sys.utils.CryptoUtils;
import com.example.storage.modules.sys.utils.TokenUtils;
import com.example.storage.modules.user.entity.User;

@Service
public class BackServiceImpl implements IBackService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserInfoDao userInfoDao;

	@Autowired
	private RoleDao roleDao;

	@Override
	public UserInfo selectUserByEmail(String email) {

		return userInfoDao.selectUserByEmail(email);
	}

	@Override
	public UserInfo selectUserByUserName(String username) {

		return userInfoDao.selectUserByUserName(username);
	}

	@Override
	public UserInfo selectUserByUserId(String id) {
		return userInfoDao.selectUserByUserId(id);
	}

	@Override
	public void chageUserPwd(String newPwd, String uid) {
		userInfoDao.chageUserPwd(newPwd, uid);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer userRegister(UserInfo userInfo) {
		try {
			userInfo.setId(IdGen.uuid());
			userInfo.setPassword(CryptoUtils.hashPassword(userInfo.getPassword().trim()));
			return userInfoDao.userRegister(userInfo);
		} catch (Exception e) {
			logger.error("UserServiceImpl.userRegister:", e);
			return 0;
		}
	}

	@Override
	public Page<SelectManagerUserInfo> selectManagerUserList(SelectByTemplateDto dto) {
		// 根据是否有查询条件判断
		Page<SelectManagerUserInfo> page = new Page<>(dto.getPageIndex(), dto.getPageSize());
		List<SelectManagerUserInfo> list;
		int count;
		if (StringUtils.isEmpty(dto.getTemplate().trim())) {
			list = userInfoDao.selectValidList(page, DELETE_STATE.NOT_DELETE.getCode());
			count = userInfoDao.selectValidCount(DELETE_STATE.NOT_DELETE.getCode());
		} else {
			list = userInfoDao.selectValidListByTemplate(page, dto.getTemplate().trim(),
					DELETE_STATE.NOT_DELETE.getCode());
			count = userInfoDao.selectValidCountByTemplate(dto.getTemplate().trim(), DELETE_STATE.NOT_DELETE.getCode());
		}
		page.setTotal(count);
		page.setRecords(list);
		return page;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserStateByValidate(UserInfo userInfo) {
		userInfo.setState(1);
		userInfoDao.updateUserStateByValidate(userInfo);
	}

	@Override
	public void updatePasswordByEmail(String email, String password) {
		UserInfo user = new UserInfo();
		String encpassword = CryptoUtils.hashPassword(password);
		user.setEmail(email);
		user.setPassword(encpassword);
		userInfoDao.update(user);
	}

	@Override
	public void updateUserField(UserInfo userInfo) {
		userInfoDao.updateUserField(userInfo);
	}

	@Override
	public void updateUserEmail(UserInfo userInfo) {

		userInfoDao.updateUserEmail(userInfo);
	}

	@Override
	public boolean validate(UserInfo userInfo, String password) {
		return CryptoUtils.validatePassword(password, userInfo.getPassword());
	}

	// 邮箱相关
	@Autowired
	private MailValidateDao mailValidateDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer insertMailValidate(MailValidate mailValidate) {

		mailValidate.setId(IdGen.uuid());
		return mailValidateDao.insertMailValidate(mailValidate);
	}

	@Override
	public MailValidate selectMailValidateByEmail(String targetMail) {

		return mailValidateDao.selectMailValidateByEmail(targetMail);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMailValidateByEmail(MailValidate mailValidate) {

		mailValidateDao.updateMailValidateByEmail(mailValidate);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMailValidateStateByEmail(MailValidate mailValidate) {
		mailValidate.setState(1);
		mailValidateDao.updateMailValidateStateByEmail(mailValidate);
	}

	@Override
	public void updateUseremail(String oldEmail, String newEmail) {
		mailValidateDao.updateUseremail(oldEmail, newEmail);
	}

	// TOKEN相关
	private byte[] peKey;
	private byte[] tokenAesKey;
	private byte[] tokenHmacKey;

	@Value("${sso.token.expire}")
	private int tokenExpire;

	private List<String> userLoginList = new ArrayList<String>();

	public BackServiceImpl(@Value("${sso.pekey}") String hexPeKey, @Value("${sso.aes-key}") String hexAesKey,
			@Value("${sso.hmac-key}") String hexHmacKey) throws DecoderException {
		this.peKey = Hex.decodeHex(hexPeKey.toCharArray());
		if (this.peKey.length != 16 && this.peKey.length != 24 && this.peKey.length != 32) {
			throw new IllegalArgumentException("Invalid PEKey length");
		}
		this.tokenAesKey = Hex.decodeHex(hexAesKey.toCharArray());
		if (this.tokenAesKey.length != 16 && this.tokenAesKey.length != 24 && this.tokenAesKey.length != 32) {
			throw new IllegalArgumentException("Invalid token AES key length");
		}
		this.tokenHmacKey = Hex.decodeHex(hexHmacKey.toCharArray());
	}

	@Override
	public String generateToken(UserInfo userInfo) {
		TokenEntity tokenEntity = new TokenEntity();
		tokenEntity.setUserId(userInfo.getId());
		tokenEntity.setTimeOut(DateUtils.addSeconds(new Date(), tokenExpire));
		tokenEntity.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		userLoginList.add(tokenEntity.getUuid());
		return TokenUtils.generateToken(tokenEntity, tokenAesKey, tokenHmacKey);
	}

	@Override
	public boolean validateToken(String uuid) {
		for (String loginUUID : userLoginList) {
			if (loginUUID.equals(uuid)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void removeLoginListByUUID(String uuid) {
		for (int i = 0; i < userLoginList.size(); i++) {
			if (uuid.equals(userLoginList.get(i))) {
				userLoginList.remove(i);
			}
		}
	}

	@Override
	public List<UserInfoVo> getAllUserInfo() {
		return userInfoDao.getAllUserInfo();
	}

	@Override
	public List<UserInfo> getUserByUid(String uid) {
		try {
			List<UserInfo> list = new ArrayList<UserInfo>();
			UserInfo user = userInfoDao.selectUserByUserId(uid);
			if (user == null) {
				return list;
			}
			List<Role> roles = roleDao.getRoleByUserId(uid);
			//user.setRoleList(roles);
			list.add(user);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String generateToken(User user) {
		TokenEntity tokenEntity = new TokenEntity();
		tokenEntity.setUserId(user.getId());
		tokenEntity.setTimeOut(DateUtils.addSeconds(new Date(), tokenExpire));
		tokenEntity.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		userLoginList.add(tokenEntity.getUuid());
		return TokenUtils.generateToken(tokenEntity, tokenAesKey, tokenHmacKey);
	}

	// --------------------------------------------------------------------role
	
	 @Override
	    @Transactional
	    public boolean add(AddManagerUserDTO dto) {
		    logger.info("add manager param is " + dto);
		    //校验用户名, 邮箱, 角色是否已存在, 校验前后密码是否一致
		    if(userInfoDao.selectUserByEmail(dto.getEmail()) != null) {
		        logger.warn("add manager : user email already exist , param is " + dto);
		        throw new BusException(ReturnCode.EMAIL_ALREADY_EXIST);
	        }
		    if (userInfoDao.selectUserByUserName(dto.getUsername()) != null) {
	            logger.warn("add manager : user name  already exist , param is " + dto);
	            throw new BusException(ReturnCode.NAME_ALREADY_EXIST);
	        }

		    if (! dto.getPassword().equals(dto.getPasswordCheck())) {
	            logger.warn("add manager : password not match , param is " + dto);
	            throw new BusException(ReturnCode.BEFORE_AFTER_PASSWORD_NOT_MATCH);
	        }
		    UserInfo userInfo = new UserInfo();
		    userInfo.setId(IdGen.uuid());
		    userInfo.setUsername(dto.getUsername());
		    userInfo.setEmail(dto.getEmail());
		    userInfo.setMobile(dto.getMobile());
		    userInfo.setRemarks(dto.getRemarks());
		    userInfo.setCreateDate(new Date());
		    userInfo.setPassword(CryptoUtils.hashPassword(dto.getPassword().trim()));
		    userInfo.setState(1);
		    userInfo.setDelFlag(Integer.parseInt(DELETE_STATE.NOT_DELETE.getCode()));
	        userInfo.setRoles(dto.getRole());
		    if(userInfoDao.insert(userInfo) == 1) {
		        logger.info("add manager : success");
	            return true;
	        } else {
	            logger.warn("add manager : success");
		        return false;
	        }
	    }
	@Override
	public Page<Role> selectRoleList(SelectByTemplateDto dto) {
		// 根据是否有查询条件判断
		Page<Role> page = new Page<>(dto.getPageIndex(), dto.getPageSize());
		List<Role> list;
		int count;
		if (StringUtils.isEmpty(dto.getTemplate().trim())) {
			list = roleDao.selectValidList(page, DELETE_STATE.NOT_DELETE.getCode());
			count = roleDao.selectValidCount(DELETE_STATE.NOT_DELETE.getCode());
		} else {
			list = roleDao.selectValidListByTemplate(page, dto.getTemplate().trim(), DELETE_STATE.NOT_DELETE.getCode());
			count = roleDao.selectValidCountByTemplate(dto.getTemplate().trim(), DELETE_STATE.NOT_DELETE.getCode());
		}
		page.setTotal(count);
		page.setRecords(list);
		return page;
	}

	@Override
	public boolean addRole(AddRoleDto addRole) {
		logger.info("add Role : param is " + addRole);
		// 校验角色是否存在
		if (roleDao.selectRoleByRoleName(addRole.getRoleName(), DELETE_STATE.NOT_DELETE.getCode()) != null) {
			logger.warn("add Role : role already exists, param is " + addRole);
			throw new BusException(ReturnCode.ROLE_ALREADY_EXIST);
		}
		Role role = new Role();
		role.setId(IdGen.uuid());
		role.setRemarks(addRole.getRemarks());
		role.setRoleName(addRole.getRoleName());
		role.setCreateDate(new Date());
		role.setUpdateDate(new Date());
		role.setPermissions(addRole.getPermissions());
		role.setDelFlag((Integer.parseInt(DELETE_STATE.NOT_DELETE.getCode())));
		int result = roleDao.insert(role);
		if (result == 1) {
			logger.info("add Role : success");
			return true;
		} else {
			logger.warn("add Role : failed");
			return false;
		}
	}

	@Override
	public boolean updateRole(AddRoleDto addRole) {
		logger.info("update Role : param is " + addRole);
		Role role = new Role();
		role.setId(addRole.getId());
		role.setRemarks(addRole.getRemarks());
		// 暂时去掉 role.setRoleName(addRole.getRoleName());
		role.setPermissions(addRole.getPermissions());
		role.setUpdateDate(new Date());
		int result = roleDao.updateById(role);
		if (result == 1) {
			logger.info("update Role : success");
			return true;
		} else {
			logger.warn("update Role : failed");
			return false;
		}
	}

	@Override
	public List<String> selectAllRoleName() {
		return roleDao.getAllRoleNames(DELETE_STATE.NOT_DELETE.getCode());
	}

	@Override
	public Role selectRoleById(String id) {
		Role r = new Role();
		r.setId(id);
		r.setDelFlag(0);
		Role role = roleDao.selectOne(r);
		if (role == null) {
			logger.warn("selectRoleById failed , id is " + id);
			return null;
		} else {
			return role;
		}
	}
	 @Override
	    public String getPermissionByRoleName(String roleName) {
		    Role role = roleDao.selectRoleByRoleName(roleName, DELETE_STATE.NOT_DELETE.getCode());
		    if (role == null) {
		        return null;
	        } else {
		        return role.getPermissions();
	        }
	    }
	  @Override
	    public boolean update(AddManagerUserDTO dto) {
		    logger.info("update user : param is " + dto);
		    //查询用户是否存在
	        UserInfo user = userInfoDao.selectUserByUserId(dto.getId());
	        if (user == null) {
	            throw new BusException(ReturnCode.USER_NOT_EXIST);
	        }
		    UserInfo userInfo = new UserInfo();
		    userInfo.setId(dto.getId());
		    userInfo.setRoles(dto.getRole());
//		    userInfo.setUpdateDate(new Date());
		    userInfo.setRemarks(dto.getRemarks());
//		    userInfo.setMobile(dto.getMobile());
//		    userInfo.setUsername(dto.getUsername());
//		    userInfo.setEmail(dto.getEmail());
		    userInfo.setState(1);
		    if(userInfoDao.updateById(userInfo) == 1) {
		        logger.info("update user : success");
		        return true;
	        } else {
	            logger.warn("update user : failed");
	            return false;
	        }
	    }
}
